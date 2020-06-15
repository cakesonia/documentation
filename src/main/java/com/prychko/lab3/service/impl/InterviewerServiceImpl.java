package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.InterviewerService;
import com.prychko.lab3.domain.Interviewer;
import com.prychko.lab3.repository.InterviewerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Interviewer}.
 */
@Service
@Transactional
public class InterviewerServiceImpl implements InterviewerService {

    private final Logger log = LoggerFactory.getLogger(InterviewerServiceImpl.class);

    private final InterviewerRepository interviewerRepository;

    public InterviewerServiceImpl(InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
    }

    /**
     * Save a interviewer.
     *
     * @param interviewer the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Interviewer save(Interviewer interviewer) {
        log.debug("Request to save Interviewer : {}", interviewer);
        return interviewerRepository.save(interviewer);
    }

    /**
     * Get all the interviewers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Interviewer> findAll(Pageable pageable) {
        log.debug("Request to get all Interviewers");
        return interviewerRepository.findAll(pageable);
    }

    /**
     * Get one interviewer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Interviewer> findOne(Long id) {
        log.debug("Request to get Interviewer : {}", id);
        return interviewerRepository.findById(id);
    }

    /**
     * Delete the interviewer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Interviewer : {}", id);
        interviewerRepository.deleteById(id);
    }
}
