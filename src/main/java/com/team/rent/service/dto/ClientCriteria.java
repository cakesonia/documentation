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
 * Criteria class for the {@link com.team.rent.domain.Client} entity. This class is used
 * in {@link com.team.rent.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter phone;

    private StringFilter address;

    private LongFilter requestsId;

    private LongFilter rentsId;

    private LongFilter finesId;

    private LongFilter rentalPointsId;

    public ClientCriteria() {
    }

    public ClientCriteria(ClientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.requestsId = other.requestsId == null ? null : other.requestsId.copy();
        this.rentsId = other.rentsId == null ? null : other.rentsId.copy();
        this.finesId = other.finesId == null ? null : other.finesId.copy();
        this.rentalPointsId = other.rentalPointsId == null ? null : other.rentalPointsId.copy();
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getRequestsId() {
        return requestsId;
    }

    public void setRequestsId(LongFilter requestsId) {
        this.requestsId = requestsId;
    }

    public LongFilter getRentsId() {
        return rentsId;
    }

    public void setRentsId(LongFilter rentsId) {
        this.rentsId = rentsId;
    }

    public LongFilter getFinesId() {
        return finesId;
    }

    public void setFinesId(LongFilter finesId) {
        this.finesId = finesId;
    }

    public LongFilter getRentalPointsId() {
        return rentalPointsId;
    }

    public void setRentalPointsId(LongFilter rentalPointsId) {
        this.rentalPointsId = rentalPointsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClientCriteria that = (ClientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(address, that.address) &&
            Objects.equals(requestsId, that.requestsId) &&
            Objects.equals(rentsId, that.rentsId) &&
            Objects.equals(finesId, that.finesId) &&
            Objects.equals(rentalPointsId, that.rentalPointsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fullName,
        phone,
        address,
        requestsId,
        rentsId,
        finesId,
        rentalPointsId
        );
    }

    @Override
    public String toString() {
        return "ClientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (requestsId != null ? "requestsId=" + requestsId + ", " : "") +
                (rentsId != null ? "rentsId=" + rentsId + ", " : "") +
                (finesId != null ? "finesId=" + finesId + ", " : "") +
                (rentalPointsId != null ? "rentalPointsId=" + rentalPointsId + ", " : "") +
            "}";
    }

}
