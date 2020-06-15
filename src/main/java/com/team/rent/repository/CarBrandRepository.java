package com.team.rent.repository;

import com.team.rent.domain.CarBrand;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CarBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
}
