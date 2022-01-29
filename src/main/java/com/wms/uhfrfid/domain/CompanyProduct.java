package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CompanyProduct.
 */
@Entity
@Table(name = "company_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "sku")
    private String sku;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "stocking_ratio", precision = 21, scale = 2, nullable = false)
    private BigDecimal stockingRatio;

    @OneToOne
    @JoinColumn(unique = true)
    private Container container;

    @ManyToOne
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productCategory" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompanyProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public CompanyProduct quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return this.sku;
    }

    public CompanyProduct sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getStockingRatio() {
        return this.stockingRatio;
    }

    public CompanyProduct stockingRatio(BigDecimal stockingRatio) {
        this.setStockingRatio(stockingRatio);
        return this;
    }

    public void setStockingRatio(BigDecimal stockingRatio) {
        this.stockingRatio = stockingRatio;
    }

    public Container getContainer() {
        return this.container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public CompanyProduct container(Container container) {
        this.setContainer(container);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyProduct company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CompanyProduct product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyProduct)) {
            return false;
        }
        return id != null && id.equals(((CompanyProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyProduct{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", sku='" + getSku() + "'" +
            ", stockingRatio=" + getStockingRatio() +
            "}";
    }
}
