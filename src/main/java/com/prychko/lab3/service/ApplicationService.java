package com.prychko.lab3.service;

import com.prychko.lab3.domain.Application;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Application}.
 */
public interface ApplicationService {

    /**
     * Save a application.
     *
     * @param application the entity to save.
     * @return the persisted entity.
     */
    Application save(Application application);

    /**
     * Get all the applications.
     *
     * @return the list of entities.
     */
    List<Application> findAll();

    /**
     * Get the "id" application.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Application> findOne(Long id);

    /**
     * Delete the "id" application.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
