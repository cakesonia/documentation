package com.prychko.lab3.repository;

import com.prychko.lab3.domain.Candidate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Candidate entity.
 */
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    @Query(value = "select distinct candidate from Candidate candidate left join fetch candidate.vacancies",
        countQuery = "select count(distinct candidate) from Candidate candidate")
    Page<Candidate> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct candidate from Candidate candidate left join fetch candidate.vacancies")
    List<Candidate> findAllWithEagerRelationships();

    @Query("select candidate from Candidate candidate left join fetch candidate.vacancies where candidate.id =:id")
    Optional<Candidate> findOneWithEagerRelationships(@Param("id") Long id);
}
