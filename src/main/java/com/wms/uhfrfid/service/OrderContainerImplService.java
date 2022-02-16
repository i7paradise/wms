package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.OrderContainer;
import com.wms.uhfrfid.domain.OrderItem;
import com.wms.uhfrfid.repository.OrderContainerRepositoryImpl;
import com.wms.uhfrfid.repository.OrderItemRepository;
import com.wms.uhfrfid.service.dto.CreateWithTagsDTO;
import com.wms.uhfrfid.service.dto.OrderContainerDTO;
import com.wms.uhfrfid.service.mapper.OrderContainerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderContainerImplService {

    private final Logger log = LoggerFactory.getLogger(OrderContainerImplService.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderContainerRepositoryImpl orderContainerRepository;
    private final OrderContainerMapper orderContainerMapper;

    public OrderContainerImplService(OrderItemRepository orderItemRepository,
                                     OrderContainerRepositoryImpl orderContainerRepository,
                                     OrderContainerMapper orderContainerMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderContainerRepository = orderContainerRepository;
        this.orderContainerMapper = orderContainerMapper;
    }

    @Transactional(readOnly = true)
    public List<OrderContainerDTO> findOrderContainers(Long orderItemId) {
        return orderContainerRepository.findByOrderItemId(orderItemId)
            .stream()
            .map(orderContainerMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderContainerDTO> createOrderContainers(CreateWithTagsDTO createWithTags, String userLogin) {
        OrderItem orderItem = orderItemRepository.getById(createWithTags.getOrderItemId());
        List<OrderContainer> orderContainers = createWithTags.getTagsList()
            .getTags().stream()
            .map(e -> new OrderContainer()
                .supplierRFIDTag(e)
                .orderItem(orderItem))
            .collect(Collectors.toList());
        orderContainerRepository.saveAll(orderContainers);

        log.debug("user {} created {} OrderContainer", userLogin, orderContainers.size());
        return findOrderContainers(orderItem.getId());
    }

    public void delete(Long id, String userLogin) {
        //TODO load user-roles
        //TODO delete if user has rights to
        log.debug("delete order-container(id={}) by user {}", id, userLogin);
        orderContainerRepository.deleteById(id);
    }
}
