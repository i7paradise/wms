package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.service.TagsService;
import com.wms.uhfrfid.service.dto.DoorAntennaDTO;
import com.wms.uhfrfid.service.dto.TagsList;
import com.wms.uhfrfid.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TagsResource controller
 */
@RestController
@RequestMapping(TagsResource.PATH)
public class TagsResource {

    public static final String PATH = "/api/v1/tags";

    private final Logger log = LoggerFactory.getLogger(TagsResource.class);
    private final TagsService tagsService;

    public TagsResource(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    /**
     * GET scan
     */
    @PostMapping("/scan")
    public TagsList scan(@RequestBody DoorAntennaDTO doorAntenna) {
        log.debug("calling /scan {}", doorAntenna);
        if (doorAntenna.getId() == null) {
            throw new BadRequestAlertException("Invalid id", DoorAntennaDTO.class.getSimpleName(), "id null");
        }
        return tagsService.scan(doorAntenna);
    }
}
