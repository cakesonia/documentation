package com.team.rent.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Autopark.
 */
@Entity
@Table(name = "autopark")
public class Autopark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "available_cars")
    private Integer availableCars;

    @OneToMany(mappedBy = "autopark")
    private Set<Car> cars = new HashSet<>();

    @OneToOne(mappedBy = "autopark")
    @JsonIgnore
    private RentalPoint rentalPoint;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public Autopark location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAvailableCars() {
        return availableCars;
    }

    public Autopark availableCars(Integer availableCars) {
        this.availableCars = availableCars;
        return this;
    }

    public void setAvailableCars(Integer availableCars) {
        this.availableCars = availableCars;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public Autopark cars(Set<Car> cars) {
        this.cars = cars;
        return this;
    }

    public Autopark addCars(Car car) {
        this.cars.add(car);
        car.setAutopark(this);
        return this;
    }

    public Autopark removeCars(Car car) {
        this.cars.remove(car);
        car.setAutopark(null);
        return this;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public RentalPoint getRentalPoint() {
        return rentalPoint;
    }

    public Autopark rentalPoint(RentalPoint rentalPoint) {
        this.rentalPoint = rentalPoint;
        return this;
    }

    public void setRentalPoint(RentalPoint rentalPoint) {
        this.rentalPoint = rentalPoint;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autopark)) {
            return false;
        }
        return id != null && id.equals(((Autopark) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Autopark{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", availableCars=" + getAvailableCars() +
            "}";
    }
}
