package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A CompanyContainer.
 */
@Entity
@Table(name = "company_container")
public class CompanyContainer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rfid_tag")
    private String rfidTag;

    @Column(name = "color")
    private String color;

    @JsonIgnoreProperties(value = { "orderItem", "orderItemProducts" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ContainerCategory containerCategory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "companyUsers", "companyContainers", "orders", "uhfRFIDReaders" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompanyContainer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfidTag() {
        return this.rfidTag;
    }

    public CompanyContainer rfidTag(String rfidTag) {
        this.setRfidTag(rfidTag);
        return this;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }

    public String getColor() {
        return this.color;
    }

    public CompanyContainer color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ContainerCategory getContainerCategory() {
        return this.containerCategory;
    }

    public void setContainerCategory(ContainerCategory containerCategory) {
        this.containerCategory = containerCategory;
    }

    public CompanyContainer containerCategory(ContainerCategory containerCategory) {
        this.setContainerCategory(containerCategory);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyContainer company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyContainer)) {
            return false;
        }
        return id != null && id.equals(((CompanyContainer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyContainer{" +
            "id=" + getId() +
            ", rfidTag='" + getRfidTag() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
