package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.Bay} entity.
 */
public class BayDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String note;

    private WHRowDTO whrow;

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

    public WHRowDTO getWhrow() {
        return whrow;
    }

    public void setWhrow(WHRowDTO whrow) {
        this.whrow = whrow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BayDTO)) {
            return false;
        }

        BayDTO bayDTO = (BayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BayDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", note='" + getNote() + "'" +
            ", whrow=" + getWhrow() +
            "}";
    }
}
