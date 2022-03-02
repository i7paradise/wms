package com.wms.uhfrfid.service.dto;

public class ScanRequest {

    private Long antennaId;
    private Integer count;

    public Long getAntennaId() {
        return antennaId;
    }

    public void setAntennaId(Long antennaId) {
        this.antennaId = antennaId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
