package com.team.rent.service;

import com.team.rent.domain.Rent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Rent}.
 */
public interface RentService {

    /**
     * Save a rent.
     *
     * @param rent the entity to save.
     * @return the persisted entity.
     */
    Rent save(Rent rent);

    /**
     * Get all the rents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Rent> findAll(Pageable pageable);
    /**
     * Get all the RentDTO where Request is {@code null}.
     *
     * @return the list of entities.
     */
    List<Rent> findAllWhereRequestIsNull();

    /**
     * Get the "id" rent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rent> findOne(Long id);

    /**
     * Delete the "id" rent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
