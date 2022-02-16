package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Warehouse of the company
 */
@Entity
@Table(name = "warehouse")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "note")
    private String note;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @ManyToOne
    @JsonIgnoreProperties(value = { "companyUsers", "companyContainers", "orders", "uhfRFIDReaders" }, allowSetters = true)
    private Company company;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = { "warehouse" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = { "warehouse" }, allowSetters = true)
    private Set<Area> areas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Warehouse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Warehouse name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return this.note;
    }

    public Warehouse note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhone() {
        return this.phone;
    }

    public Warehouse phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public Warehouse contactPerson(String contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Warehouse company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.setWarehouse(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setWarehouse(this));
        }
        this.locations = locations;
    }

    public Warehouse locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Warehouse addLocation(Location location) {
        this.locations.add(location);
        location.setWarehouse(this);
        return this;
    }

    public Warehouse removeLocation(Location location) {
        this.locations.remove(location);
        location.setWarehouse(null);
        return this;
    }

    public Set<Area> getAreas() {
        return this.areas;
    }

    public void setAreas(Set<Area> areas) {
        if (this.areas != null) {
            this.areas.forEach(i -> i.setWarehouse(null));
        }
        if (areas != null) {
            areas.forEach(i -> i.setWarehouse(this));
        }
        this.areas = areas;
    }

    public Warehouse areas(Set<Area> areas) {
        this.setAreas(areas);
        return this;
    }

    public Warehouse addArea(Area area) {
        this.areas.add(area);
        area.setWarehouse(this);
        return this;
    }

    public Warehouse removeArea(Area area) {
        this.areas.remove(area);
        area.setWarehouse(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse)) {
            return false;
        }
        return id != null && id.equals(((Warehouse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Warehouse{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", note='" + getNote() + "'" +
            ", phone='" + getPhone() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            "}";
    }
}
