package com.prychko.lab3.service;

import com.prychko.lab3.domain.VacancyStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link VacancyStatus}.
 */
public interface VacancyStatusService {

    /**
     * Save a vacancyStatus.
     *
     * @param vacancyStatus the entity to save.
     * @return the persisted entity.
     */
    VacancyStatus save(VacancyStatus vacancyStatus);

    /**
     * Get all the vacancyStatuses.
     *
     * @return the list of entities.
     */
    List<VacancyStatus> findAll();

    /**
     * Get the "id" vacancyStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VacancyStatus> findOne(Long id);

    /**
     * Delete the "id" vacancyStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
