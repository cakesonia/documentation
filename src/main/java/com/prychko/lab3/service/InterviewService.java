package com.prychko.lab3.service;

import com.prychko.lab3.domain.Interview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Interview}.
 */
public interface InterviewService {

    /**
     * Save a interview.
     *
     * @param interview the entity to save.
     * @return the persisted entity.
     */
    Interview save(Interview interview);

    /**
     * Get all the interviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Interview> findAll(Pageable pageable);

    /**
     * Get the "id" interview.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Interview> findOne(Long id);

    /**
     * Delete the "id" interview.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
