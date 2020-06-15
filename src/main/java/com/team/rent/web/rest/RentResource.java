package com.team.rent.web.rest;

import com.team.rent.domain.Rent;
import com.team.rent.service.RentService;
import com.team.rent.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.team.rent.domain.Rent}.
 */
@RestController
@RequestMapping("/api")
public class RentResource {

    private final Logger log = LoggerFactory.getLogger(RentResource.class);

    private static final String ENTITY_NAME = "rent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentService rentService;

    public RentResource(RentService rentService) {
        this.rentService = rentService;
    }

    /**
     * {@code POST  /rents} : Create a new rent.
     *
     * @param rent the rent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rent, or with status {@code 400 (Bad Request)} if the rent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rents")
    public ResponseEntity<Rent> createRent(@RequestBody Rent rent) throws URISyntaxException {
        log.debug("REST request to save Rent : {}", rent);
        if (rent.getId() != null) {
            throw new BadRequestAlertException("A new rent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rent result = rentService.save(rent);
        return ResponseEntity.created(new URI("/api/rents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rents} : Updates an existing rent.
     *
     * @param rent the rent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rent,
     * or with status {@code 400 (Bad Request)} if the rent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rents")
    public ResponseEntity<Rent> updateRent(@RequestBody Rent rent) throws URISyntaxException {
        log.debug("REST request to update Rent : {}", rent);
        if (rent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rent result = rentService.save(rent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rents} : get all the rents.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents in body.
     */
    @GetMapping("/rents")
    public ResponseEntity<List<Rent>> getAllRents(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("request-is-null".equals(filter)) {
            log.debug("REST request to get all Rents where request is null");
            return new ResponseEntity<>(rentService.findAllWhereRequestIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Rents");
        Page<Rent> page = rentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rents/:id} : get the "id" rent.
     *
     * @param id the id of the rent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rents/{id}")
    public ResponseEntity<Rent> getRent(@PathVariable Long id) {
        log.debug("REST request to get Rent : {}", id);
        Optional<Rent> rent = rentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rent);
    }

    /**
     * {@code DELETE  /rents/:id} : delete the "id" rent.
     *
     * @param id the id of the rent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rents/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable Long id) {
        log.debug("REST request to delete Rent : {}", id);
        rentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
