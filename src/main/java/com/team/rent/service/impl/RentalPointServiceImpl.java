package com.team.rent.service.impl;

import com.team.rent.service.RentalPointService;
import com.team.rent.domain.RentalPoint;
import com.team.rent.repository.RentalPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RentalPoint}.
 */
@Service
@Transactional
public class RentalPointServiceImpl implements RentalPointService {

    private final Logger log = LoggerFactory.getLogger(RentalPointServiceImpl.class);

    private final RentalPointRepository rentalPointRepository;

    public RentalPointServiceImpl(RentalPointRepository rentalPointRepository) {
        this.rentalPointRepository = rentalPointRepository;
    }

    /**
     * Save a rentalPoint.
     *
     * @param rentalPoint the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RentalPoint save(RentalPoint rentalPoint) {
        log.debug("Request to save RentalPoint : {}", rentalPoint);
        return rentalPointRepository.save(rentalPoint);
    }

    /**
     * Get all the rentalPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RentalPoint> findAll(Pageable pageable) {
        log.debug("Request to get all RentalPoints");
        return rentalPointRepository.findAll(pageable);
    }

    /**
     * Get all the rentalPoints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RentalPoint> findAllWithEagerRelationships(Pageable pageable) {
        return rentalPointRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one rentalPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RentalPoint> findOne(Long id) {
        log.debug("Request to get RentalPoint : {}", id);
        return rentalPointRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the rentalPoint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RentalPoint : {}", id);
        rentalPointRepository.deleteById(id);
    }
}
