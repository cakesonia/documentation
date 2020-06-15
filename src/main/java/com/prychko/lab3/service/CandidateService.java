package com.prychko.lab3.service;

import com.prychko.lab3.domain.Candidate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Candidate}.
 */
public interface CandidateService {

    /**
     * Save a candidate.
     *
     * @param candidate the entity to save.
     * @return the persisted entity.
     */
    Candidate save(Candidate candidate);

    /**
     * Get all the candidates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Candidate> findAll(Pageable pageable);

    /**
     * Get all the candidates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Candidate> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" candidate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Candidate> findOne(Long id);

    /**
     * Delete the "id" candidate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
