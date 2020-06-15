package com.team.rent.repository;

import com.team.rent.domain.Fine;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Fine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
}
