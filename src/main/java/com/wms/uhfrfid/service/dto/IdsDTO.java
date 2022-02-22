package com.wms.uhfrfid.service.dto;

import java.util.Set;

public class IdsDTO {
    private Set<Long> ids;

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "IdsDTO{" +
            "ids=" + ids +
            '}';
    }
}
