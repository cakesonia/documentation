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
 * Criteria class for the {@link com.prychko.lab3.domain.Candidate} entity. This class is used
 * in {@link com.prychko.lab3.web.rest.CandidateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /candidates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CandidateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter email;

    private StringFilter cvUrl;

    private LongFilter interviewsId;

    private LongFilter vacanciesId;

    public CandidateCriteria() {
    }

    public CandidateCriteria(CandidateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.cvUrl = other.cvUrl == null ? null : other.cvUrl.copy();
        this.interviewsId = other.interviewsId == null ? null : other.interviewsId.copy();
        this.vacanciesId = other.vacanciesId == null ? null : other.vacanciesId.copy();
    }

    @Override
    public CandidateCriteria copy() {
        return new CandidateCriteria(this);
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

    public StringFilter getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(StringFilter cvUrl) {
        this.cvUrl = cvUrl;
    }

    public LongFilter getInterviewsId() {
        return interviewsId;
    }

    public void setInterviewsId(LongFilter interviewsId) {
        this.interviewsId = interviewsId;
    }

    public LongFilter getVacanciesId() {
        return vacanciesId;
    }

    public void setVacanciesId(LongFilter vacanciesId) {
        this.vacanciesId = vacanciesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CandidateCriteria that = (CandidateCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(cvUrl, that.cvUrl) &&
            Objects.equals(interviewsId, that.interviewsId) &&
            Objects.equals(vacanciesId, that.vacanciesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fullName,
        email,
        cvUrl,
        interviewsId,
        vacanciesId
        );
    }

    @Override
    public String toString() {
        return "CandidateCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (cvUrl != null ? "cvUrl=" + cvUrl + ", " : "") +
                (interviewsId != null ? "interviewsId=" + interviewsId + ", " : "") +
                (vacanciesId != null ? "vacanciesId=" + vacanciesId + ", " : "") +
            "}";
    }

}
