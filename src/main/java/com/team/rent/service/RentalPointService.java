package com.team.rent.service;

import com.team.rent.domain.RentalPoint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RentalPoint}.
 */
public interface RentalPointService {

    /**
     * Save a rentalPoint.
     *
     * @param rentalPoint the entity to save.
     * @return the persisted entity.
     */
    RentalPoint save(RentalPoint rentalPoint);

    /**
     * Get all the rentalPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentalPoint> findAll(Pageable pageable);

    /**
     * Get all the rentalPoints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<RentalPoint> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" rentalPoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentalPoint> findOne(Long id);

    /**
     * Delete the "id" rentalPoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
