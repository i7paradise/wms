package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "user", "company" }, allowSetters = true)
    private Set<CompanyUser> companyUsers = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "containerCategory", "company" }, allowSetters = true)
    private Set<CompanyContainer> companyContainers = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "orderItems" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "uhfRFIDAntennas" }, allowSetters = true)
    private Set<UHFRFIDReader> uhfRFIDReaders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Company address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public Company phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return this.description;
    }

    public Company description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CompanyUser> getCompanyUsers() {
        return this.companyUsers;
    }

    public void setCompanyUsers(Set<CompanyUser> companyUsers) {
        if (this.companyUsers != null) {
            this.companyUsers.forEach(i -> i.setCompany(null));
        }
        if (companyUsers != null) {
            companyUsers.forEach(i -> i.setCompany(this));
        }
        this.companyUsers = companyUsers;
    }

    public Company companyUsers(Set<CompanyUser> companyUsers) {
        this.setCompanyUsers(companyUsers);
        return this;
    }

    public Company addCompanyUser(CompanyUser companyUser) {
        this.companyUsers.add(companyUser);
        companyUser.setCompany(this);
        return this;
    }

    public Company removeCompanyUser(CompanyUser companyUser) {
        this.companyUsers.remove(companyUser);
        companyUser.setCompany(null);
        return this;
    }

    public Set<CompanyContainer> getCompanyContainers() {
        return this.companyContainers;
    }

    public void setCompanyContainers(Set<CompanyContainer> companyContainers) {
        if (this.companyContainers != null) {
            this.companyContainers.forEach(i -> i.setCompany(null));
        }
        if (companyContainers != null) {
            companyContainers.forEach(i -> i.setCompany(this));
        }
        this.companyContainers = companyContainers;
    }

    public Company companyContainers(Set<CompanyContainer> companyContainers) {
        this.setCompanyContainers(companyContainers);
        return this;
    }

    public Company addCompanyContainer(CompanyContainer companyContainer) {
        this.companyContainers.add(companyContainer);
        companyContainer.setCompany(this);
        return this;
    }

    public Company removeCompanyContainer(CompanyContainer companyContainer) {
        this.companyContainers.remove(companyContainer);
        companyContainer.setCompany(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setCompany(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setCompany(this));
        }
        this.orders = orders;
    }

    public Company orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Company addOrder(Order order) {
        this.orders.add(order);
        order.setCompany(this);
        return this;
    }

    public Company removeOrder(Order order) {
        this.orders.remove(order);
        order.setCompany(null);
        return this;
    }

    public Set<UHFRFIDReader> getUhfRFIDReaders() {
        return this.uhfRFIDReaders;
    }

    public void setUhfRFIDReaders(Set<UHFRFIDReader> uHFRFIDReaders) {
        if (this.uhfRFIDReaders != null) {
            this.uhfRFIDReaders.forEach(i -> i.setCompany(null));
        }
        if (uHFRFIDReaders != null) {
            uHFRFIDReaders.forEach(i -> i.setCompany(this));
        }
        this.uhfRFIDReaders = uHFRFIDReaders;
    }

    public Company uhfRFIDReaders(Set<UHFRFIDReader> uHFRFIDReaders) {
        this.setUhfRFIDReaders(uHFRFIDReaders);
        return this;
    }

    public Company addUhfRFIDReader(UHFRFIDReader uHFRFIDReader) {
        this.uhfRFIDReaders.add(uHFRFIDReader);
        uHFRFIDReader.setCompany(this);
        return this;
    }

    public Company removeUhfRFIDReader(UHFRFIDReader uHFRFIDReader) {
        this.uhfRFIDReaders.remove(uHFRFIDReader);
        uHFRFIDReader.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
