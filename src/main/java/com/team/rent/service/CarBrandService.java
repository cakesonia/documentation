package com.team.rent.service;

import com.team.rent.domain.CarBrand;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CarBrand}.
 */
public interface CarBrandService {

    /**
     * Save a carBrand.
     *
     * @param carBrand the entity to save.
     * @return the persisted entity.
     */
    CarBrand save(CarBrand carBrand);

    /**
     * Get all the carBrands.
     *
     * @return the list of entities.
     */
    List<CarBrand> findAll();

    /**
     * Get the "id" carBrand.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarBrand> findOne(Long id);

    /**
     * Delete the "id" carBrand.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
