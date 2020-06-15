package com.team.rent.service.impl;

import com.team.rent.service.RentService;
import com.team.rent.domain.Rent;
import com.team.rent.repository.RentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Rent}.
 */
@Service
@Transactional
public class RentServiceImpl implements RentService {

    private final Logger log = LoggerFactory.getLogger(RentServiceImpl.class);

    private final RentRepository rentRepository;

    public RentServiceImpl(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    /**
     * Save a rent.
     *
     * @param rent the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Rent save(Rent rent) {
        log.debug("Request to save Rent : {}", rent);
        return rentRepository.save(rent);
    }

    /**
     * Get all the rents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Rent> findAll(Pageable pageable) {
        log.debug("Request to get all Rents");
        return rentRepository.findAll(pageable);
    }


    /**
     *  Get all the rents where Request is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<Rent> findAllWhereRequestIsNull() {
        log.debug("Request to get all rents where Request is null");
        return StreamSupport
            .stream(rentRepository.findAll().spliterator(), false)
            .filter(rent -> rent.getRequest() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one rent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Rent> findOne(Long id) {
        log.debug("Request to get Rent : {}", id);
        return rentRepository.findById(id);
    }

    /**
     * Delete the rent by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rent : {}", id);
        rentRepository.deleteById(id);
    }
}
