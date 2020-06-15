package com.team.rent.web.rest;

import com.team.rent.domain.Autopark;
import com.team.rent.service.AutoparkService;
import com.team.rent.web.rest.errors.BadRequestAlertException;
import com.team.rent.service.dto.AutoparkCriteria;
import com.team.rent.service.AutoparkQueryService;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.team.rent.domain.Autopark}.
 */
@RestController
@RequestMapping("/api")
public class AutoparkResource {

    private final Logger log = LoggerFactory.getLogger(AutoparkResource.class);

    private static final String ENTITY_NAME = "autopark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoparkService autoparkService;

    private final AutoparkQueryService autoparkQueryService;

    public AutoparkResource(AutoparkService autoparkService, AutoparkQueryService autoparkQueryService) {
        this.autoparkService = autoparkService;
        this.autoparkQueryService = autoparkQueryService;
    }

    /**
     * {@code POST  /autoparks} : Create a new autopark.
     *
     * @param autopark the autopark to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autopark, or with status {@code 400 (Bad Request)} if the autopark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/autoparks")
    public ResponseEntity<Autopark> createAutopark(@RequestBody Autopark autopark) throws URISyntaxException {
        log.debug("REST request to save Autopark : {}", autopark);
        if (autopark.getId() != null) {
            throw new BadRequestAlertException("A new autopark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Autopark result = autoparkService.save(autopark);
        return ResponseEntity.created(new URI("/api/autoparks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /autoparks} : Updates an existing autopark.
     *
     * @param autopark the autopark to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autopark,
     * or with status {@code 400 (Bad Request)} if the autopark is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autopark couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/autoparks")
    public ResponseEntity<Autopark> updateAutopark(@RequestBody Autopark autopark) throws URISyntaxException {
        log.debug("REST request to update Autopark : {}", autopark);
        if (autopark.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Autopark result = autoparkService.save(autopark);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, autopark.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /autoparks} : get all the autoparks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoparks in body.
     */
    @GetMapping("/autoparks")
    public ResponseEntity<List<Autopark>> getAllAutoparks(AutoparkCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Autoparks by criteria: {}", criteria);
        Page<Autopark> page = autoparkQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /autoparks/count} : count all the autoparks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/autoparks/count")
    public ResponseEntity<Long> countAutoparks(AutoparkCriteria criteria) {
        log.debug("REST request to count Autoparks by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoparkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /autoparks/:id} : get the "id" autopark.
     *
     * @param id the id of the autopark to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autopark, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/autoparks/{id}")
    public ResponseEntity<Autopark> getAutopark(@PathVariable Long id) {
        log.debug("REST request to get Autopark : {}", id);
        Optional<Autopark> autopark = autoparkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autopark);
    }

    /**
     * {@code DELETE  /autoparks/:id} : delete the "id" autopark.
     *
     * @param id the id of the autopark to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/autoparks/{id}")
    public ResponseEntity<Void> deleteAutopark(@PathVariable Long id) {
        log.debug("REST request to delete Autopark : {}", id);
        autoparkService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
