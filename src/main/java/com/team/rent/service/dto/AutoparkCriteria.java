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

/**
 * Criteria class for the {@link com.team.rent.domain.Autopark} entity. This class is used
 * in {@link com.team.rent.web.rest.AutoparkResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /autoparks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AutoparkCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter location;

    private IntegerFilter availableCars;

    private LongFilter carsId;

    private LongFilter rentalPointId;

    public AutoparkCriteria() {
    }

    public AutoparkCriteria(AutoparkCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.availableCars = other.availableCars == null ? null : other.availableCars.copy();
        this.carsId = other.carsId == null ? null : other.carsId.copy();
        this.rentalPointId = other.rentalPointId == null ? null : other.rentalPointId.copy();
    }

    @Override
    public AutoparkCriteria copy() {
        return new AutoparkCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public IntegerFilter getAvailableCars() {
        return availableCars;
    }

    public void setAvailableCars(IntegerFilter availableCars) {
        this.availableCars = availableCars;
    }

    public LongFilter getCarsId() {
        return carsId;
    }

    public void setCarsId(LongFilter carsId) {
        this.carsId = carsId;
    }

    public LongFilter getRentalPointId() {
        return rentalPointId;
    }

    public void setRentalPointId(LongFilter rentalPointId) {
        this.rentalPointId = rentalPointId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AutoparkCriteria that = (AutoparkCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(location, that.location) &&
            Objects.equals(availableCars, that.availableCars) &&
            Objects.equals(carsId, that.carsId) &&
            Objects.equals(rentalPointId, that.rentalPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        location,
        availableCars,
        carsId,
        rentalPointId
        );
    }

    @Override
    public String toString() {
        return "AutoparkCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (availableCars != null ? "availableCars=" + availableCars + ", " : "") +
                (carsId != null ? "carsId=" + carsId + ", " : "") +
                (rentalPointId != null ? "rentalPointId=" + rentalPointId + ", " : "") +
            "}";
    }

}
