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

import com.team.rent.domain.Car;
import com.team.rent.domain.*; // for static metamodels
import com.team.rent.repository.CarRepository;
import com.team.rent.service.dto.CarCriteria;

/**
 * Service for executing complex queries for {@link Car} entities in the database.
 * The main input is a {@link CarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Car} or a {@link Page} of {@link Car} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarQueryService extends QueryService<Car> {

    private final Logger log = LoggerFactory.getLogger(CarQueryService.class);

    private final CarRepository carRepository;

    public CarQueryService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Return a {@link List} of {@link Car} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Car> findByCriteria(CarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Car} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Car> findByCriteria(CarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.count(specification);
    }

    /**
     * Function to convert {@link CarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Car> createSpecification(CarCriteria criteria) {
        Specification<Car> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Car_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Car_.price));
            }
            if (criteria.getManufacturedYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getManufacturedYear(), Car_.manufacturedYear));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(),
                    root -> root.join(Car_.request, JoinType.LEFT).get(Request_.id)));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Car_.type, JoinType.LEFT).get(CarType_.id)));
            }
            if (criteria.getBrandId() != null) {
                specification = specification.and(buildSpecification(criteria.getBrandId(),
                    root -> root.join(Car_.brand, JoinType.LEFT).get(CarBrand_.id)));
            }
            if (criteria.getAutoparkId() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoparkId(),
                    root -> root.join(Car_.autopark, JoinType.LEFT).get(Autopark_.id)));
            }
        }
        return specification;
    }
}
