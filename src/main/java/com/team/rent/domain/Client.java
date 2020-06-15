package com.team.rent.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "full_name", length = 100, nullable = false, unique = true)
    private String fullName;

    @Pattern(regexp = "[0-9]{10}")
    @Column(name = "phone")
    private String phone;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @OneToMany(mappedBy = "client")
    private Set<Request> requests = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Rent> rents = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Fine> fines = new HashSet<>();

    @ManyToMany(mappedBy = "clients")
    @JsonIgnore
    private Set<RentalPoint> rentalPoints = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Client fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public Client phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Client address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public Client requests(Set<Request> requests) {
        this.requests = requests;
        return this;
    }

    public Client addRequests(Request request) {
        this.requests.add(request);
        request.setClient(this);
        return this;
    }

    public Client removeRequests(Request request) {
        this.requests.remove(request);
        request.setClient(null);
        return this;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<Rent> getRents() {
        return rents;
    }

    public Client rents(Set<Rent> rents) {
        this.rents = rents;
        return this;
    }

    public Client addRents(Rent rent) {
        this.rents.add(rent);
        rent.setClient(this);
        return this;
    }

    public Client removeRents(Rent rent) {
        this.rents.remove(rent);
        rent.setClient(null);
        return this;
    }

    public void setRents(Set<Rent> rents) {
        this.rents = rents;
    }

    public Set<Fine> getFines() {
        return fines;
    }

    public Client fines(Set<Fine> fines) {
        this.fines = fines;
        return this;
    }

    public Client addFines(Fine fine) {
        this.fines.add(fine);
        fine.setClient(this);
        return this;
    }

    public Client removeFines(Fine fine) {
        this.fines.remove(fine);
        fine.setClient(null);
        return this;
    }

    public void setFines(Set<Fine> fines) {
        this.fines = fines;
    }

    public Set<RentalPoint> getRentalPoints() {
        return rentalPoints;
    }

    public Client rentalPoints(Set<RentalPoint> rentalPoints) {
        this.rentalPoints = rentalPoints;
        return this;
    }

    public Client addRentalPoints(RentalPoint rentalPoint) {
        this.rentalPoints.add(rentalPoint);
        rentalPoint.getClients().add(this);
        return this;
    }

    public Client removeRentalPoints(RentalPoint rentalPoint) {
        this.rentalPoints.remove(rentalPoint);
        rentalPoint.getClients().remove(this);
        return this;
    }

    public void setRentalPoints(Set<RentalPoint> rentalPoints) {
        this.rentalPoints = rentalPoints;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
