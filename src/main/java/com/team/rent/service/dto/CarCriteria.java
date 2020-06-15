package com.team.rent.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.team.rent.domain.Car} entity. This class is used
 * in {@link com.team.rent.web.rest.CarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter price;

    private ZonedDateTimeFilter manufacturedYear;

    private LongFilter requestId;

    private LongFilter typeId;

    private LongFilter brandId;

    private LongFilter autoparkId;

    public CarCriteria() {
    }

    public CarCriteria(CarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.manufacturedYear = other.manufacturedYear == null ? null : other.manufacturedYear.copy();
        this.requestId = other.requestId == null ? null : other.requestId.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.brandId = other.brandId == null ? null : other.brandId.copy();
        this.autoparkId = other.autoparkId == null ? null : other.autoparkId.copy();
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPrice() {
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public ZonedDateTimeFilter getManufacturedYear() {
        return manufacturedYear;
    }

    public void setManufacturedYear(ZonedDateTimeFilter manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public LongFilter getRequestId() {
        return requestId;
    }

    public void setRequestId(LongFilter requestId) {
        this.requestId = requestId;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getBrandId() {
        return brandId;
    }

    public void setBrandId(LongFilter brandId) {
        this.brandId = brandId;
    }

    public LongFilter getAutoparkId() {
        return autoparkId;
    }

    public void setAutoparkId(LongFilter autoparkId) {
        this.autoparkId = autoparkId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CarCriteria that = (CarCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(manufacturedYear, that.manufacturedYear) &&
            Objects.equals(requestId, that.requestId) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(brandId, that.brandId) &&
            Objects.equals(autoparkId, that.autoparkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        price,
        manufacturedYear,
        requestId,
        typeId,
        brandId,
        autoparkId
        );
    }

    @Override
    public String toString() {
        return "CarCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (manufacturedYear != null ? "manufacturedYear=" + manufacturedYear + ", " : "") +
                (requestId != null ? "requestId=" + requestId + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
                (brandId != null ? "brandId=" + brandId + ", " : "") +
                (autoparkId != null ? "autoparkId=" + autoparkId + ", " : "") +
            "}";
    }

}
