package com.wms.uhfrfid.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.uhfrfid.repository.WarehouseRepository;
import com.wms.uhfrfid.service.dto.WarehouseDTOV2;
import com.wms.uhfrfid.service.mapper.WarehouseMapper;
import com.wms.uhfrfid.service.mapper.v2.WarehouseV2Mapper;

@Service
@Transactional
public class XSPSWarehouseService extends WarehouseService {

    private final Logger log = LoggerFactory.getLogger(XSPSWarehouseService.class);

    private final WarehouseRepository warehouseRepository;
    private final WarehouseV2Mapper warehouseV2Mapper;

    public XSPSWarehouseService(WarehouseRepository warehouseRepository, WarehouseMapper warehouseMapper,
    		WarehouseV2Mapper warehouseV2Mapper) {
        super(warehouseRepository, warehouseMapper);
        this.warehouseRepository = warehouseRepository;
        this.warehouseV2Mapper = warehouseV2Mapper;
    }

    @Transactional(readOnly = true)
    public Optional<WarehouseDTOV2> findOneDetailed(Long id) {
        log.debug("Request to get Locations : {}", id);
        return warehouseRepository.findById(id)
            .map(e -> {
                e.getLocations();
                return e;
            })
            .map(warehouseV2Mapper::toDto);
    }
}
