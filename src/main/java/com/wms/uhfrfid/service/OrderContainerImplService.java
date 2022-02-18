package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.OrderContainer;
import com.wms.uhfrfid.domain.OrderItem;
import com.wms.uhfrfid.domain.OrderItemProduct;
import com.wms.uhfrfid.domain.ProductsByContainer;
import com.wms.uhfrfid.repository.OrderContainerRepositoryImpl;
import com.wms.uhfrfid.repository.OrderItemProductRepositoryImpl;
import com.wms.uhfrfid.repository.OrderItemRepository;
import com.wms.uhfrfid.service.dto.CreateWithTagsDTO;
import com.wms.uhfrfid.service.dto.OrderContainerImplDTO;
import com.wms.uhfrfid.service.dto.OrderItemProductDTO;
import com.wms.uhfrfid.service.dto.TagsList;
import com.wms.uhfrfid.service.mapper.OrderContainerImplMapper;
import com.wms.uhfrfid.service.mapper.OrderItemProductMapper;
import com.wms.uhfrfid.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderContainerImplService {

    private final Logger log = LoggerFactory.getLogger(OrderContainerImplService.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderContainerRepositoryImpl orderContainerRepository;
    private final OrderContainerImplMapper orderContainerMapper;
    private final OrderItemProductRepositoryImpl orderItemProductRepository;
    private final OrderItemProductMapper orderItemProductMapper;

    public OrderContainerImplService(OrderItemRepository orderItemRepository,
                                     OrderContainerRepositoryImpl orderContainerRepository,
                                     OrderContainerImplMapper orderContainerMapper,
                                     OrderItemProductRepositoryImpl orderItemProductRepository,
                                     OrderItemProductMapper orderItemProductMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderContainerRepository = orderContainerRepository;
        this.orderContainerMapper = orderContainerMapper;
        this.orderItemProductRepository = orderItemProductRepository;
        this.orderItemProductMapper = orderItemProductMapper;
    }

    @Transactional(readOnly = true)
    public List<OrderContainerImplDTO> findOrderContainers(Long orderItemId) {
        List<OrderContainerImplDTO> list = orderContainerRepository.findByOrderItemId(orderItemId)
            .stream()
            .map(orderContainerMapper::toDto)
            .collect(Collectors.toList());
        if (!list.isEmpty()) {
            Map<Long, Long> mapContainerIdByProductsCount = orderItemProductRepository.countIdByOrderContainer()
                .stream()
                .collect(Collectors.toMap(ProductsByContainer::getContainerId, ProductsByContainer::getProductsCount));
            list.forEach(e -> e.setCountProducts(
                        mapContainerIdByProductsCount.get(e.getId())
                    )
                );
        }
        return list;
    }

    public List<OrderContainerImplDTO> createOrderContainers(CreateWithTagsDTO createWithTags, String userLogin) {
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
        deleteItemProducts(id, userLogin);
        log.debug("delete order-container(id={}) by user {}", id, userLogin);
        orderContainerRepository.deleteById(id);
    }

    public List<OrderItemProductDTO> createOrderItemProducts(Long id, TagsList tagsList, String userLogin) {
        OrderContainer orderContainer = orderContainerRepository.getById(id);
        List<OrderItemProduct> orderItemProducts = tagsList.getTags().stream()
            .map(e -> new OrderItemProduct()
                .orderContainer(orderContainer)
                .rfidTAG(e)
            ).collect(Collectors.toList());

        return orderItemProductRepository.saveAll(orderItemProducts)
            .stream()
            .map(orderItemProductMapper::toDto)
            .collect(Collectors.toList());
    }

    public void deleteItemProducts(Long orderContainerId, String userLogin) {
        checkUpdateRights(orderContainerId, userLogin);
        orderItemProductRepository.deleteByOrderContainerId(orderContainerId);
    }

    private void checkUpdateRights(Long orderContainerId, String userLogin) {
        //TODO TEMP code
        /*
        if (!"admin".equals(userLogin)) {
            throw new BadRequestAlertException("no update right", "order-item", "no-update");
        }
        */
        //TODO
    }
}
