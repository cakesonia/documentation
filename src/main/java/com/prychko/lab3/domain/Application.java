package com.prychko.lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_date")
    private ZonedDateTime applicationDate;

    @OneToMany(mappedBy = "application")
    private Set<ApplicationStatus> statuses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("applications")
    private Vacancy vacancy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getApplicationDate() {
        return applicationDate;
    }

    public Application applicationDate(ZonedDateTime applicationDate) {
        this.applicationDate = applicationDate;
        return this;
    }

    public void setApplicationDate(ZonedDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Set<ApplicationStatus> getStatuses() {
        return statuses;
    }

    public Application statuses(Set<ApplicationStatus> applicationStatuses) {
        this.statuses = applicationStatuses;
        return this;
    }

    public Application addStatus(ApplicationStatus applicationStatus) {
        this.statuses.add(applicationStatus);
        applicationStatus.setApplication(this);
        return this;
    }

    public Application removeStatus(ApplicationStatus applicationStatus) {
        this.statuses.remove(applicationStatus);
        applicationStatus.setApplication(null);
        return this;
    }

    public void setStatuses(Set<ApplicationStatus> applicationStatuses) {
        this.statuses = applicationStatuses;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public Application vacancy(Vacancy vacancy) {
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
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", applicationDate='" + getApplicationDate() + "'" +
            "}";
    }
}
