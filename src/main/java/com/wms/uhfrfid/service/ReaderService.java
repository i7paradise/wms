package com.wms.uhfrfid.service;

import com.wms.uhfrfid.repository.UHFRFIDReaderRepository;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTOV2;
import com.wms.uhfrfid.service.mapper.UHFRFIDReaderMapper;
import com.wms.uhfrfid.service.mapper.v2.UHFRFIDReaderV2Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReaderService extends UHFRFIDReaderService {

    private final Logger log = LoggerFactory.getLogger(ReaderService.class);

    private final UHFRFIDReaderRepository uHFRFIDReaderRepository;
    private final UHFRFIDReaderV2Mapper uhfrfidReaderV2Mapper;

    public ReaderService(UHFRFIDReaderRepository uHFRFIDReaderRepository, UHFRFIDReaderMapper uHFRFIDReaderMapper,
                         UHFRFIDReaderV2Mapper uhfrfidReaderV2Mapper) {
        super(uHFRFIDReaderRepository, uHFRFIDReaderMapper);
        this.uHFRFIDReaderRepository = uHFRFIDReaderRepository;
        this.uhfrfidReaderV2Mapper = uhfrfidReaderV2Mapper;
    }

    @Transactional(readOnly = true)
    public Optional<UHFRFIDReaderDTOV2> findOneDetailed(Long id) {
        log.debug("Request to get UHFRFIDReader : {}", id);
        return uHFRFIDReaderRepository.findById(id)
            .map(e -> {
                e.getUhfRFIDAntennas();
                return e;
            })
            .map(uhfrfidReaderV2Mapper::toDto);
    }
}
