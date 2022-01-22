package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderItemStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeliveryOrderItem.
 */
@Entity
@Table(name = "delivery_order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeliveryOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitQuantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "container_quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal containerQuantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryOrderItemStatus status;

    @JsonIgnoreProperties(value = { "container", "company", "product" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CompanyProduct compganyProduct;

    @ManyToOne
    private DeliveryOrder deliveryOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryOrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnitQuantity() {
        return this.unitQuantity;
    }

    public DeliveryOrderItem unitQuantity(BigDecimal unitQuantity) {
        this.setUnitQuantity(unitQuantity);
        return this;
    }

    public void setUnitQuantity(BigDecimal unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public BigDecimal getContainerQuantity() {
        return this.containerQuantity;
    }

    public DeliveryOrderItem containerQuantity(BigDecimal containerQuantity) {
        this.setContainerQuantity(containerQuantity);
        return this;
    }

    public void setContainerQuantity(BigDecimal containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public DeliveryOrderItemStatus getStatus() {
        return this.status;
    }

    public DeliveryOrderItem status(DeliveryOrderItemStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DeliveryOrderItemStatus status) {
        this.status = status;
    }

    public CompanyProduct getCompganyProduct() {
        return this.compganyProduct;
    }

    public void setCompganyProduct(CompanyProduct companyProduct) {
        this.compganyProduct = companyProduct;
    }

    public DeliveryOrderItem compganyProduct(CompanyProduct companyProduct) {
        this.setCompganyProduct(companyProduct);
        return this;
    }

    public DeliveryOrder getDeliveryOrder() {
        return this.deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public DeliveryOrderItem deliveryOrder(DeliveryOrder deliveryOrder) {
        this.setDeliveryOrder(deliveryOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryOrderItem)) {
            return false;
        }
        return id != null && id.equals(((DeliveryOrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryOrderItem{" +
            "id=" + getId() +
            ", unitQuantity=" + getUnitQuantity() +
            ", containerQuantity=" + getContainerQuantity() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
