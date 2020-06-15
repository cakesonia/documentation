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

import com.team.rent.domain.RentalPoint;
import com.team.rent.domain.*; // for static metamodels
import com.team.rent.repository.RentalPointRepository;
import com.team.rent.service.dto.RentalPointCriteria;

/**
 * Service for executing complex queries for {@link RentalPoint} entities in the database.
 * The main input is a {@link RentalPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RentalPoint} or a {@link Page} of {@link RentalPoint} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RentalPointQueryService extends QueryService<RentalPoint> {

    private final Logger log = LoggerFactory.getLogger(RentalPointQueryService.class);

    private final RentalPointRepository rentalPointRepository;

    public RentalPointQueryService(RentalPointRepository rentalPointRepository) {
        this.rentalPointRepository = rentalPointRepository;
    }

    /**
     * Return a {@link List} of {@link RentalPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RentalPoint> findByCriteria(RentalPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RentalPoint> specification = createSpecification(criteria);
        return rentalPointRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RentalPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RentalPoint> findByCriteria(RentalPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RentalPoint> specification = createSpecification(criteria);
        return rentalPointRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RentalPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RentalPoint> specification = createSpecification(criteria);
        return rentalPointRepository.count(specification);
    }

    /**
     * Function to convert {@link RentalPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RentalPoint> createSpecification(RentalPointCriteria criteria) {
        Specification<RentalPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RentalPoint_.id));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), RentalPoint_.location));
            }
            if (criteria.getWorktime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWorktime(), RentalPoint_.worktime));
            }
            if (criteria.getAutoparkId() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoparkId(),
                    root -> root.join(RentalPoint_.autopark, JoinType.LEFT).get(Autopark_.id)));
            }
            if (criteria.getClientsId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientsId(),
                    root -> root.join(RentalPoint_.clients, JoinType.LEFT).get(Client_.id)));
            }
        }
        return specification;
    }
}
