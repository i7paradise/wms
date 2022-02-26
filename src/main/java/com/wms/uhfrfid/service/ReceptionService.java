package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.User;
import com.wms.uhfrfid.domain.enumeration.OrderStatus;
import com.wms.uhfrfid.repository.ReceptionRepository;
import com.wms.uhfrfid.repository.UserRepository;
import com.wms.uhfrfid.service.dto.OrderDTO;
import com.wms.uhfrfid.service.dto.OrderDTOV2;
import com.wms.uhfrfid.service.mapper.v2.OrderV2Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReceptionService {

    private final Logger log = LoggerFactory.getLogger(ReceptionService.class);

    private final ReceptionRepository receptionRepository;
    private final UserRepository userRepository;
    private final OrderV2Mapper orderV2Mapper;

    public ReceptionService(
        ReceptionRepository receptionRepository,
        UserRepository userRepository,
        OrderV2Mapper orderV2Mapper
    ) {
        this.receptionRepository = receptionRepository;
        this.userRepository = userRepository;
        this.orderV2Mapper = orderV2Mapper;
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(String userLogin, Pageable pageable) {
        User user = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalArgumentException("TODO ReceptionService user not found"));
        //TODO add user to the query
        return receptionRepository.findOrdersByStatus(OrderStatus.PENDING, pageable).map(orderV2Mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<OrderDTOV2> findOne(Long id, String userLogin) {
        log.debug("Request to get Order : {}", id);
        //TODO implement find by user
        return receptionRepository
            .findById(id)
            .map(e -> {
                e.getOrderItems();
                return e;
            })
            .map(orderV2Mapper::toDto);
    }
}
