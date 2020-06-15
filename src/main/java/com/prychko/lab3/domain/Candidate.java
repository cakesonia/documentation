package com.prychko.lab3.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Candidate.
 */
@Entity
@Table(name = "candidate")
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "full_name", length = 100, nullable = false, unique = true)
    private String fullName;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Size(max = 100)
    @Column(name = "cv_url", length = 100)
    private String cvUrl;

    @OneToMany(mappedBy = "candidate")
    private Set<Interview> interviews = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "candidate_vacancies",
               joinColumns = @JoinColumn(name = "candidate_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "vacancies_id", referencedColumnName = "id"))
    private Set<Vacancy> vacancies = new HashSet<>();

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

    public Candidate fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public Candidate email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public Candidate cvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
        return this;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public Set<Interview> getInterviews() {
        return interviews;
    }

    public Candidate interviews(Set<Interview> interviews) {
        this.interviews = interviews;
        return this;
    }

    public Candidate addInterviews(Interview interview) {
        this.interviews.add(interview);
        interview.setCandidate(this);
        return this;
    }

    public Candidate removeInterviews(Interview interview) {
        this.interviews.remove(interview);
        interview.setCandidate(null);
        return this;
    }

    public void setInterviews(Set<Interview> interviews) {
        this.interviews = interviews;
    }

    public Set<Vacancy> getVacancies() {
        return vacancies;
    }

    public Candidate vacancies(Set<Vacancy> vacancies) {
        this.vacancies = vacancies;
        return this;
    }

    public Candidate addVacancies(Vacancy vacancy) {
        this.vacancies.add(vacancy);
        vacancy.getCandidates().add(this);
        return this;
    }

    public Candidate removeVacancies(Vacancy vacancy) {
        this.vacancies.remove(vacancy);
        vacancy.getCandidates().remove(this);
        return this;
    }

    public void setVacancies(Set<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidate)) {
            return false;
        }
        return id != null && id.equals(((Candidate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Candidate{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", email='" + getEmail() + "'" +
            ", cvUrl='" + getCvUrl() + "'" +
            "}";
    }
}
