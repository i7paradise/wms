package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.service.TagsService;
import com.wms.uhfrfid.service.dto.ScanRequest;
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
    public TagsList scan(@RequestBody ScanRequest scanRequest) {
        log.debug("calling /scan {}", scanRequest);
        if (scanRequest.getAntennaId() == null) {
            throw new BadRequestAlertException("Invalid antenna id", ScanRequest.class.getSimpleName(), "id null");
        }
        return tagsService.scan(scanRequest);
    }
}
