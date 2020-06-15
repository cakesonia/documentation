package com.team.rent.service.impl;

import com.team.rent.service.CarBrandService;
import com.team.rent.domain.CarBrand;
import com.team.rent.repository.CarBrandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CarBrand}.
 */
@Service
@Transactional
public class CarBrandServiceImpl implements CarBrandService {

    private final Logger log = LoggerFactory.getLogger(CarBrandServiceImpl.class);

    private final CarBrandRepository carBrandRepository;

    public CarBrandServiceImpl(CarBrandRepository carBrandRepository) {
        this.carBrandRepository = carBrandRepository;
    }

    /**
     * Save a carBrand.
     *
     * @param carBrand the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CarBrand save(CarBrand carBrand) {
        log.debug("Request to save CarBrand : {}", carBrand);
        return carBrandRepository.save(carBrand);
    }

    /**
     * Get all the carBrands.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CarBrand> findAll() {
        log.debug("Request to get all CarBrands");
        return carBrandRepository.findAll();
    }

    /**
     * Get one carBrand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CarBrand> findOne(Long id) {
        log.debug("Request to get CarBrand : {}", id);
        return carBrandRepository.findById(id);
    }

    /**
     * Delete the carBrand by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarBrand : {}", id);
        carBrandRepository.deleteById(id);
    }
}
