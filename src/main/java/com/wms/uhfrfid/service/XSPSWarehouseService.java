package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.User;
import com.wms.uhfrfid.domain.enumeration.OrderStatus;
import com.wms.uhfrfid.repository.ReceptionRepository;
import com.wms.uhfrfid.repository.UserRepository;
import com.wms.uhfrfid.repository.WarehouseRepository;
import com.wms.uhfrfid.repository.XSPSWarehouseRepository;
import com.wms.uhfrfid.service.dto.WarehouseDTO;
import com.wms.uhfrfid.service.dto.WarehouseDTOV2;
import com.wms.uhfrfid.service.mapper.WarehouseMapper;
import com.wms.uhfrfid.service.mapper.v2.WarehouseV2Mapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XSPSWarehouseService extends WarehouseService {

    private final Logger log = LoggerFactory.getLogger(XSPSWarehouseService.class);

    private final WarehouseRepository warehouseRepository;
    private final XSPSWarehouseRepository xspsWarehouseRepository;
    private final UserRepository userRepository;
    private final WarehouseMapper warehouseMapper;
    private final WarehouseV2Mapper warehouseV2Mapper;

    public XSPSWarehouseService(
    		WarehouseRepository warehouseRepository,
    		WarehouseMapper warehouseMapper,
    		XSPSWarehouseRepository xspsWarehouseRepository,
    		UserRepository userRepository,
    		WarehouseV2Mapper warehouseV2Mapper
    ) {
        super(warehouseRepository, warehouseMapper);
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.xspsWarehouseRepository = xspsWarehouseRepository;
        this.userRepository = userRepository;
        this.warehouseV2Mapper = warehouseV2Mapper;
    }

    @Transactional(readOnly = true)
    public Page<WarehouseDTO> findAll(String userLogin, Pageable pageable) {
        User user = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalArgumentException("TODO ReceptionService user not found"));
        //TODO add user to the query
        return warehouseRepository.findAll(pageable).map(warehouseMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<WarehouseDTOV2> findOne(Long id, String userLogin) {
        log.debug("Request to get Order : {}", id);
        //TODO implement find by user
        return warehouseRepository
            .findById(id)
            .map(e -> {
            	e.getLocations();
                return e;
            })
            .map(warehouseV2Mapper::toDto);
    }
}
