package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.User;
import com.wms.uhfrfid.repository.UHFRFIDReaderRepository;
import com.wms.uhfrfid.repository.UserRepository;
import com.wms.uhfrfid.repository.XSPSUHFRFIDReaderRepository;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTO;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTOV2;
import com.wms.uhfrfid.service.mapper.UHFRFIDReaderMapper;
import com.wms.uhfrfid.service.mapper.v2.UHFRFIDReaderV2Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Primary
@Service
@Transactional
public class XSPSUHFRFIDReaderService extends UHFRFIDReaderService {

    private final Logger log = LoggerFactory.getLogger(XSPSUHFRFIDReaderService.class);

    private final UHFRFIDReaderRepository uhfRFIDReaderRepository;
    private final XSPSUHFRFIDReaderRepository xspsUHFRFIDReaderRepository;
    private final UserRepository userRepository;
    private final UHFRFIDReaderMapper uhfRFIDReaderMapper;
    private final UHFRFIDReaderV2Mapper uhfRFIDReaderV2Mapper;

    public XSPSUHFRFIDReaderService(
        UHFRFIDReaderRepository uhfRFIDReaderRepository,
        UHFRFIDReaderMapper uhfRFIDReaderMapper,
        XSPSUHFRFIDReaderRepository xspsUHFRFIDReaderRepository,
        UserRepository userRepository,
        UHFRFIDReaderV2Mapper uhfRFIDReaderV2Mapper
    ) {
        super(uhfRFIDReaderRepository, uhfRFIDReaderMapper);
        this.uhfRFIDReaderRepository = uhfRFIDReaderRepository;
        this.uhfRFIDReaderMapper = uhfRFIDReaderMapper;
        this.xspsUHFRFIDReaderRepository = xspsUHFRFIDReaderRepository;
        this.userRepository = userRepository;
        this.uhfRFIDReaderV2Mapper = uhfRFIDReaderV2Mapper;
    }

    @Transactional(readOnly = true)
    public Page<UHFRFIDReaderDTO> findAll(String userLogin, Pageable pageable) {
        User user = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalArgumentException("TODO UHFRFIDReaderService user not found"));
        //TODO add user to the query
        return xspsUHFRFIDReaderRepository.findAll(pageable).map(uhfRFIDReaderMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<UHFRFIDReaderDTOV2> findOne(Long id, String userLogin) {
        log.debug("Request to get UHFRFIDReader : {}", id);
        //TODO implement find by user
        return uhfRFIDReaderRepository
            .findById(id)
            .map(e -> {
                e.getUhfRFIDAntennas();
                return e;
            })
            .map(uhfRFIDReaderV2Mapper::toDto);
    }
}
