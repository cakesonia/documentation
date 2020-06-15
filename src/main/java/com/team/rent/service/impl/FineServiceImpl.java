package com.team.rent.service.impl;

import com.team.rent.service.FineService;
import com.team.rent.domain.Fine;
import com.team.rent.repository.FineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Fine}.
 */
@Service
@Transactional
public class FineServiceImpl implements FineService {

    private final Logger log = LoggerFactory.getLogger(FineServiceImpl.class);

    private final FineRepository fineRepository;

    public FineServiceImpl(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    /**
     * Save a fine.
     *
     * @param fine the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Fine save(Fine fine) {
        log.debug("Request to save Fine : {}", fine);
        return fineRepository.save(fine);
    }

    /**
     * Get all the fines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Fine> findAll(Pageable pageable) {
        log.debug("Request to get all Fines");
        return fineRepository.findAll(pageable);
    }

    /**
     * Get one fine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Fine> findOne(Long id) {
        log.debug("Request to get Fine : {}", id);
        return fineRepository.findById(id);
    }

    /**
     * Delete the fine by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fine : {}", id);
        fineRepository.deleteById(id);
    }
}
