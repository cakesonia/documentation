package com.prychko.lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InterviewResult.
 */
@Entity
@Table(name = "interview_result")
public class InterviewResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @OneToOne(mappedBy = "result")
    @JsonIgnore
    private Interview interview;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public InterviewResult description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Interview getInterview() {
        return interview;
    }

    public InterviewResult interview(Interview interview) {
        this.interview = interview;
        return this;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterviewResult)) {
            return false;
        }
        return id != null && id.equals(((InterviewResult) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InterviewResult{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
