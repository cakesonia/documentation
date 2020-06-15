package com.team.rent.repository;

import com.team.rent.domain.Autopark;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Autopark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoparkRepository extends JpaRepository<Autopark, Long>, JpaSpecificationExecutor<Autopark> {
}
