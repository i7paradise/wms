package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.domain.User;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import com.wms.uhfrfid.repository.DeliveryOrderRepository;
import com.wms.uhfrfid.repository.ReceptionRepository;
import com.wms.uhfrfid.repository.UserRepository;
import com.wms.uhfrfid.service.mapper.DeliveryOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceptionService extends DeliveryOrderService {

    private final Logger log = LoggerFactory.getLogger(ReceptionService.class);

    private final ReceptionRepository receptionRepository;
    private final UserRepository userRepository;

    public ReceptionService(
        DeliveryOrderRepository deliveryOrderRepository,
        DeliveryOrderMapper deliveryOrderMapper,
        ReceptionRepository receptionRepository,
        UserRepository userRepository
    ) {
        super(deliveryOrderRepository, deliveryOrderMapper);
        this.receptionRepository = receptionRepository;
        this.userRepository = userRepository;
    }

    public Page<DeliveryOrder> findAllOpen(String userLogin, Pageable pageable) {
        User user = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalArgumentException("TODO ReceptionService user not found"));
        //TODO add user to the query
        return receptionRepository.findDeliveryOrdersByStatus(DeliveryOrderStatus.PENDING, pageable);
    }
}
