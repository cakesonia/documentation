package com.prychko.lab3.service;

import com.prychko.lab3.domain.ApplicationStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ApplicationStatus}.
 */
public interface ApplicationStatusService {

    /**
     * Save a applicationStatus.
     *
     * @param applicationStatus the entity to save.
     * @return the persisted entity.
     */
    ApplicationStatus save(ApplicationStatus applicationStatus);

    /**
     * Get all the applicationStatuses.
     *
     * @return the list of entities.
     */
    List<ApplicationStatus> findAll();

    /**
     * Get the "id" applicationStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApplicationStatus> findOne(Long id);

    /**
     * Delete the "id" applicationStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
