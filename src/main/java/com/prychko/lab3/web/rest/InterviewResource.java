package com.prychko.lab3.web.rest;

import com.prychko.lab3.domain.Interview;
import com.prychko.lab3.service.InterviewService;
import com.prychko.lab3.web.rest.errors.BadRequestAlertException;
import com.prychko.lab3.service.dto.InterviewCriteria;
import com.prychko.lab3.service.InterviewQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.prychko.lab3.domain.Interview}.
 */
@RestController
@RequestMapping("/api")
public class InterviewResource {

    private final Logger log = LoggerFactory.getLogger(InterviewResource.class);

    private static final String ENTITY_NAME = "interview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterviewService interviewService;

    private final InterviewQueryService interviewQueryService;

    public InterviewResource(InterviewService interviewService, InterviewQueryService interviewQueryService) {
        this.interviewService = interviewService;
        this.interviewQueryService = interviewQueryService;
    }

    /**
     * {@code POST  /interviews} : Create a new interview.
     *
     * @param interview the interview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interview, or with status {@code 400 (Bad Request)} if the interview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interviews")
    public ResponseEntity<Interview> createInterview(@RequestBody Interview interview) throws URISyntaxException {
        log.debug("REST request to save Interview : {}", interview);
        if (interview.getId() != null) {
            throw new BadRequestAlertException("A new interview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Interview result = interviewService.save(interview);
        return ResponseEntity.created(new URI("/api/interviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interviews} : Updates an existing interview.
     *
     * @param interview the interview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interview,
     * or with status {@code 400 (Bad Request)} if the interview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interviews")
    public ResponseEntity<Interview> updateInterview(@RequestBody Interview interview) throws URISyntaxException {
        log.debug("REST request to update Interview : {}", interview);
        if (interview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Interview result = interviewService.save(interview);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, interview.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /interviews} : get all the interviews.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interviews in body.
     */
    @GetMapping("/interviews")
    public ResponseEntity<List<Interview>> getAllInterviews(InterviewCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Interviews by criteria: {}", criteria);
        Page<Interview> page = interviewQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interviews/count} : count all the interviews.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/interviews/count")
    public ResponseEntity<Long> countInterviews(InterviewCriteria criteria) {
        log.debug("REST request to count Interviews by criteria: {}", criteria);
        return ResponseEntity.ok().body(interviewQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interviews/:id} : get the "id" interview.
     *
     * @param id the id of the interview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interviews/{id}")
    public ResponseEntity<Interview> getInterview(@PathVariable Long id) {
        log.debug("REST request to get Interview : {}", id);
        Optional<Interview> interview = interviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interview);
    }

    /**
     * {@code DELETE  /interviews/:id} : delete the "id" interview.
     *
     * @param id the id of the interview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interviews/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        log.debug("REST request to delete Interview : {}", id);
        interviewService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
