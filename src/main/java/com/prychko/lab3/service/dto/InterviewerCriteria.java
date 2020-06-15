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

/**
 * Criteria class for the {@link com.prychko.lab3.domain.Interviewer} entity. This class is used
 * in {@link com.prychko.lab3.web.rest.InterviewerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interviewers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InterviewerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter email;

    private StringFilter position;

    private LongFilter interviewsId;

    public InterviewerCriteria() {
    }

    public InterviewerCriteria(InterviewerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.interviewsId = other.interviewsId == null ? null : other.interviewsId.copy();
    }

    @Override
    public InterviewerCriteria copy() {
        return new InterviewerCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPosition() {
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public LongFilter getInterviewsId() {
        return interviewsId;
    }

    public void setInterviewsId(LongFilter interviewsId) {
        this.interviewsId = interviewsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterviewerCriteria that = (InterviewerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(position, that.position) &&
            Objects.equals(interviewsId, that.interviewsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fullName,
        email,
        position,
        interviewsId
        );
    }

    @Override
    public String toString() {
        return "InterviewerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (position != null ? "position=" + position + ", " : "") +
                (interviewsId != null ? "interviewsId=" + interviewsId + ", " : "") +
            "}";
    }

}
