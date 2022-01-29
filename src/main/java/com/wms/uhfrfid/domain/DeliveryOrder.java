package com.wms.uhfrfid.domain;

import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeliveryOrder.
 */
@Entity
@Table(name = "delivery_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeliveryOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "do_number", nullable = false, unique = true)
    private String doNumber;

    @NotNull
    @Column(name = "placed_date", nullable = false)
    private Instant placedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryOrderStatus status;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoNumber() {
        return this.doNumber;
    }

    public DeliveryOrder doNumber(String doNumber) {
        this.setDoNumber(doNumber);
        return this;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public Instant getPlacedDate() {
        return this.placedDate;
    }

    public DeliveryOrder placedDate(Instant placedDate) {
        this.setPlacedDate(placedDate);
        return this;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public DeliveryOrderStatus getStatus() {
        return this.status;
    }

    public DeliveryOrder status(DeliveryOrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DeliveryOrderStatus status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public DeliveryOrder code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryOrder)) {
            return false;
        }
        return id != null && id.equals(((DeliveryOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryOrder{" +
            "id=" + getId() +
            ", doNumber='" + getDoNumber() + "'" +
            ", placedDate='" + getPlacedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
