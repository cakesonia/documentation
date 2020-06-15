package com.prychko.lab3.repository;

import com.prychko.lab3.domain.VacancyStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the VacancyStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VacancyStatusRepository extends JpaRepository<VacancyStatus, Long> {
}
