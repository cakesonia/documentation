package com.team.rent.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Rent.
 */
@Entity
@Table(name = "rent")
public class Rent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lending_date")
    private ZonedDateTime lendingDate;

    @Column(name = "returning_date")
    private ZonedDateTime returningDate;

    @Column(name = "rent_status")
    private String rentStatus;

    @OneToMany(mappedBy = "rent")
    private Set<Fine> fines = new HashSet<>();

    @OneToOne(mappedBy = "rent")
    @JsonIgnore
    private Request request;

    @ManyToOne
    @JsonIgnoreProperties("rents")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLendingDate() {
        return lendingDate;
    }

    public Rent lendingDate(ZonedDateTime lendingDate) {
        this.lendingDate = lendingDate;
        return this;
    }

    public void setLendingDate(ZonedDateTime lendingDate) {
        this.lendingDate = lendingDate;
    }

    public ZonedDateTime getReturningDate() {
        return returningDate;
    }

    public Rent returningDate(ZonedDateTime returningDate) {
        this.returningDate = returningDate;
        return this;
    }

    public void setReturningDate(ZonedDateTime returningDate) {
        this.returningDate = returningDate;
    }

    public String getRentStatus() {
        return rentStatus;
    }

    public Rent rentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
        return this;
    }

    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }

    public Set<Fine> getFines() {
        return fines;
    }

    public Rent fines(Set<Fine> fines) {
        this.fines = fines;
        return this;
    }

    public Rent addFines(Fine fine) {
        this.fines.add(fine);
        fine.setRent(this);
        return this;
    }

    public Rent removeFines(Fine fine) {
        this.fines.remove(fine);
        fine.setRent(null);
        return this;
    }

    public void setFines(Set<Fine> fines) {
        this.fines = fines;
    }

    public Request getRequest() {
        return request;
    }

    public Rent request(Request request) {
        this.request = request;
        return this;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Client getClient() {
        return client;
    }

    public Rent client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rent)) {
            return false;
        }
        return id != null && id.equals(((Rent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Rent{" +
            "id=" + getId() +
            ", lendingDate='" + getLendingDate() + "'" +
            ", returningDate='" + getReturningDate() + "'" +
            ", rentStatus='" + getRentStatus() + "'" +
            "}";
    }
}
