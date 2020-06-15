package com.team.rent.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A CarBrand.
 */
@Entity
@Table(name = "car_brand")
public class CarBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "brand")
    private Set<Car> cars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public CarBrand status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public CarBrand cars(Set<Car> cars) {
        this.cars = cars;
        return this;
    }

    public CarBrand addCars(Car car) {
        this.cars.add(car);
        car.setBrand(this);
        return this;
    }

    public CarBrand removeCars(Car car) {
        this.cars.remove(car);
        car.setBrand(null);
        return this;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarBrand)) {
            return false;
        }
        return id != null && id.equals(((CarBrand) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CarBrand{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
