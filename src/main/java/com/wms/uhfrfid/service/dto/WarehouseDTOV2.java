package com.wms.uhfrfid.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.Warehouse} entity.
 */
@Schema(description = "Warehouse of the company")
public class WarehouseDTOV2 extends WarehouseDTO {

	private List<LocationDTO> locations;

	public List<LocationDTO> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationDTO> locations) {
		this.locations = locations;
	}
}
