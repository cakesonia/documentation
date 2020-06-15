package com.prychko.lab3.repository;

import com.prychko.lab3.domain.Interviewer;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Interviewer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Long>, JpaSpecificationExecutor<Interviewer> {
}
