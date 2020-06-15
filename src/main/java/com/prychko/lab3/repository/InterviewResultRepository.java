package com.prychko.lab3.repository;

import com.prychko.lab3.domain.InterviewResult;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InterviewResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewResultRepository extends JpaRepository<InterviewResult, Long> {
}
