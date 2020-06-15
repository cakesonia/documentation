package com.prychko.lab3.service.dto;

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
 * Criteria class for the {@link com.prychko.lab3.domain.Vacancy} entity. This class is used
 * in {@link com.prychko.lab3.web.rest.VacancyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vacancies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VacancyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private IntegerFilter salary;

    private ZonedDateTimeFilter createdDate;

    private LongFilter statusId;

    private LongFilter applicationsId;

    private LongFilter candidatesId;

    public VacancyCriteria() {
    }

    public VacancyCriteria(VacancyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.salary = other.salary == null ? null : other.salary.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.applicationsId = other.applicationsId == null ? null : other.applicationsId.copy();
        this.candidatesId = other.candidatesId == null ? null : other.candidatesId.copy();
    }

    @Override
    public VacancyCriteria copy() {
        return new VacancyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getSalary() {
        return salary;
    }

    public void setSalary(IntegerFilter salary) {
        this.salary = salary;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getApplicationsId() {
        return applicationsId;
    }

    public void setApplicationsId(LongFilter applicationsId) {
        this.applicationsId = applicationsId;
    }

    public LongFilter getCandidatesId() {
        return candidatesId;
    }

    public void setCandidatesId(LongFilter candidatesId) {
        this.candidatesId = candidatesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VacancyCriteria that = (VacancyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(applicationsId, that.applicationsId) &&
            Objects.equals(candidatesId, that.candidatesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        salary,
        createdDate,
        statusId,
        applicationsId,
        candidatesId
        );
    }

    @Override
    public String toString() {
        return "VacancyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (salary != null ? "salary=" + salary + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (statusId != null ? "statusId=" + statusId + ", " : "") +
                (applicationsId != null ? "applicationsId=" + applicationsId + ", " : "") +
                (candidatesId != null ? "candidatesId=" + candidatesId + ", " : "") +
            "}";
    }

}
