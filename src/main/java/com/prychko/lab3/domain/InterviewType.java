package com.prychko.lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InterviewType.
 */
@Entity
@Table(name = "interview_type")
public class InterviewType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "type", length = 100, nullable = false)
    private String type;

    @ManyToOne
    @JsonIgnoreProperties("types")
    private Interview interview;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public InterviewType type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Interview getInterview() {
        return interview;
    }

    public InterviewType interview(Interview interview) {
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
        if (!(o instanceof InterviewType)) {
            return false;
        }
        return id != null && id.equals(((InterviewType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InterviewType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
