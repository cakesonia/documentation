package com.team.rent.repository;

import com.team.rent.domain.RentalPoint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the RentalPoint entity.
 */
@Repository
public interface RentalPointRepository extends JpaRepository<RentalPoint, Long>, JpaSpecificationExecutor<RentalPoint> {

    @Query(value = "select distinct rentalPoint from RentalPoint rentalPoint left join fetch rentalPoint.clients",
        countQuery = "select count(distinct rentalPoint) from RentalPoint rentalPoint")
    Page<RentalPoint> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct rentalPoint from RentalPoint rentalPoint left join fetch rentalPoint.clients")
    List<RentalPoint> findAllWithEagerRelationships();

    @Query("select rentalPoint from RentalPoint rentalPoint left join fetch rentalPoint.clients where rentalPoint.id =:id")
    Optional<RentalPoint> findOneWithEagerRelationships(@Param("id") Long id);
}
