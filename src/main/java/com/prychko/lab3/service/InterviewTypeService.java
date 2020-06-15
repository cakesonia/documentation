package com.prychko.lab3.service;

import com.prychko.lab3.domain.InterviewType;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link InterviewType}.
 */
public interface InterviewTypeService {

    /**
     * Save a interviewType.
     *
     * @param interviewType the entity to save.
     * @return the persisted entity.
     */
    InterviewType save(InterviewType interviewType);

    /**
     * Get all the interviewTypes.
     *
     * @return the list of entities.
     */
    List<InterviewType> findAll();

    /**
     * Get the "id" interviewType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InterviewType> findOne(Long id);

    /**
     * Delete the "id" interviewType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
