package com.wms.uhfrfid.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderContainerImplDTO extends OrderContainerDTO {

    private Long countProducts;

    public Long getCountProducts() {
        return countProducts;
    }

    public void setCountProducts(Long countProducts) {
        this.countProducts = countProducts;
    }
}
