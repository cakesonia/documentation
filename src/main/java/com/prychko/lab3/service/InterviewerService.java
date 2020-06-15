package com.prychko.lab3.service;

import com.prychko.lab3.domain.Interviewer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Interviewer}.
 */
public interface InterviewerService {

    /**
     * Save a interviewer.
     *
     * @param interviewer the entity to save.
     * @return the persisted entity.
     */
    Interviewer save(Interviewer interviewer);

    /**
     * Get all the interviewers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Interviewer> findAll(Pageable pageable);

    /**
     * Get the "id" interviewer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Interviewer> findOne(Long id);

    /**
     * Delete the "id" interviewer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
