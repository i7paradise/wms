package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.OrderItem;
import com.wms.uhfrfid.domain.enumeration.OrderItemStatus;
import com.wms.uhfrfid.repository.OrderContainerRepositoryImpl;
import com.wms.uhfrfid.repository.OrderItemRepository;
import com.wms.uhfrfid.security.SecurityUtils;
import com.wms.uhfrfid.service.dto.OrderItemDTO;
import com.wms.uhfrfid.service.mapper.OrderItemMapper;
import com.wms.uhfrfid.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Status;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ReceptionItemService extends OrderItemService {

    private final Logger log = LoggerFactory.getLogger(ReceptionItemService.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ReceptionService receptionService;
    private final OrderContainerRepositoryImpl orderContainerRepository;

    public ReceptionItemService(OrderItemRepository orderItemRepository,
                                OrderItemMapper orderItemMapper, ReceptionService receptionService,
                                OrderContainerRepositoryImpl orderContainerRepository) {
        super(orderItemRepository, orderItemMapper);
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.receptionService = receptionService;
        this.orderContainerRepository = orderContainerRepository;
    }

    @Override
    public OrderItemDTO save(OrderItemDTO orderItemDTO) {
        OrderItem byId;
        try {
            byId = orderItemRepository.getById(orderItemDTO.getId());
        } catch (EntityNotFoundException e) {
            throw new BadRequestAlertException("Entity not found", "order-item", "idnotfound");
        }
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        checkRightToUpdate(byId, userLogin);


        log.debug("Request to save OrderItem : {}", orderItemDTO);
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem = orderItemRepository.save(orderItem);

        //TODO log in table user-activity
        return orderItemMapper.toDto(orderItem);
    }

    public void statusForward(OrderItem orderItem) {
        next(orderItem.getStatus())
            .filter(e -> e == OrderItemStatus.COMPLETED && isAllScanned(orderItem)
                || e == OrderItemStatus.IN_PROGRESS)
            .ifPresent(e -> {
                orderItem.setStatus(e);
                orderItemRepository.save(orderItem);
                receptionService.statusForward(orderItem.getOrder());
            });
    }

    private static Optional<OrderItemStatus> next(OrderItemStatus status) {
        return switch (status) {
            case DRAFT -> Optional.of(OrderItemStatus.IN_PROGRESS);
            case IN_PROGRESS -> Optional.of(OrderItemStatus.COMPLETED);
            default -> Optional.empty();
        };
    }

    @Transactional(readOnly = true)
    public boolean isAllScanned(OrderItem orderItem) {
        return Objects.equals(orderItem.getContainersCount(), orderContainerRepository.countScanned(orderItem.getId()));
    }

    private void checkRightToUpdate(OrderItem orderItem, String userLogin) {
        if ("admin".equals(userLogin)) {
            return;
        }
        if (orderItem.getStatus() == OrderItemStatus.COMPLETED) {
            throw new ConstraintViolationProblem(
                Status.BAD_REQUEST, List.of(new Violation("status", "can not update when status " + OrderItemStatus.COMPLETED))
            );
        }
    }
}
