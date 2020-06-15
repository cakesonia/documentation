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
 * Criteria class for the {@link com.team.rent.domain.RentalPoint} entity. This class is used
 * in {@link com.team.rent.web.rest.RentalPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rental-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RentalPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter location;

    private ZonedDateTimeFilter worktime;

    private LongFilter autoparkId;

    private LongFilter clientsId;

    public RentalPointCriteria() {
    }

    public RentalPointCriteria(RentalPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.worktime = other.worktime == null ? null : other.worktime.copy();
        this.autoparkId = other.autoparkId == null ? null : other.autoparkId.copy();
        this.clientsId = other.clientsId == null ? null : other.clientsId.copy();
    }

    @Override
    public RentalPointCriteria copy() {
        return new RentalPointCriteria(this);
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

    public ZonedDateTimeFilter getWorktime() {
        return worktime;
    }

    public void setWorktime(ZonedDateTimeFilter worktime) {
        this.worktime = worktime;
    }

    public LongFilter getAutoparkId() {
        return autoparkId;
    }

    public void setAutoparkId(LongFilter autoparkId) {
        this.autoparkId = autoparkId;
    }

    public LongFilter getClientsId() {
        return clientsId;
    }

    public void setClientsId(LongFilter clientsId) {
        this.clientsId = clientsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RentalPointCriteria that = (RentalPointCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(location, that.location) &&
            Objects.equals(worktime, that.worktime) &&
            Objects.equals(autoparkId, that.autoparkId) &&
            Objects.equals(clientsId, that.clientsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        location,
        worktime,
        autoparkId,
        clientsId
        );
    }

    @Override
    public String toString() {
        return "RentalPointCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (worktime != null ? "worktime=" + worktime + ", " : "") +
                (autoparkId != null ? "autoparkId=" + autoparkId + ", " : "") +
                (clientsId != null ? "clientsId=" + clientsId + ", " : "") +
            "}";
    }

}
