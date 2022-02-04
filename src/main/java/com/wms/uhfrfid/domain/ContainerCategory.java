package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContainerCategory.
 */
@Entity
@Table(name = "container_category")
public class ContainerCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compganyProduct", "order", "containerCategories" }, allowSetters = true)
    private OrderItem orderItem;

    @OneToMany(mappedBy = "containerCategory")
    @JsonIgnoreProperties(value = { "containerCategory", "orderItem" }, allowSetters = true)
    private Set<OrderItemProduct> orderItemProducts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContainerCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ContainerCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ContainerCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public ContainerCategory orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Set<OrderItemProduct> getOrderItemProducts() {
        return this.orderItemProducts;
    }

    public void setOrderItemProducts(Set<OrderItemProduct> orderItemProducts) {
        if (this.orderItemProducts != null) {
            this.orderItemProducts.forEach(i -> i.setContainerCategory(null));
        }
        if (orderItemProducts != null) {
            orderItemProducts.forEach(i -> i.setContainerCategory(this));
        }
        this.orderItemProducts = orderItemProducts;
    }

    public ContainerCategory orderItemProducts(Set<OrderItemProduct> orderItemProducts) {
        this.setOrderItemProducts(orderItemProducts);
        return this;
    }

    public ContainerCategory addOrderItemProduct(OrderItemProduct orderItemProduct) {
        this.orderItemProducts.add(orderItemProduct);
        orderItemProduct.setContainerCategory(this);
        return this;
    }

    public ContainerCategory removeOrderItemProduct(OrderItemProduct orderItemProduct) {
        this.orderItemProducts.remove(orderItemProduct);
        orderItemProduct.setContainerCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContainerCategory)) {
            return false;
        }
        return id != null && id.equals(((ContainerCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContainerCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
