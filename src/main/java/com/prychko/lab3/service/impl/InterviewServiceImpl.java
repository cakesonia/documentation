package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.InterviewService;
import com.prychko.lab3.domain.Interview;
import com.prychko.lab3.repository.InterviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Interview}.
 */
@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final Logger log = LoggerFactory.getLogger(InterviewServiceImpl.class);

    private final InterviewRepository interviewRepository;

    public InterviewServiceImpl(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    /**
     * Save a interview.
     *
     * @param interview the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Interview save(Interview interview) {
        log.debug("Request to save Interview : {}", interview);
        return interviewRepository.save(interview);
    }

    /**
     * Get all the interviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Interview> findAll(Pageable pageable) {
        log.debug("Request to get all Interviews");
        return interviewRepository.findAll(pageable);
    }

    /**
     * Get one interview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Interview> findOne(Long id) {
        log.debug("Request to get Interview : {}", id);
        return interviewRepository.findById(id);
    }

    /**
     * Delete the interview by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Interview : {}", id);
        interviewRepository.deleteById(id);
    }
}
