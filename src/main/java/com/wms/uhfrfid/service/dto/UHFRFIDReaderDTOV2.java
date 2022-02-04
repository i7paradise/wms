package com.wms.uhfrfid.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UHFRFIDReaderDTOV2 extends UHFRFIDReaderDTO {

    private List<UHFRFIDAntennaDTO> uhfRFIDAntennas;

    public void setUHFRFIDAntennas(List<UHFRFIDAntennaDTO> uhfRFIDAntennas) {
        this.uhfRFIDAntennas = uhfRFIDAntennas;
    }

    public List<UHFRFIDAntennaDTO> getDeliveryOrderItems() {
        return uhfRFIDAntennas;
    }
}