package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.CandidateService;
import com.prychko.lab3.domain.Candidate;
import com.prychko.lab3.repository.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Candidate}.
 */
@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private final Logger log = LoggerFactory.getLogger(CandidateServiceImpl.class);

    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    /**
     * Save a candidate.
     *
     * @param candidate the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Candidate save(Candidate candidate) {
        log.debug("Request to save Candidate : {}", candidate);
        return candidateRepository.save(candidate);
    }

    /**
     * Get all the candidates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Candidate> findAll(Pageable pageable) {
        log.debug("Request to get all Candidates");
        return candidateRepository.findAll(pageable);
    }

    /**
     * Get all the candidates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Candidate> findAllWithEagerRelationships(Pageable pageable) {
        return candidateRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one candidate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Candidate> findOne(Long id) {
        log.debug("Request to get Candidate : {}", id);
        return candidateRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the candidate by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Candidate : {}", id);
        candidateRepository.deleteById(id);
    }
}
