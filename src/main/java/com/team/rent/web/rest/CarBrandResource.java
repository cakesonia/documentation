package com.team.rent.web.rest;

import com.team.rent.domain.CarBrand;
import com.team.rent.service.CarBrandService;
import com.team.rent.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.team.rent.domain.CarBrand}.
 */
@RestController
@RequestMapping("/api")
public class CarBrandResource {

    private final Logger log = LoggerFactory.getLogger(CarBrandResource.class);

    private static final String ENTITY_NAME = "carBrand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarBrandService carBrandService;

    public CarBrandResource(CarBrandService carBrandService) {
        this.carBrandService = carBrandService;
    }

    /**
     * {@code POST  /car-brands} : Create a new carBrand.
     *
     * @param carBrand the carBrand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carBrand, or with status {@code 400 (Bad Request)} if the carBrand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/car-brands")
    public ResponseEntity<CarBrand> createCarBrand(@RequestBody CarBrand carBrand) throws URISyntaxException {
        log.debug("REST request to save CarBrand : {}", carBrand);
        if (carBrand.getId() != null) {
            throw new BadRequestAlertException("A new carBrand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarBrand result = carBrandService.save(carBrand);
        return ResponseEntity.created(new URI("/api/car-brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /car-brands} : Updates an existing carBrand.
     *
     * @param carBrand the carBrand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carBrand,
     * or with status {@code 400 (Bad Request)} if the carBrand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carBrand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/car-brands")
    public ResponseEntity<CarBrand> updateCarBrand(@RequestBody CarBrand carBrand) throws URISyntaxException {
        log.debug("REST request to update CarBrand : {}", carBrand);
        if (carBrand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CarBrand result = carBrandService.save(carBrand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carBrand.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /car-brands} : get all the carBrands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carBrands in body.
     */
    @GetMapping("/car-brands")
    public List<CarBrand> getAllCarBrands() {
        log.debug("REST request to get all CarBrands");
        return carBrandService.findAll();
    }

    /**
     * {@code GET  /car-brands/:id} : get the "id" carBrand.
     *
     * @param id the id of the carBrand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carBrand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/car-brands/{id}")
    public ResponseEntity<CarBrand> getCarBrand(@PathVariable Long id) {
        log.debug("REST request to get CarBrand : {}", id);
        Optional<CarBrand> carBrand = carBrandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carBrand);
    }

    /**
     * {@code DELETE  /car-brands/:id} : delete the "id" carBrand.
     *
     * @param id the id of the carBrand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/car-brands/{id}")
    public ResponseEntity<Void> deleteCarBrand(@PathVariable Long id) {
        log.debug("REST request to delete CarBrand : {}", id);
        carBrandService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
