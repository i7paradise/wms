package com.wms.uhfrfid.service.dto;

public class CreateWithTagsDTO {
    private Long orderItemId;
    private TagsList tagsList;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public TagsList getTagsList() {
        return tagsList;
    }

    public void setTagsList(TagsList tagsList) {
        this.tagsList = tagsList;
    }
}
