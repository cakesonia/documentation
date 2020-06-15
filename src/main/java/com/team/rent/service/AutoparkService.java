package com.team.rent.service;

import com.team.rent.domain.Autopark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Autopark}.
 */
public interface AutoparkService {

    /**
     * Save a autopark.
     *
     * @param autopark the entity to save.
     * @return the persisted entity.
     */
    Autopark save(Autopark autopark);

    /**
     * Get all the autoparks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Autopark> findAll(Pageable pageable);
    /**
     * Get all the AutoparkDTO where RentalPoint is {@code null}.
     *
     * @return the list of entities.
     */
    List<Autopark> findAllWhereRentalPointIsNull();

    /**
     * Get the "id" autopark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Autopark> findOne(Long id);

    /**
     * Delete the "id" autopark.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
