package com.prychko.lab3.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.prychko.lab3.domain.Candidate;
import com.prychko.lab3.domain.*; // for static metamodels
import com.prychko.lab3.repository.CandidateRepository;
import com.prychko.lab3.service.dto.CandidateCriteria;

/**
 * Service for executing complex queries for {@link Candidate} entities in the database.
 * The main input is a {@link CandidateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Candidate} or a {@link Page} of {@link Candidate} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CandidateQueryService extends QueryService<Candidate> {

    private final Logger log = LoggerFactory.getLogger(CandidateQueryService.class);

    private final CandidateRepository candidateRepository;

    public CandidateQueryService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    /**
     * Return a {@link List} of {@link Candidate} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Candidate> findByCriteria(CandidateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Candidate> specification = createSpecification(criteria);
        return candidateRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Candidate} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Candidate> findByCriteria(CandidateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Candidate> specification = createSpecification(criteria);
        return candidateRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CandidateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Candidate> specification = createSpecification(criteria);
        return candidateRepository.count(specification);
    }

    /**
     * Function to convert {@link CandidateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Candidate> createSpecification(CandidateCriteria criteria) {
        Specification<Candidate> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Candidate_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Candidate_.fullName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Candidate_.email));
            }
            if (criteria.getCvUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCvUrl(), Candidate_.cvUrl));
            }
            if (criteria.getInterviewsId() != null) {
                specification = specification.and(buildSpecification(criteria.getInterviewsId(),
                    root -> root.join(Candidate_.interviews, JoinType.LEFT).get(Interview_.id)));
            }
            if (criteria.getVacanciesId() != null) {
                specification = specification.and(buildSpecification(criteria.getVacanciesId(),
                    root -> root.join(Candidate_.vacancies, JoinType.LEFT).get(Vacancy_.id)));
            }
        }
        return specification;
    }
}
