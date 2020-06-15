package com.prychko.lab3.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Interviewer.
 */
@Entity
@Table(name = "interviewer")
public class Interviewer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "full_name", length = 100, nullable = false, unique = true)
    private String fullName;

    @Size(max = 100)
    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Size(max = 100)
    @Column(name = "position", length = 100)
    private String position;

    @OneToMany(mappedBy = "interviewer")
    private Set<Interview> interviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Interviewer fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public Interviewer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public Interviewer position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<Interview> getInterviews() {
        return interviews;
    }

    public Interviewer interviews(Set<Interview> interviews) {
        this.interviews = interviews;
        return this;
    }

    public Interviewer addInterviews(Interview interview) {
        this.interviews.add(interview);
        interview.setInterviewer(this);
        return this;
    }

    public Interviewer removeInterviews(Interview interview) {
        this.interviews.remove(interview);
        interview.setInterviewer(null);
        return this;
    }

    public void setInterviews(Set<Interview> interviews) {
        this.interviews = interviews;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interviewer)) {
            return false;
        }
        return id != null && id.equals(((Interviewer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Interviewer{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", email='" + getEmail() + "'" +
            ", position='" + getPosition() + "'" +
            "}";
    }
}
