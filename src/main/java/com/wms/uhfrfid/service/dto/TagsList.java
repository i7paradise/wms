package com.wms.uhfrfid.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TagsList {

    private final List<String> tags;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TagsList(@JsonProperty("tags") List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "TagsList{" +
            "tags=" + tags +
            '}';
    }
}
