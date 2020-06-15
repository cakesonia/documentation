package com.prychko.lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vacancy.
 */
@Entity
@Table(name = "vacancy")
public class Vacancy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false, unique = true)
    private String title;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @OneToMany(mappedBy = "vacancy")
    private Set<VacancyStatus> statuses = new HashSet<>();

    @OneToMany(mappedBy = "vacancy")
    private Set<Application> applications = new HashSet<>();

    @ManyToMany(mappedBy = "vacancies")
    @JsonIgnore
    private Set<Candidate> candidates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Vacancy title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Vacancy description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSalary() {
        return salary;
    }

    public Vacancy salary(Integer salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Vacancy createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Set<VacancyStatus> getStatuses() {
        return statuses;
    }

    public Vacancy statuses(Set<VacancyStatus> vacancyStatuses) {
        this.statuses = vacancyStatuses;
        return this;
    }

    public Vacancy addStatus(VacancyStatus vacancyStatus) {
        this.statuses.add(vacancyStatus);
        vacancyStatus.setVacancy(this);
        return this;
    }

    public Vacancy removeStatus(VacancyStatus vacancyStatus) {
        this.statuses.remove(vacancyStatus);
        vacancyStatus.setVacancy(null);
        return this;
    }

    public void setStatuses(Set<VacancyStatus> vacancyStatuses) {
        this.statuses = vacancyStatuses;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public Vacancy applications(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public Vacancy addApplications(Application application) {
        this.applications.add(application);
        application.setVacancy(this);
        return this;
    }

    public Vacancy removeApplications(Application application) {
        this.applications.remove(application);
        application.setVacancy(null);
        return this;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    public Vacancy candidates(Set<Candidate> candidates) {
        this.candidates = candidates;
        return this;
    }

    public Vacancy addCandidates(Candidate candidate) {
        this.candidates.add(candidate);
        candidate.getVacancies().add(this);
        return this;
    }

    public Vacancy removeCandidates(Candidate candidate) {
        this.candidates.remove(candidate);
        candidate.getVacancies().remove(this);
        return this;
    }

    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vacancy)) {
            return false;
        }
        return id != null && id.equals(((Vacancy) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", salary=" + getSalary() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
