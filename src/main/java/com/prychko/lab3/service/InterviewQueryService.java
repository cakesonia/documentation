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

import com.prychko.lab3.domain.Interview;
import com.prychko.lab3.domain.*; // for static metamodels
import com.prychko.lab3.repository.InterviewRepository;
import com.prychko.lab3.service.dto.InterviewCriteria;

/**
 * Service for executing complex queries for {@link Interview} entities in the database.
 * The main input is a {@link InterviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Interview} or a {@link Page} of {@link Interview} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterviewQueryService extends QueryService<Interview> {

    private final Logger log = LoggerFactory.getLogger(InterviewQueryService.class);

    private final InterviewRepository interviewRepository;

    public InterviewQueryService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    /**
     * Return a {@link List} of {@link Interview} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Interview> findByCriteria(InterviewCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Interview> specification = createSpecification(criteria);
        return interviewRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Interview} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Interview> findByCriteria(InterviewCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Interview> specification = createSpecification(criteria);
        return interviewRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterviewCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Interview> specification = createSpecification(criteria);
        return interviewRepository.count(specification);
    }

    /**
     * Function to convert {@link InterviewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Interview> createSpecification(InterviewCriteria criteria) {
        Specification<Interview> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Interview_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Interview_.date));
            }
            if (criteria.getResultId() != null) {
                specification = specification.and(buildSpecification(criteria.getResultId(),
                    root -> root.join(Interview_.result, JoinType.LEFT).get(InterviewResult_.id)));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Interview_.types, JoinType.LEFT).get(InterviewType_.id)));
            }
            if (criteria.getCandidateId() != null) {
                specification = specification.and(buildSpecification(criteria.getCandidateId(),
                    root -> root.join(Interview_.candidate, JoinType.LEFT).get(Candidate_.id)));
            }
            if (criteria.getInterviewerId() != null) {
                specification = specification.and(buildSpecification(criteria.getInterviewerId(),
                    root -> root.join(Interview_.interviewer, JoinType.LEFT).get(Interviewer_.id)));
            }
        }
        return specification;
    }
}
