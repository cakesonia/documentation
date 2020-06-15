package com.prychko.lab3.service;

import com.prychko.lab3.domain.InterviewResult;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link InterviewResult}.
 */
public interface InterviewResultService {

    /**
     * Save a interviewResult.
     *
     * @param interviewResult the entity to save.
     * @return the persisted entity.
     */
    InterviewResult save(InterviewResult interviewResult);

    /**
     * Get all the interviewResults.
     *
     * @return the list of entities.
     */
    List<InterviewResult> findAll();
    /**
     * Get all the InterviewResultDTO where Interview is {@code null}.
     *
     * @return the list of entities.
     */
    List<InterviewResult> findAllWhereInterviewIsNull();

    /**
     * Get the "id" interviewResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InterviewResult> findOne(Long id);

    /**
     * Delete the "id" interviewResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
