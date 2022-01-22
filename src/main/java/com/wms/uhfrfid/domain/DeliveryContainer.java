package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeliveryContainer.
 */
@Entity
@Table(name = "delivery_container")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeliveryContainer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "supplier_rfid_tag")
    private String supplierRFIDTag;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compganyProduct", "deliveryOrder" }, allowSetters = true)
    private DeliveryOrderItem deliveryOrderItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "container" }, allowSetters = true)
    private CompanyContainer companyContainer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryContainer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierRFIDTag() {
        return this.supplierRFIDTag;
    }

    public DeliveryContainer supplierRFIDTag(String supplierRFIDTag) {
        this.setSupplierRFIDTag(supplierRFIDTag);
        return this;
    }

    public void setSupplierRFIDTag(String supplierRFIDTag) {
        this.supplierRFIDTag = supplierRFIDTag;
    }

    public DeliveryOrderItem getDeliveryOrderItem() {
        return this.deliveryOrderItem;
    }

    public void setDeliveryOrderItem(DeliveryOrderItem deliveryOrderItem) {
        this.deliveryOrderItem = deliveryOrderItem;
    }

    public DeliveryContainer deliveryOrderItem(DeliveryOrderItem deliveryOrderItem) {
        this.setDeliveryOrderItem(deliveryOrderItem);
        return this;
    }

    public CompanyContainer getCompanyContainer() {
        return this.companyContainer;
    }

    public void setCompanyContainer(CompanyContainer companyContainer) {
        this.companyContainer = companyContainer;
    }

    public DeliveryContainer companyContainer(CompanyContainer companyContainer) {
        this.setCompanyContainer(companyContainer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryContainer)) {
            return false;
        }
        return id != null && id.equals(((DeliveryContainer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryContainer{" +
            "id=" + getId() +
            ", supplierRFIDTag='" + getSupplierRFIDTag() + "'" +
            "}";
    }
}
