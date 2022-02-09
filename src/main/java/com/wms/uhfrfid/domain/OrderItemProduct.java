package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A OrderItemProduct.
 */
@Entity
@Table(name = "order_item_product")
public class OrderItemProduct implements Serializable {

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
    @JsonIgnoreProperties(value = { "companyContainer", "orderItem" }, allowSetters = true)
    private OrderContainer orderItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItemProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfidTAG() {
        return this.rfidTAG;
    }

    public OrderItemProduct rfidTAG(String rfidTAG) {
        this.setRfidTAG(rfidTAG);
        return this;
    }

    public void setRfidTAG(String rfidTAG) {
        this.rfidTAG = rfidTAG;
    }

    public OrderContainer getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderContainer orderContainer) {
        this.orderItem = orderContainer;
    }

    public OrderItemProduct orderItem(OrderContainer orderContainer) {
        this.setOrderItem(orderContainer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemProduct)) {
            return false;
        }
        return id != null && id.equals(((OrderItemProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemProduct{" +
            "id=" + getId() +
            ", rfidTAG='" + getRfidTAG() + "'" +
            "}";
    }
}
