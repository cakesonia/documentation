package com.prychko.lab3.web.rest;

import com.prychko.lab3.domain.ApplicationStatus;
import com.prychko.lab3.service.ApplicationStatusService;
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
 * REST controller for managing {@link com.prychko.lab3.domain.ApplicationStatus}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationStatusResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationStatusResource.class);

    private static final String ENTITY_NAME = "applicationStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationStatusService applicationStatusService;

    public ApplicationStatusResource(ApplicationStatusService applicationStatusService) {
        this.applicationStatusService = applicationStatusService;
    }

    /**
     * {@code POST  /application-statuses} : Create a new applicationStatus.
     *
     * @param applicationStatus the applicationStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationStatus, or with status {@code 400 (Bad Request)} if the applicationStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-statuses")
    public ResponseEntity<ApplicationStatus> createApplicationStatus(@Valid @RequestBody ApplicationStatus applicationStatus) throws URISyntaxException {
        log.debug("REST request to save ApplicationStatus : {}", applicationStatus);
        if (applicationStatus.getId() != null) {
            throw new BadRequestAlertException("A new applicationStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationStatus result = applicationStatusService.save(applicationStatus);
        return ResponseEntity.created(new URI("/api/application-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-statuses} : Updates an existing applicationStatus.
     *
     * @param applicationStatus the applicationStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationStatus,
     * or with status {@code 400 (Bad Request)} if the applicationStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-statuses")
    public ResponseEntity<ApplicationStatus> updateApplicationStatus(@Valid @RequestBody ApplicationStatus applicationStatus) throws URISyntaxException {
        log.debug("REST request to update ApplicationStatus : {}", applicationStatus);
        if (applicationStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApplicationStatus result = applicationStatusService.save(applicationStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /application-statuses} : get all the applicationStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationStatuses in body.
     */
    @GetMapping("/application-statuses")
    public List<ApplicationStatus> getAllApplicationStatuses() {
        log.debug("REST request to get all ApplicationStatuses");
        return applicationStatusService.findAll();
    }

    /**
     * {@code GET  /application-statuses/:id} : get the "id" applicationStatus.
     *
     * @param id the id of the applicationStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-statuses/{id}")
    public ResponseEntity<ApplicationStatus> getApplicationStatus(@PathVariable Long id) {
        log.debug("REST request to get ApplicationStatus : {}", id);
        Optional<ApplicationStatus> applicationStatus = applicationStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationStatus);
    }

    /**
     * {@code DELETE  /application-statuses/:id} : delete the "id" applicationStatus.
     *
     * @param id the id of the applicationStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-statuses/{id}")
    public ResponseEntity<Void> deleteApplicationStatus(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationStatus : {}", id);
        applicationStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
