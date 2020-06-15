package com.team.rent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

/**
 * A Request.
 */
@Entity
@Table(name = "request")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_date")
    private ZonedDateTime registrationDate;

    @Column(name = "delivery_date")
    private ZonedDateTime deliveryDate;

    @Column(name = "request_status")
    private String requestStatus;

    @OneToOne
    @JoinColumn(unique = true)
    private Rent rent;

    @OneToOne
    @JoinColumn(unique = true)
    private Car car;

    @ManyToOne
    @JsonIgnoreProperties("requests")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRegistrationDate() {
        return registrationDate;
    }

    public Request registrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public void setRegistrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public Request deliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public Request requestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Rent getRent() {
        return rent;
    }

    public Request rent(Rent rent) {
        this.rent = rent;
        return this;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public Car getCar() {
        return car;
    }

    public Request car(Car car) {
        this.car = car;
        return this;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Client getClient() {
        return client;
    }

    public Request client(Client client) {
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
        if (!(o instanceof Request)) {
            return false;
        }
        return id != null && id.equals(((Request) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Request{" +
            "id=" + getId() +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", requestStatus='" + getRequestStatus() + "'" +
            "}";
    }
}
