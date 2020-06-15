package com.prychko.lab3.repository;

import com.prychko.lab3.domain.Interview;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Interview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long>, JpaSpecificationExecutor<Interview> {
}
