package com.wms.uhfrfid.service;

import com.wms.uhfrfid.service.dto.DoorAntennaDTO;
import com.wms.uhfrfid.service.dto.TagsList;
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

    public TagsList scan(DoorAntennaDTO doorAntenna) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.debug("TagsService sleep", e);
        }
        List<String> tags = IntStream.range(0, 10)
            .mapToObj(e -> UUID.randomUUID().toString())
            .collect(Collectors.toList());
        return new TagsList(tags);
    }
}
