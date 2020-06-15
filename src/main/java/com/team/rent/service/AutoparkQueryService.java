package com.team.rent.service;

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

import com.team.rent.domain.Autopark;
import com.team.rent.domain.*; // for static metamodels
import com.team.rent.repository.AutoparkRepository;
import com.team.rent.service.dto.AutoparkCriteria;

/**
 * Service for executing complex queries for {@link Autopark} entities in the database.
 * The main input is a {@link AutoparkCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Autopark} or a {@link Page} of {@link Autopark} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoparkQueryService extends QueryService<Autopark> {

    private final Logger log = LoggerFactory.getLogger(AutoparkQueryService.class);

    private final AutoparkRepository autoparkRepository;

    public AutoparkQueryService(AutoparkRepository autoparkRepository) {
        this.autoparkRepository = autoparkRepository;
    }

    /**
     * Return a {@link List} of {@link Autopark} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Autopark> findByCriteria(AutoparkCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Autopark> specification = createSpecification(criteria);
        return autoparkRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Autopark} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Autopark> findByCriteria(AutoparkCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Autopark> specification = createSpecification(criteria);
        return autoparkRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoparkCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Autopark> specification = createSpecification(criteria);
        return autoparkRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoparkCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Autopark> createSpecification(AutoparkCriteria criteria) {
        Specification<Autopark> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Autopark_.id));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Autopark_.location));
            }
            if (criteria.getAvailableCars() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvailableCars(), Autopark_.availableCars));
            }
            if (criteria.getCarsId() != null) {
                specification = specification.and(buildSpecification(criteria.getCarsId(),
                    root -> root.join(Autopark_.cars, JoinType.LEFT).get(Car_.id)));
            }
            if (criteria.getRentalPointId() != null) {
                specification = specification.and(buildSpecification(criteria.getRentalPointId(),
                    root -> root.join(Autopark_.rentalPoint, JoinType.LEFT).get(RentalPoint_.id)));
            }
        }
        return specification;
    }
}
