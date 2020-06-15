package com.team.rent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Fine.
 */
@Entity
@Table(name = "fine")
public class Fine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fine_size")
    private Integer fineSize;

    @Column(name = "fine_reason")
    private String fineReason;

    @ManyToOne
    @JsonIgnoreProperties("fines")
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties("fines")
    private Rent rent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFineSize() {
        return fineSize;
    }

    public Fine fineSize(Integer fineSize) {
        this.fineSize = fineSize;
        return this;
    }

    public void setFineSize(Integer fineSize) {
        this.fineSize = fineSize;
    }

    public String getFineReason() {
        return fineReason;
    }

    public Fine fineReason(String fineReason) {
        this.fineReason = fineReason;
        return this;
    }

    public void setFineReason(String fineReason) {
        this.fineReason = fineReason;
    }

    public Client getClient() {
        return client;
    }

    public Fine client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Rent getRent() {
        return rent;
    }

    public Fine rent(Rent rent) {
        this.rent = rent;
        return this;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fine)) {
            return false;
        }
        return id != null && id.equals(((Fine) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Fine{" +
            "id=" + getId() +
            ", fineSize=" + getFineSize() +
            ", fineReason='" + getFineReason() + "'" +
            "}";
    }
}
