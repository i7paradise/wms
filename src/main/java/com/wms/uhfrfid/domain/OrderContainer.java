package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A OrderContainer.
 */
@Entity
@Table(name = "order_container")
public class OrderContainer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "supplier_rfid_tag")
    private String supplierRFIDTag;

    @JsonIgnoreProperties(value = { "containerCategory", "company" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CompanyContainer companyContainer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "order", "companyProduct", "orderContainers" }, allowSetters = true)
    private OrderItem orderItem;

    @OneToMany(mappedBy = "orderContainer")
    @JsonIgnoreProperties(value = { "orderContainer" }, allowSetters = true)
    private Set<OrderItemProduct> orderItemProducts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderContainer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierRFIDTag() {
        return this.supplierRFIDTag;
    }

    public OrderContainer supplierRFIDTag(String supplierRFIDTag) {
        this.setSupplierRFIDTag(supplierRFIDTag);
        return this;
    }

    public void setSupplierRFIDTag(String supplierRFIDTag) {
        this.supplierRFIDTag = supplierRFIDTag;
    }

    public CompanyContainer getCompanyContainer() {
        return this.companyContainer;
    }

    public void setCompanyContainer(CompanyContainer companyContainer) {
        this.companyContainer = companyContainer;
    }

    public OrderContainer companyContainer(CompanyContainer companyContainer) {
        this.setCompanyContainer(companyContainer);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public OrderContainer orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Set<OrderItemProduct> getOrderItemProducts() {
        return this.orderItemProducts;
    }

    public void setOrderItemProducts(Set<OrderItemProduct> orderItemProducts) {
        if (this.orderItemProducts != null) {
            this.orderItemProducts.forEach(i -> i.setOrderContainer(null));
        }
        if (orderItemProducts != null) {
            orderItemProducts.forEach(i -> i.setOrderContainer(this));
        }
        this.orderItemProducts = orderItemProducts;
    }

    public OrderContainer orderItemProducts(Set<OrderItemProduct> orderItemProducts) {
        this.setOrderItemProducts(orderItemProducts);
        return this;
    }

    public OrderContainer addOrderItemProduct(OrderItemProduct orderItemProduct) {
        this.orderItemProducts.add(orderItemProduct);
        orderItemProduct.setOrderContainer(this);
        return this;
    }

    public OrderContainer removeOrderItemProduct(OrderItemProduct orderItemProduct) {
        this.orderItemProducts.remove(orderItemProduct);
        orderItemProduct.setOrderContainer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderContainer)) {
            return false;
        }
        return id != null && id.equals(((OrderContainer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderContainer{" +
            "id=" + getId() +
            ", supplierRFIDTag='" + getSupplierRFIDTag() + "'" +
            "}";
    }
}
