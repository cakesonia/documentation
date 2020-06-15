package com.team.rent.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private Integer price;

    @Column(name = "manufactured_year")
    private ZonedDateTime manufacturedYear;

    @OneToOne(mappedBy = "car")
    @JsonIgnore
    private Request request;

    @ManyToOne
    @JsonIgnoreProperties("cars")
    private CarType type;

    @ManyToOne
    @JsonIgnoreProperties("cars")
    private CarBrand brand;

    @ManyToOne
    @JsonIgnoreProperties("cars")
    private Autopark autopark;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public Car price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ZonedDateTime getManufacturedYear() {
        return manufacturedYear;
    }

    public Car manufacturedYear(ZonedDateTime manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
        return this;
    }

    public void setManufacturedYear(ZonedDateTime manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public Request getRequest() {
        return request;
    }

    public Car request(Request request) {
        this.request = request;
        return this;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public CarType getType() {
        return type;
    }

    public Car type(CarType carType) {
        this.type = carType;
        return this;
    }

    public void setType(CarType carType) {
        this.type = carType;
    }

    public CarBrand getBrand() {
        return brand;
    }

    public Car brand(CarBrand carBrand) {
        this.brand = carBrand;
        return this;
    }

    public void setBrand(CarBrand carBrand) {
        this.brand = carBrand;
    }

    public Autopark getAutopark() {
        return autopark;
    }

    public Car autopark(Autopark autopark) {
        this.autopark = autopark;
        return this;
    }

    public void setAutopark(Autopark autopark) {
        this.autopark = autopark;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", manufacturedYear='" + getManufacturedYear() + "'" +
            "}";
    }
}
