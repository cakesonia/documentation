package com.team.rent.web.rest;

import com.team.rent.domain.RentalPoint;
import com.team.rent.service.RentalPointService;
import com.team.rent.web.rest.errors.BadRequestAlertException;
import com.team.rent.service.dto.RentalPointCriteria;
import com.team.rent.service.RentalPointQueryService;

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
 * REST controller for managing {@link com.team.rent.domain.RentalPoint}.
 */
@RestController
@RequestMapping("/api")
public class RentalPointResource {

    private final Logger log = LoggerFactory.getLogger(RentalPointResource.class);

    private static final String ENTITY_NAME = "rentalPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentalPointService rentalPointService;

    private final RentalPointQueryService rentalPointQueryService;

    public RentalPointResource(RentalPointService rentalPointService, RentalPointQueryService rentalPointQueryService) {
        this.rentalPointService = rentalPointService;
        this.rentalPointQueryService = rentalPointQueryService;
    }

    /**
     * {@code POST  /rental-points} : Create a new rentalPoint.
     *
     * @param rentalPoint the rentalPoint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentalPoint, or with status {@code 400 (Bad Request)} if the rentalPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rental-points")
    public ResponseEntity<RentalPoint> createRentalPoint(@RequestBody RentalPoint rentalPoint) throws URISyntaxException {
        log.debug("REST request to save RentalPoint : {}", rentalPoint);
        if (rentalPoint.getId() != null) {
            throw new BadRequestAlertException("A new rentalPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentalPoint result = rentalPointService.save(rentalPoint);
        return ResponseEntity.created(new URI("/api/rental-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rental-points} : Updates an existing rentalPoint.
     *
     * @param rentalPoint the rentalPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalPoint,
     * or with status {@code 400 (Bad Request)} if the rentalPoint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentalPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rental-points")
    public ResponseEntity<RentalPoint> updateRentalPoint(@RequestBody RentalPoint rentalPoint) throws URISyntaxException {
        log.debug("REST request to update RentalPoint : {}", rentalPoint);
        if (rentalPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RentalPoint result = rentalPointService.save(rentalPoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rentalPoint.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rental-points} : get all the rentalPoints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentalPoints in body.
     */
    @GetMapping("/rental-points")
    public ResponseEntity<List<RentalPoint>> getAllRentalPoints(RentalPointCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RentalPoints by criteria: {}", criteria);
        Page<RentalPoint> page = rentalPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rental-points/count} : count all the rentalPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rental-points/count")
    public ResponseEntity<Long> countRentalPoints(RentalPointCriteria criteria) {
        log.debug("REST request to count RentalPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(rentalPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rental-points/:id} : get the "id" rentalPoint.
     *
     * @param id the id of the rentalPoint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentalPoint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rental-points/{id}")
    public ResponseEntity<RentalPoint> getRentalPoint(@PathVariable Long id) {
        log.debug("REST request to get RentalPoint : {}", id);
        Optional<RentalPoint> rentalPoint = rentalPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rentalPoint);
    }

    /**
     * {@code DELETE  /rental-points/:id} : delete the "id" rentalPoint.
     *
     * @param id the id of the rentalPoint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rental-points/{id}")
    public ResponseEntity<Void> deleteRentalPoint(@PathVariable Long id) {
        log.debug("REST request to delete RentalPoint : {}", id);
        rentalPointService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
