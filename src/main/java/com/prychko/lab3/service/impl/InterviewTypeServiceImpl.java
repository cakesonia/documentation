package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.InterviewTypeService;
import com.prychko.lab3.domain.InterviewType;
import com.prychko.lab3.repository.InterviewTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link InterviewType}.
 */
@Service
@Transactional
public class InterviewTypeServiceImpl implements InterviewTypeService {

    private final Logger log = LoggerFactory.getLogger(InterviewTypeServiceImpl.class);

    private final InterviewTypeRepository interviewTypeRepository;

    public InterviewTypeServiceImpl(InterviewTypeRepository interviewTypeRepository) {
        this.interviewTypeRepository = interviewTypeRepository;
    }

    /**
     * Save a interviewType.
     *
     * @param interviewType the entity to save.
     * @return the persisted entity.
     */
    @Override
    public InterviewType save(InterviewType interviewType) {
        log.debug("Request to save InterviewType : {}", interviewType);
        return interviewTypeRepository.save(interviewType);
    }

    /**
     * Get all the interviewTypes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<InterviewType> findAll() {
        log.debug("Request to get all InterviewTypes");
        return interviewTypeRepository.findAll();
    }

    /**
     * Get one interviewType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InterviewType> findOne(Long id) {
        log.debug("Request to get InterviewType : {}", id);
        return interviewTypeRepository.findById(id);
    }

    /**
     * Delete the interviewType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InterviewType : {}", id);
        interviewTypeRepository.deleteById(id);
    }
}
