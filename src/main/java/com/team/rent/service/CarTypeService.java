package com.team.rent.service;

import com.team.rent.domain.CarType;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CarType}.
 */
public interface CarTypeService {

    /**
     * Save a carType.
     *
     * @param carType the entity to save.
     * @return the persisted entity.
     */
    CarType save(CarType carType);

    /**
     * Get all the carTypes.
     *
     * @return the list of entities.
     */
    List<CarType> findAll();

    /**
     * Get the "id" carType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarType> findOne(Long id);

    /**
     * Delete the "id" carType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
