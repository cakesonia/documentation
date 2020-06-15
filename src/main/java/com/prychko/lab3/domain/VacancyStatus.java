package com.prychko.lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A VacancyStatus.
 */
@Entity
@Table(name = "vacancy_status")
public class VacancyStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "status", length = 100, nullable = false, unique = true)
    private String status;

    @ManyToOne
    @JsonIgnoreProperties("statuses")
    private Vacancy vacancy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public VacancyStatus status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public VacancyStatus vacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
        return this;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VacancyStatus)) {
            return false;
        }
        return id != null && id.equals(((VacancyStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "VacancyStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
