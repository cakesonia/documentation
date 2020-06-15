package com.prychko.lab3.repository;

import com.prychko.lab3.domain.InterviewType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InterviewType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewTypeRepository extends JpaRepository<InterviewType, Long> {
}
