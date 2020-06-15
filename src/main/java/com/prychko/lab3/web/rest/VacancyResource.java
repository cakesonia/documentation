package com.prychko.lab3.web.rest;

import com.prychko.lab3.domain.Vacancy;
import com.prychko.lab3.service.VacancyService;
import com.prychko.lab3.web.rest.errors.BadRequestAlertException;
import com.prychko.lab3.service.dto.VacancyCriteria;
import com.prychko.lab3.service.VacancyQueryService;

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
 * REST controller for managing {@link com.prychko.lab3.domain.Vacancy}.
 */
@RestController
@RequestMapping("/api")
public class VacancyResource {

    private final Logger log = LoggerFactory.getLogger(VacancyResource.class);

    private static final String ENTITY_NAME = "vacancy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VacancyService vacancyService;

    private final VacancyQueryService vacancyQueryService;

    public VacancyResource(VacancyService vacancyService, VacancyQueryService vacancyQueryService) {
        this.vacancyService = vacancyService;
        this.vacancyQueryService = vacancyQueryService;
    }

    /**
     * {@code POST  /vacancies} : Create a new vacancy.
     *
     * @param vacancy the vacancy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vacancy, or with status {@code 400 (Bad Request)} if the vacancy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vacancies")
    public ResponseEntity<Vacancy> createVacancy(@Valid @RequestBody Vacancy vacancy) throws URISyntaxException {
        log.debug("REST request to save Vacancy : {}", vacancy);
        if (vacancy.getId() != null) {
            throw new BadRequestAlertException("A new vacancy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vacancy result = vacancyService.save(vacancy);
        return ResponseEntity.created(new URI("/api/vacancies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vacancies} : Updates an existing vacancy.
     *
     * @param vacancy the vacancy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacancy,
     * or with status {@code 400 (Bad Request)} if the vacancy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vacancy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vacancies")
    public ResponseEntity<Vacancy> updateVacancy(@Valid @RequestBody Vacancy vacancy) throws URISyntaxException {
        log.debug("REST request to update Vacancy : {}", vacancy);
        if (vacancy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vacancy result = vacancyService.save(vacancy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vacancy.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vacancies} : get all the vacancies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vacancies in body.
     */
    @GetMapping("/vacancies")
    public ResponseEntity<List<Vacancy>> getAllVacancies(VacancyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vacancies by criteria: {}", criteria);
        Page<Vacancy> page = vacancyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vacancies/count} : count all the vacancies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vacancies/count")
    public ResponseEntity<Long> countVacancies(VacancyCriteria criteria) {
        log.debug("REST request to count Vacancies by criteria: {}", criteria);
        return ResponseEntity.ok().body(vacancyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vacancies/:id} : get the "id" vacancy.
     *
     * @param id the id of the vacancy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vacancy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vacancies/{id}")
    public ResponseEntity<Vacancy> getVacancy(@PathVariable Long id) {
        log.debug("REST request to get Vacancy : {}", id);
        Optional<Vacancy> vacancy = vacancyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vacancy);
    }

    /**
     * {@code DELETE  /vacancies/:id} : delete the "id" vacancy.
     *
     * @param id the id of the vacancy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vacancies/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        log.debug("REST request to delete Vacancy : {}", id);
        vacancyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
