package com.prychko.lab3.web.rest;

import com.prychko.lab3.domain.VacancyStatus;
import com.prychko.lab3.service.VacancyStatusService;
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
 * REST controller for managing {@link com.prychko.lab3.domain.VacancyStatus}.
 */
@RestController
@RequestMapping("/api")
public class VacancyStatusResource {

    private final Logger log = LoggerFactory.getLogger(VacancyStatusResource.class);

    private static final String ENTITY_NAME = "vacancyStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VacancyStatusService vacancyStatusService;

    public VacancyStatusResource(VacancyStatusService vacancyStatusService) {
        this.vacancyStatusService = vacancyStatusService;
    }

    /**
     * {@code POST  /vacancy-statuses} : Create a new vacancyStatus.
     *
     * @param vacancyStatus the vacancyStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vacancyStatus, or with status {@code 400 (Bad Request)} if the vacancyStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vacancy-statuses")
    public ResponseEntity<VacancyStatus> createVacancyStatus(@Valid @RequestBody VacancyStatus vacancyStatus) throws URISyntaxException {
        log.debug("REST request to save VacancyStatus : {}", vacancyStatus);
        if (vacancyStatus.getId() != null) {
            throw new BadRequestAlertException("A new vacancyStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VacancyStatus result = vacancyStatusService.save(vacancyStatus);
        return ResponseEntity.created(new URI("/api/vacancy-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vacancy-statuses} : Updates an existing vacancyStatus.
     *
     * @param vacancyStatus the vacancyStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacancyStatus,
     * or with status {@code 400 (Bad Request)} if the vacancyStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vacancyStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vacancy-statuses")
    public ResponseEntity<VacancyStatus> updateVacancyStatus(@Valid @RequestBody VacancyStatus vacancyStatus) throws URISyntaxException {
        log.debug("REST request to update VacancyStatus : {}", vacancyStatus);
        if (vacancyStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VacancyStatus result = vacancyStatusService.save(vacancyStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vacancyStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vacancy-statuses} : get all the vacancyStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vacancyStatuses in body.
     */
    @GetMapping("/vacancy-statuses")
    public List<VacancyStatus> getAllVacancyStatuses() {
        log.debug("REST request to get all VacancyStatuses");
        return vacancyStatusService.findAll();
    }

    /**
     * {@code GET  /vacancy-statuses/:id} : get the "id" vacancyStatus.
     *
     * @param id the id of the vacancyStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vacancyStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vacancy-statuses/{id}")
    public ResponseEntity<VacancyStatus> getVacancyStatus(@PathVariable Long id) {
        log.debug("REST request to get VacancyStatus : {}", id);
        Optional<VacancyStatus> vacancyStatus = vacancyStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vacancyStatus);
    }

    /**
     * {@code DELETE  /vacancy-statuses/:id} : delete the "id" vacancyStatus.
     *
     * @param id the id of the vacancyStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vacancy-statuses/{id}")
    public ResponseEntity<Void> deleteVacancyStatus(@PathVariable Long id) {
        log.debug("REST request to delete VacancyStatus : {}", id);
        vacancyStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
