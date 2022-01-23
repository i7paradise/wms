package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.User;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import com.wms.uhfrfid.repository.DeliveryOrderRepository;
import com.wms.uhfrfid.repository.ReceptionRepository;
import com.wms.uhfrfid.repository.UserRepository;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTO;
import com.wms.uhfrfid.service.mapper.DeliveryOrderMapper;
import java.util.Optional;
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
    private final DeliveryOrderMapper deliveryOrderMapper;

    public ReceptionService(
        DeliveryOrderRepository deliveryOrderRepository,
        DeliveryOrderMapper deliveryOrderMapper,
        ReceptionRepository receptionRepository,
        UserRepository userRepository
    ) {
        super(deliveryOrderRepository, deliveryOrderMapper);
        this.deliveryOrderMapper = deliveryOrderMapper;
        this.receptionRepository = receptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<DeliveryOrderDTO> findAll(String userLogin, Pageable pageable) {
        User user = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalArgumentException("TODO ReceptionService user not found"));
        //TODO add user to the query
        return receptionRepository.findDeliveryOrdersByStatus(DeliveryOrderStatus.PENDING, pageable).map(deliveryOrderMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<DeliveryOrderDTO> findOne(Long id, String userLogin) {
        //TODO implement find by user
        return super.findOne(id);
    }
}
