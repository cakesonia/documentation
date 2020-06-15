package com.team.rent.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A CarType.
 */
@Entity
@Table(name = "car_type")
public class CarType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "type")
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

    public CarType status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public CarType cars(Set<Car> cars) {
        this.cars = cars;
        return this;
    }

    public CarType addCars(Car car) {
        this.cars.add(car);
        car.setType(this);
        return this;
    }

    public CarType removeCars(Car car) {
        this.cars.remove(car);
        car.setType(null);
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
        if (!(o instanceof CarType)) {
            return false;
        }
        return id != null && id.equals(((CarType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CarType{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
