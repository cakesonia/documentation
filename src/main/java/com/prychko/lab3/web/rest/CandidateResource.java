package com.prychko.lab3.web.rest;

import com.prychko.lab3.domain.Candidate;
import com.prychko.lab3.service.CandidateService;
import com.prychko.lab3.web.rest.errors.BadRequestAlertException;
import com.prychko.lab3.service.dto.CandidateCriteria;
import com.prychko.lab3.service.CandidateQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.prychko.lab3.domain.Candidate}.
 */
@RestController
@RequestMapping("/api")
public class CandidateResource {

    private final Logger log = LoggerFactory.getLogger(CandidateResource.class);

    private static final String ENTITY_NAME = "candidate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidateService candidateService;

    private final CandidateQueryService candidateQueryService;

    public CandidateResource(CandidateService candidateService, CandidateQueryService candidateQueryService) {
        this.candidateService = candidateService;
        this.candidateQueryService = candidateQueryService;
    }

    /**
     * {@code POST  /candidates} : Create a new candidate.
     *
     * @param candidate the candidate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidate, or with status {@code 400 (Bad Request)} if the candidate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/candidates")
    public ResponseEntity<Candidate> createCandidate(@Valid @RequestBody Candidate candidate) throws URISyntaxException {
        log.debug("REST request to save Candidate : {}", candidate);
        if (candidate.getId() != null) {
            throw new BadRequestAlertException("A new candidate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Candidate result = candidateService.save(candidate);
        return ResponseEntity.created(new URI("/api/candidates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /candidates} : Updates an existing candidate.
     *
     * @param candidate the candidate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidate,
     * or with status {@code 400 (Bad Request)} if the candidate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/candidates")
    public ResponseEntity<Candidate> updateCandidate(@Valid @RequestBody Candidate candidate) throws URISyntaxException {
        log.debug("REST request to update Candidate : {}", candidate);
        if (candidate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Candidate result = candidateService.save(candidate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, candidate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /candidates} : get all the candidates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidates in body.
     */
    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getAllCandidates(CandidateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Candidates by criteria: {}", criteria);
        Page<Candidate> page = candidateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /candidates/count} : count all the candidates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/candidates/count")
    public ResponseEntity<Long> countCandidates(CandidateCriteria criteria) {
        log.debug("REST request to count Candidates by criteria: {}", criteria);
        return ResponseEntity.ok().body(candidateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /candidates/:id} : get the "id" candidate.
     *
     * @param id the id of the candidate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/candidates/{id}")
    public ResponseEntity<Candidate> getCandidate(@PathVariable Long id) {
        log.debug("REST request to get Candidate : {}", id);
        Optional<Candidate> candidate = candidateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidate);
    }

    /**
     * {@code DELETE  /candidates/:id} : delete the "id" candidate.
     *
     * @param id the id of the candidate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        log.debug("REST request to delete Candidate : {}", id);
        candidateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
