package com.prychko.lab3.repository;

import com.prychko.lab3.domain.ApplicationStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ApplicationStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Long> {
}
