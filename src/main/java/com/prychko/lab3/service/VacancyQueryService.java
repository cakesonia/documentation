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

import com.prychko.lab3.domain.Vacancy;
import com.prychko.lab3.domain.*; // for static metamodels
import com.prychko.lab3.repository.VacancyRepository;
import com.prychko.lab3.service.dto.VacancyCriteria;

/**
 * Service for executing complex queries for {@link Vacancy} entities in the database.
 * The main input is a {@link VacancyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Vacancy} or a {@link Page} of {@link Vacancy} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VacancyQueryService extends QueryService<Vacancy> {

    private final Logger log = LoggerFactory.getLogger(VacancyQueryService.class);

    private final VacancyRepository vacancyRepository;

    public VacancyQueryService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    /**
     * Return a {@link List} of {@link Vacancy} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Vacancy> findByCriteria(VacancyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vacancy> specification = createSpecification(criteria);
        return vacancyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Vacancy} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Vacancy> findByCriteria(VacancyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vacancy> specification = createSpecification(criteria);
        return vacancyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VacancyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vacancy> specification = createSpecification(criteria);
        return vacancyRepository.count(specification);
    }

    /**
     * Function to convert {@link VacancyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vacancy> createSpecification(VacancyCriteria criteria) {
        Specification<Vacancy> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vacancy_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Vacancy_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Vacancy_.description));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), Vacancy_.salary));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Vacancy_.createdDate));
            }
            if (criteria.getStatusId() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusId(),
                    root -> root.join(Vacancy_.statuses, JoinType.LEFT).get(VacancyStatus_.id)));
            }
            if (criteria.getApplicationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getApplicationsId(),
                    root -> root.join(Vacancy_.applications, JoinType.LEFT).get(Application_.id)));
            }
            if (criteria.getCandidatesId() != null) {
                specification = specification.and(buildSpecification(criteria.getCandidatesId(),
                    root -> root.join(Vacancy_.candidates, JoinType.LEFT).get(Candidate_.id)));
            }
        }
        return specification;
    }
}
