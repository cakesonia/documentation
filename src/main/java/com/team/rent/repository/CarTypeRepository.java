package com.team.rent.repository;

import com.team.rent.domain.CarType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CarType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarTypeRepository extends JpaRepository<CarType, Long> {
}
