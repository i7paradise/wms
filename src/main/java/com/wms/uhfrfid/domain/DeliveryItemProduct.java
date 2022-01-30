package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DeliveryItemProduct.
 */
@Entity
@Table(name = "delivery_item_product")
public class DeliveryItemProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "rfid_tag", nullable = false, unique = true)
    private String rfidTAG;

    @ManyToOne
    @JsonIgnoreProperties(value = { "deliveryOrderItem", "companyContainer" }, allowSetters = true)
    private DeliveryContainer deliveryContainer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryItemProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfidTAG() {
        return this.rfidTAG;
    }

    public DeliveryItemProduct rfidTAG(String rfidTAG) {
        this.setRfidTAG(rfidTAG);
        return this;
    }

    public void setRfidTAG(String rfidTAG) {
        this.rfidTAG = rfidTAG;
    }

    public DeliveryContainer getDeliveryContainer() {
        return this.deliveryContainer;
    }

    public void setDeliveryContainer(DeliveryContainer deliveryContainer) {
        this.deliveryContainer = deliveryContainer;
    }

    public DeliveryItemProduct deliveryContainer(DeliveryContainer deliveryContainer) {
        this.setDeliveryContainer(deliveryContainer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryItemProduct)) {
            return false;
        }
        return id != null && id.equals(((DeliveryItemProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryItemProduct{" +
            "id=" + getId() +
            ", rfidTAG='" + getRfidTAG() + "'" +
            "}";
    }
}
