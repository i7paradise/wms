package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import com.wms.uhfrfid.repository.UHFRFIDAntennaRepository;
import com.wms.uhfrfid.service.dto.ScanRequest;
import com.wms.uhfrfid.service.dto.TagsList;
import com.wms.uhfrfid.service.rfid.reader.RFIDTag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class TagsService {

    private final Logger log = LoggerFactory.getLogger(TagsService.class);

    private final UHFRFIDAntennaRepository antennaRepository;

    public TagsService(UHFRFIDAntennaRepository antennaRepository) {
        this.antennaRepository = antennaRepository;
    }

    public TagsList scan(ScanRequest scanRequest) {
        tempSilentSleep();
        UHFRFIDAntenna antenna = antennaRepository.getById(scanRequest.getAntennaId());
        log.debug("scanning with {}", antenna);
        List<String> tags = mockRFIDReaderCall(scanRequest.getCount())
            .stream()
            .map(RFIDTag::get_EPC)
            .collect(Collectors.toList());
        return new TagsList(tags);
    }

    @NotNull
    private List<RFIDTag> mockRFIDReaderCall(Integer count) {
        return IntStream.range(0, count)
            .mapToObj(e -> {
                RFIDTag rfidTag = new RFIDTag();
                rfidTag.set_EPC(UUID.randomUUID().toString());
                return rfidTag;
            })
            .collect(Collectors.toList());
    }

    private void tempSilentSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.debug("TagsService sleep", e);
        }
    }
}
