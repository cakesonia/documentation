package com.prychko.lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Interview.
 */
@Entity
@Table(name = "interview")
public class Interview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @OneToOne
    @JoinColumn(unique = true)
    private InterviewResult result;

    @OneToMany(mappedBy = "interview")
    private Set<InterviewType> types = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("interviews")
    private Candidate candidate;

    @ManyToOne
    @JsonIgnoreProperties("interviews")
    private Interviewer interviewer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Interview date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public InterviewResult getResult() {
        return result;
    }

    public Interview result(InterviewResult interviewResult) {
        this.result = interviewResult;
        return this;
    }

    public void setResult(InterviewResult interviewResult) {
        this.result = interviewResult;
    }

    public Set<InterviewType> getTypes() {
        return types;
    }

    public Interview types(Set<InterviewType> interviewTypes) {
        this.types = interviewTypes;
        return this;
    }

    public Interview addType(InterviewType interviewType) {
        this.types.add(interviewType);
        interviewType.setInterview(this);
        return this;
    }

    public Interview removeType(InterviewType interviewType) {
        this.types.remove(interviewType);
        interviewType.setInterview(null);
        return this;
    }

    public void setTypes(Set<InterviewType> interviewTypes) {
        this.types = interviewTypes;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public Interview candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Interviewer getInterviewer() {
        return interviewer;
    }

    public Interview interviewer(Interviewer interviewer) {
        this.interviewer = interviewer;
        return this;
    }

    public void setInterviewer(Interviewer interviewer) {
        this.interviewer = interviewer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interview)) {
            return false;
        }
        return id != null && id.equals(((Interview) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Interview{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
