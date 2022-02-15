package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Door.
 */
@Entity
@Table(name = "door")
public class Door implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "warehouse" }, allowSetters = true)
    private Area area;

    @OneToMany(mappedBy = "door")
    @JsonIgnoreProperties(value = { "door", "rfidAntenna" }, allowSetters = true)
    private Set<DoorAntenna> doorAntennas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Door id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Door name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getArea() {
        return this.area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Door area(Area area) {
        this.setArea(area);
        return this;
    }

    public Set<DoorAntenna> getDoorAntennas() {
        return this.doorAntennas;
    }

    public void setDoorAntennas(Set<DoorAntenna> doorAntennas) {
        if (this.doorAntennas != null) {
            this.doorAntennas.forEach(i -> i.setDoor(null));
        }
        if (doorAntennas != null) {
            doorAntennas.forEach(i -> i.setDoor(this));
        }
        this.doorAntennas = doorAntennas;
    }

    public Door doorAntennas(Set<DoorAntenna> doorAntennas) {
        this.setDoorAntennas(doorAntennas);
        return this;
    }

    public Door addDoorAntenna(DoorAntenna doorAntenna) {
        this.doorAntennas.add(doorAntenna);
        doorAntenna.setDoor(this);
        return this;
    }

    public Door removeDoorAntenna(DoorAntenna doorAntenna) {
        this.doorAntennas.remove(doorAntenna);
        doorAntenna.setDoor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Door)) {
            return false;
        }
        return id != null && id.equals(((Door) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Door{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
