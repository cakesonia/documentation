package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.InterviewResultService;
import com.prychko.lab3.domain.InterviewResult;
import com.prychko.lab3.repository.InterviewResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link InterviewResult}.
 */
@Service
@Transactional
public class InterviewResultServiceImpl implements InterviewResultService {

    private final Logger log = LoggerFactory.getLogger(InterviewResultServiceImpl.class);

    private final InterviewResultRepository interviewResultRepository;

    public InterviewResultServiceImpl(InterviewResultRepository interviewResultRepository) {
        this.interviewResultRepository = interviewResultRepository;
    }

    /**
     * Save a interviewResult.
     *
     * @param interviewResult the entity to save.
     * @return the persisted entity.
     */
    @Override
    public InterviewResult save(InterviewResult interviewResult) {
        log.debug("Request to save InterviewResult : {}", interviewResult);
        return interviewResultRepository.save(interviewResult);
    }

    /**
     * Get all the interviewResults.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<InterviewResult> findAll() {
        log.debug("Request to get all InterviewResults");
        return interviewResultRepository.findAll();
    }


    /**
     *  Get all the interviewResults where Interview is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<InterviewResult> findAllWhereInterviewIsNull() {
        log.debug("Request to get all interviewResults where Interview is null");
        return StreamSupport
            .stream(interviewResultRepository.findAll().spliterator(), false)
            .filter(interviewResult -> interviewResult.getInterview() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one interviewResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InterviewResult> findOne(Long id) {
        log.debug("Request to get InterviewResult : {}", id);
        return interviewResultRepository.findById(id);
    }

    /**
     * Delete the interviewResult by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InterviewResult : {}", id);
        interviewResultRepository.deleteById(id);
    }
}
