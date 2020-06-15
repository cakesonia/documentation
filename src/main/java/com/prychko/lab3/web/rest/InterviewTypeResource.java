package com.prychko.lab3.web.rest;

import com.prychko.lab3.domain.InterviewType;
import com.prychko.lab3.service.InterviewTypeService;
import com.prychko.lab3.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.prychko.lab3.domain.InterviewType}.
 */
@RestController
@RequestMapping("/api")
public class InterviewTypeResource {

    private final Logger log = LoggerFactory.getLogger(InterviewTypeResource.class);

    private static final String ENTITY_NAME = "interviewType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterviewTypeService interviewTypeService;

    public InterviewTypeResource(InterviewTypeService interviewTypeService) {
        this.interviewTypeService = interviewTypeService;
    }

    /**
     * {@code POST  /interview-types} : Create a new interviewType.
     *
     * @param interviewType the interviewType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interviewType, or with status {@code 400 (Bad Request)} if the interviewType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interview-types")
    public ResponseEntity<InterviewType> createInterviewType(@Valid @RequestBody InterviewType interviewType) throws URISyntaxException {
        log.debug("REST request to save InterviewType : {}", interviewType);
        if (interviewType.getId() != null) {
            throw new BadRequestAlertException("A new interviewType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterviewType result = interviewTypeService.save(interviewType);
        return ResponseEntity.created(new URI("/api/interview-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interview-types} : Updates an existing interviewType.
     *
     * @param interviewType the interviewType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interviewType,
     * or with status {@code 400 (Bad Request)} if the interviewType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interviewType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interview-types")
    public ResponseEntity<InterviewType> updateInterviewType(@Valid @RequestBody InterviewType interviewType) throws URISyntaxException {
        log.debug("REST request to update InterviewType : {}", interviewType);
        if (interviewType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InterviewType result = interviewTypeService.save(interviewType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, interviewType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /interview-types} : get all the interviewTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interviewTypes in body.
     */
    @GetMapping("/interview-types")
    public List<InterviewType> getAllInterviewTypes() {
        log.debug("REST request to get all InterviewTypes");
        return interviewTypeService.findAll();
    }

    /**
     * {@code GET  /interview-types/:id} : get the "id" interviewType.
     *
     * @param id the id of the interviewType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interviewType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interview-types/{id}")
    public ResponseEntity<InterviewType> getInterviewType(@PathVariable Long id) {
        log.debug("REST request to get InterviewType : {}", id);
        Optional<InterviewType> interviewType = interviewTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interviewType);
    }

    /**
     * {@code DELETE  /interview-types/:id} : delete the "id" interviewType.
     *
     * @param id the id of the interviewType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interview-types/{id}")
    public ResponseEntity<Void> deleteInterviewType(@PathVariable Long id) {
        log.debug("REST request to delete InterviewType : {}", id);
        interviewTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
