package com.prychko.lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ApplicationStatus.
 */
@Entity
@Table(name = "application_status")
public class ApplicationStatus implements Serializable {

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
    private Application application;

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

    public ApplicationStatus status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Application getApplication() {
        return application;
    }

    public ApplicationStatus application(Application application) {
        this.application = application;
        return this;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationStatus)) {
            return false;
        }
        return id != null && id.equals(((ApplicationStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ApplicationStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
