package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wms.uhfrfid.domain.enumeration.UHFRFIDReaderStatus;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A UHFRFIDReader.
 */
@Entity
@Table(name = "uhfrfid_reader")
public class UHFRFIDReader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "ip", nullable = false)
    private String ip;

    @NotNull
    @Column(name = "port", nullable = false)
    private Integer port;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UHFRFIDReaderStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "companyUsers", "companyContainers", "orders", "uhfRFIDReaders" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UHFRFIDReader id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UHFRFIDReader name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return this.ip;
    }

    public UHFRFIDReader ip(String ip) {
        this.setIp(ip);
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return this.port;
    }

    public UHFRFIDReader port(Integer port) {
        this.setPort(port);
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public UHFRFIDReaderStatus getStatus() {
        return this.status;
    }

    public UHFRFIDReader status(UHFRFIDReaderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(UHFRFIDReaderStatus status) {
        this.status = status;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public UHFRFIDReader company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UHFRFIDReader)) {
            return false;
        }
        return id != null && id.equals(((UHFRFIDReader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UHFRFIDReader{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ip='" + getIp() + "'" +
            ", port=" + getPort() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
