package com.prychko.lab3.repository;

import com.prychko.lab3.domain.Vacancy;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Vacancy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {
}
