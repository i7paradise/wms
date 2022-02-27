package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wms.uhfrfid.domain.enumeration.UHFRFIDAntennaStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A UHFRFIDAntenna.
 */
@Entity
@Table(name = "uhfrfid_antenna")
public class UHFRFIDAntenna implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "output_power", nullable = false)
    private Integer outputPower;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UHFRFIDAntennaStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "uhfRFIDAntennas" }, allowSetters = true)
    private UHFRFIDReader uhfRFIDReader;

    @OneToMany(mappedBy = "rfidAntenna")
    @JsonIgnoreProperties(value = { "door", "rfidAntenna" }, allowSetters = true)
    private Set<DoorAntenna> doorAntennas = new HashSet<>();

    @OneToMany(mappedBy = "rfidAntenna")
    @JsonIgnoreProperties(value = { "door", "rfidAntenna" }, allowSetters = true)
    private Set<DoorAntenna> doorAntennas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UHFRFIDAntenna id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UHFRFIDAntenna name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOutputPower() {
        return this.outputPower;
    }

    public UHFRFIDAntenna outputPower(Integer outputPower) {
        this.setOutputPower(outputPower);
        return this;
    }

    public void setOutputPower(Integer outputPower) {
        this.outputPower = outputPower;
    }

    public UHFRFIDAntennaStatus getStatus() {
        return this.status;
    }

    public UHFRFIDAntenna status(UHFRFIDAntennaStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(UHFRFIDAntennaStatus status) {
        this.status = status;
    }

    public UHFRFIDReader getUhfRFIDReader() {
        return this.uhfRFIDReader;
    }

    public void setUhfRFIDReader(UHFRFIDReader uHFRFIDReader) {
        this.uhfRFIDReader = uHFRFIDReader;
    }

    public UHFRFIDAntenna uhfRFIDReader(UHFRFIDReader uHFRFIDReader) {
        this.setUhfRFIDReader(uHFRFIDReader);
        return this;
    }

    public Set<DoorAntenna> getDoorAntennas() {
        return this.doorAntennas;
    }

    public void setDoorAntennas(Set<DoorAntenna> doorAntennas) {
        if (this.doorAntennas != null) {
            this.doorAntennas.forEach(i -> i.setRfidAntenna(null));
        }
        if (doorAntennas != null) {
            doorAntennas.forEach(i -> i.setRfidAntenna(this));
        }
        this.doorAntennas = doorAntennas;
    }

    public UHFRFIDAntenna doorAntennas(Set<DoorAntenna> doorAntennas) {
        this.setDoorAntennas(doorAntennas);
        return this;
    }

    public UHFRFIDAntenna addDoorAntenna(DoorAntenna doorAntenna) {
        this.doorAntennas.add(doorAntenna);
        doorAntenna.setRfidAntenna(this);
        return this;
    }

    public UHFRFIDAntenna removeDoorAntenna(DoorAntenna doorAntenna) {
        this.doorAntennas.remove(doorAntenna);
        doorAntenna.setRfidAntenna(null);
        return this;
    }

    public Set<DoorAntenna> getDoorAntennas() {
        return this.doorAntennas;
    }

    public void setDoorAntennas(Set<DoorAntenna> doorAntennas) {
        if (this.doorAntennas != null) {
            this.doorAntennas.forEach(i -> i.setRfidAntenna(null));
        }
        if (doorAntennas != null) {
            doorAntennas.forEach(i -> i.setRfidAntenna(this));
        }
        this.doorAntennas = doorAntennas;
    }

    public UHFRFIDAntenna doorAntennas(Set<DoorAntenna> doorAntennas) {
        this.setDoorAntennas(doorAntennas);
        return this;
    }

    public UHFRFIDAntenna addDoorAntenna(DoorAntenna doorAntenna) {
        this.doorAntennas.add(doorAntenna);
        doorAntenna.setRfidAntenna(this);
        return this;
    }

    public UHFRFIDAntenna removeDoorAntenna(DoorAntenna doorAntenna) {
        this.doorAntennas.remove(doorAntenna);
        doorAntenna.setRfidAntenna(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UHFRFIDAntenna)) {
            return false;
        }
        return id != null && id.equals(((UHFRFIDAntenna) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UHFRFIDAntenna{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", outputPower=" + getOutputPower() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
