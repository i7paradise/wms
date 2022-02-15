package com.wms.uhfrfid.service.dto;

import java.util.List;

public class TagsList {

    private final List<String> tags;

    public TagsList(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
