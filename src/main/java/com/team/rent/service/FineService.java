package com.team.rent.service;

import com.team.rent.domain.Fine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Fine}.
 */
public interface FineService {

    /**
     * Save a fine.
     *
     * @param fine the entity to save.
     * @return the persisted entity.
     */
    Fine save(Fine fine);

    /**
     * Get all the fines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Fine> findAll(Pageable pageable);

    /**
     * Get the "id" fine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Fine> findOne(Long id);

    /**
     * Delete the "id" fine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
