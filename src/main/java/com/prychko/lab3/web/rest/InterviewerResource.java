package com.prychko.lab3.web.rest;

import com.prychko.lab3.domain.Interviewer;
import com.prychko.lab3.service.InterviewerService;
import com.prychko.lab3.web.rest.errors.BadRequestAlertException;
import com.prychko.lab3.service.dto.InterviewerCriteria;
import com.prychko.lab3.service.InterviewerQueryService;

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
 * REST controller for managing {@link com.prychko.lab3.domain.Interviewer}.
 */
@RestController
@RequestMapping("/api")
public class InterviewerResource {

    private final Logger log = LoggerFactory.getLogger(InterviewerResource.class);

    private static final String ENTITY_NAME = "interviewer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterviewerService interviewerService;

    private final InterviewerQueryService interviewerQueryService;

    public InterviewerResource(InterviewerService interviewerService, InterviewerQueryService interviewerQueryService) {
        this.interviewerService = interviewerService;
        this.interviewerQueryService = interviewerQueryService;
    }

    /**
     * {@code POST  /interviewers} : Create a new interviewer.
     *
     * @param interviewer the interviewer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interviewer, or with status {@code 400 (Bad Request)} if the interviewer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interviewers")
    public ResponseEntity<Interviewer> createInterviewer(@Valid @RequestBody Interviewer interviewer) throws URISyntaxException {
        log.debug("REST request to save Interviewer : {}", interviewer);
        if (interviewer.getId() != null) {
            throw new BadRequestAlertException("A new interviewer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Interviewer result = interviewerService.save(interviewer);
        return ResponseEntity.created(new URI("/api/interviewers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interviewers} : Updates an existing interviewer.
     *
     * @param interviewer the interviewer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interviewer,
     * or with status {@code 400 (Bad Request)} if the interviewer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interviewer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interviewers")
    public ResponseEntity<Interviewer> updateInterviewer(@Valid @RequestBody Interviewer interviewer) throws URISyntaxException {
        log.debug("REST request to update Interviewer : {}", interviewer);
        if (interviewer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Interviewer result = interviewerService.save(interviewer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, interviewer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /interviewers} : get all the interviewers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interviewers in body.
     */
    @GetMapping("/interviewers")
    public ResponseEntity<List<Interviewer>> getAllInterviewers(InterviewerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Interviewers by criteria: {}", criteria);
        Page<Interviewer> page = interviewerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interviewers/count} : count all the interviewers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/interviewers/count")
    public ResponseEntity<Long> countInterviewers(InterviewerCriteria criteria) {
        log.debug("REST request to count Interviewers by criteria: {}", criteria);
        return ResponseEntity.ok().body(interviewerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interviewers/:id} : get the "id" interviewer.
     *
     * @param id the id of the interviewer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interviewer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interviewers/{id}")
    public ResponseEntity<Interviewer> getInterviewer(@PathVariable Long id) {
        log.debug("REST request to get Interviewer : {}", id);
        Optional<Interviewer> interviewer = interviewerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interviewer);
    }

    /**
     * {@code DELETE  /interviewers/:id} : delete the "id" interviewer.
     *
     * @param id the id of the interviewer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interviewers/{id}")
    public ResponseEntity<Void> deleteInterviewer(@PathVariable Long id) {
        log.debug("REST request to delete Interviewer : {}", id);
        interviewerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
