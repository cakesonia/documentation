package com.team.rent.service;

import com.team.rent.domain.Car;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Car}.
 */
public interface CarService {

    /**
     * Save a car.
     *
     * @param car the entity to save.
     * @return the persisted entity.
     */
    Car save(Car car);

    /**
     * Get all the cars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Car> findAll(Pageable pageable);
    /**
     * Get all the CarDTO where Request is {@code null}.
     *
     * @return the list of entities.
     */
    List<Car> findAllWhereRequestIsNull();

    /**
     * Get the "id" car.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Car> findOne(Long id);

    /**
     * Delete the "id" car.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
