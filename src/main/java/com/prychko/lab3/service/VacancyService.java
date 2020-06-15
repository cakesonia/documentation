package com.prychko.lab3.service;

import com.prychko.lab3.domain.Vacancy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Vacancy}.
 */
public interface VacancyService {

    /**
     * Save a vacancy.
     *
     * @param vacancy the entity to save.
     * @return the persisted entity.
     */
    Vacancy save(Vacancy vacancy);

    /**
     * Get all the vacancies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Vacancy> findAll(Pageable pageable);

    /**
     * Get the "id" vacancy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vacancy> findOne(Long id);

    /**
     * Delete the "id" vacancy.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
