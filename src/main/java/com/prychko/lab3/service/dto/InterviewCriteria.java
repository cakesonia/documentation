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
 * Criteria class for the {@link com.prychko.lab3.domain.Interview} entity. This class is used
 * in {@link com.prychko.lab3.web.rest.InterviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InterviewCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter date;

    private LongFilter resultId;

    private LongFilter typeId;

    private LongFilter candidateId;

    private LongFilter interviewerId;

    public InterviewCriteria() {
    }

    public InterviewCriteria(InterviewCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.resultId = other.resultId == null ? null : other.resultId.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.candidateId = other.candidateId == null ? null : other.candidateId.copy();
        this.interviewerId = other.interviewerId == null ? null : other.interviewerId.copy();
    }

    @Override
    public InterviewCriteria copy() {
        return new InterviewCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public LongFilter getResultId() {
        return resultId;
    }

    public void setResultId(LongFilter resultId) {
        this.resultId = resultId;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(LongFilter candidateId) {
        this.candidateId = candidateId;
    }

    public LongFilter getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(LongFilter interviewerId) {
        this.interviewerId = interviewerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterviewCriteria that = (InterviewCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(resultId, that.resultId) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(candidateId, that.candidateId) &&
            Objects.equals(interviewerId, that.interviewerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        resultId,
        typeId,
        candidateId,
        interviewerId
        );
    }

    @Override
    public String toString() {
        return "InterviewCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (resultId != null ? "resultId=" + resultId + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
                (candidateId != null ? "candidateId=" + candidateId + ", " : "") +
                (interviewerId != null ? "interviewerId=" + interviewerId + ", " : "") +
            "}";
    }

}
