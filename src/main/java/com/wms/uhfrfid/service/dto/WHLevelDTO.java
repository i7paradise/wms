package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.WHLevel} entity.
 */
public class WHLevelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String note;

    private BayDTO bay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BayDTO getBay() {
        return bay;
    }

    public void setBay(BayDTO bay) {
        this.bay = bay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WHLevelDTO)) {
            return false;
        }

        WHLevelDTO wHLevelDTO = (WHLevelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wHLevelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WHLevelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", note='" + getNote() + "'" +
            ", bay=" + getBay() +
            "}";
    }
}
