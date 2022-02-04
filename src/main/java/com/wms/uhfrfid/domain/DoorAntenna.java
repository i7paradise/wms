package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wms.uhfrfid.domain.enumeration.DoorAntennaType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DoorAntenna.
 */
@Entity
@Table(name = "door_antenna")
public class DoorAntenna implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DoorAntennaType type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "area" }, allowSetters = true)
    private Door door;

    @ManyToOne
    @JsonIgnoreProperties(value = { "uhfRFIDReader" }, allowSetters = true)
    private UHFRFIDAntenna uhfRFIDAntenna;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DoorAntenna id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoorAntennaType getType() {
        return this.type;
    }

    public DoorAntenna type(DoorAntennaType type) {
        this.setType(type);
        return this;
    }

    public void setType(DoorAntennaType type) {
        this.type = type;
    }

    public Door getDoor() {
        return this.door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public DoorAntenna door(Door door) {
        this.setDoor(door);
        return this;
    }

    public UHFRFIDAntenna getUhfRFIDAntenna() {
        return this.uhfRFIDAntenna;
    }

    public void setUhfRFIDAntenna(UHFRFIDAntenna uHFRFIDAntenna) {
        this.uhfRFIDAntenna = uHFRFIDAntenna;
    }

    public DoorAntenna uhfRFIDAntenna(UHFRFIDAntenna uHFRFIDAntenna) {
        this.setUhfRFIDAntenna(uHFRFIDAntenna);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoorAntenna)) {
            return false;
        }
        return id != null && id.equals(((DoorAntenna) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoorAntenna{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
