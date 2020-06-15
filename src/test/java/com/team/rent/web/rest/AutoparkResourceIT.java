package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.Autopark;
import com.team.rent.domain.Car;
import com.team.rent.domain.RentalPoint;
import com.team.rent.repository.AutoparkRepository;
import com.team.rent.service.AutoparkService;
import com.team.rent.service.dto.AutoparkCriteria;
import com.team.rent.service.AutoparkQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AutoparkResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class AutoparkResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVAILABLE_CARS = 1;
    private static final Integer UPDATED_AVAILABLE_CARS = 2;
    private static final Integer SMALLER_AVAILABLE_CARS = 1 - 1;

    @Autowired
    private AutoparkRepository autoparkRepository;

    @Autowired
    private AutoparkService autoparkService;

    @Autowired
    private AutoparkQueryService autoparkQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutoparkMockMvc;

    private Autopark autopark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autopark createEntity(EntityManager em) {
        Autopark autopark = new Autopark()
            .location(DEFAULT_LOCATION)
            .availableCars(DEFAULT_AVAILABLE_CARS);
        return autopark;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autopark createUpdatedEntity(EntityManager em) {
        Autopark autopark = new Autopark()
            .location(UPDATED_LOCATION)
            .availableCars(UPDATED_AVAILABLE_CARS);
        return autopark;
    }

    @BeforeEach
    public void initTest() {
        autopark = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutopark() throws Exception {
        int databaseSizeBeforeCreate = autoparkRepository.findAll().size();

        // Create the Autopark
        restAutoparkMockMvc.perform(post("/api/autoparks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(autopark)))
            .andExpect(status().isCreated());

        // Validate the Autopark in the database
        List<Autopark> autoparkList = autoparkRepository.findAll();
        assertThat(autoparkList).hasSize(databaseSizeBeforeCreate + 1);
        Autopark testAutopark = autoparkList.get(autoparkList.size() - 1);
        assertThat(testAutopark.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testAutopark.getAvailableCars()).isEqualTo(DEFAULT_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void createAutoparkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoparkRepository.findAll().size();

        // Create the Autopark with an existing ID
        autopark.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoparkMockMvc.perform(post("/api/autoparks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(autopark)))
            .andExpect(status().isBadRequest());

        // Validate the Autopark in the database
        List<Autopark> autoparkList = autoparkRepository.findAll();
        assertThat(autoparkList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAutoparks() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList
        restAutoparkMockMvc.perform(get("/api/autoparks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autopark.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].availableCars").value(hasItem(DEFAULT_AVAILABLE_CARS)));
    }
    
    @Test
    @Transactional
    public void getAutopark() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get the autopark
        restAutoparkMockMvc.perform(get("/api/autoparks/{id}", autopark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autopark.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.availableCars").value(DEFAULT_AVAILABLE_CARS));
    }


    @Test
    @Transactional
    public void getAutoparksByIdFiltering() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        Long id = autopark.getId();

        defaultAutoparkShouldBeFound("id.equals=" + id);
        defaultAutoparkShouldNotBeFound("id.notEquals=" + id);

        defaultAutoparkShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAutoparkShouldNotBeFound("id.greaterThan=" + id);

        defaultAutoparkShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAutoparkShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAutoparksByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where location equals to DEFAULT_LOCATION
        defaultAutoparkShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the autoparkList where location equals to UPDATED_LOCATION
        defaultAutoparkShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllAutoparksByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where location not equals to DEFAULT_LOCATION
        defaultAutoparkShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the autoparkList where location not equals to UPDATED_LOCATION
        defaultAutoparkShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllAutoparksByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultAutoparkShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the autoparkList where location equals to UPDATED_LOCATION
        defaultAutoparkShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllAutoparksByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where location is not null
        defaultAutoparkShouldBeFound("location.specified=true");

        // Get all the autoparkList where location is null
        defaultAutoparkShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllAutoparksByLocationContainsSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where location contains DEFAULT_LOCATION
        defaultAutoparkShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the autoparkList where location contains UPDATED_LOCATION
        defaultAutoparkShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllAutoparksByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where location does not contain DEFAULT_LOCATION
        defaultAutoparkShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the autoparkList where location does not contain UPDATED_LOCATION
        defaultAutoparkShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars equals to DEFAULT_AVAILABLE_CARS
        defaultAutoparkShouldBeFound("availableCars.equals=" + DEFAULT_AVAILABLE_CARS);

        // Get all the autoparkList where availableCars equals to UPDATED_AVAILABLE_CARS
        defaultAutoparkShouldNotBeFound("availableCars.equals=" + UPDATED_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars not equals to DEFAULT_AVAILABLE_CARS
        defaultAutoparkShouldNotBeFound("availableCars.notEquals=" + DEFAULT_AVAILABLE_CARS);

        // Get all the autoparkList where availableCars not equals to UPDATED_AVAILABLE_CARS
        defaultAutoparkShouldBeFound("availableCars.notEquals=" + UPDATED_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsInShouldWork() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars in DEFAULT_AVAILABLE_CARS or UPDATED_AVAILABLE_CARS
        defaultAutoparkShouldBeFound("availableCars.in=" + DEFAULT_AVAILABLE_CARS + "," + UPDATED_AVAILABLE_CARS);

        // Get all the autoparkList where availableCars equals to UPDATED_AVAILABLE_CARS
        defaultAutoparkShouldNotBeFound("availableCars.in=" + UPDATED_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars is not null
        defaultAutoparkShouldBeFound("availableCars.specified=true");

        // Get all the autoparkList where availableCars is null
        defaultAutoparkShouldNotBeFound("availableCars.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars is greater than or equal to DEFAULT_AVAILABLE_CARS
        defaultAutoparkShouldBeFound("availableCars.greaterThanOrEqual=" + DEFAULT_AVAILABLE_CARS);

        // Get all the autoparkList where availableCars is greater than or equal to UPDATED_AVAILABLE_CARS
        defaultAutoparkShouldNotBeFound("availableCars.greaterThanOrEqual=" + UPDATED_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars is less than or equal to DEFAULT_AVAILABLE_CARS
        defaultAutoparkShouldBeFound("availableCars.lessThanOrEqual=" + DEFAULT_AVAILABLE_CARS);

        // Get all the autoparkList where availableCars is less than or equal to SMALLER_AVAILABLE_CARS
        defaultAutoparkShouldNotBeFound("availableCars.lessThanOrEqual=" + SMALLER_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsLessThanSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars is less than DEFAULT_AVAILABLE_CARS
        defaultAutoparkShouldNotBeFound("availableCars.lessThan=" + DEFAULT_AVAILABLE_CARS);

        // Get all the autoparkList where availableCars is less than UPDATED_AVAILABLE_CARS
        defaultAutoparkShouldBeFound("availableCars.lessThan=" + UPDATED_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void getAllAutoparksByAvailableCarsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);

        // Get all the autoparkList where availableCars is greater than DEFAULT_AVAILABLE_CARS
        defaultAutoparkShouldNotBeFound("availableCars.greaterThan=" + DEFAULT_AVAILABLE_CARS);

        // Get all the autoparkList where availableCars is greater than SMALLER_AVAILABLE_CARS
        defaultAutoparkShouldBeFound("availableCars.greaterThan=" + SMALLER_AVAILABLE_CARS);
    }


    @Test
    @Transactional
    public void getAllAutoparksByCarsIsEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);
        Car cars = CarResourceIT.createEntity(em);
        em.persist(cars);
        em.flush();
        autopark.addCars(cars);
        autoparkRepository.saveAndFlush(autopark);
        Long carsId = cars.getId();

        // Get all the autoparkList where cars equals to carsId
        defaultAutoparkShouldBeFound("carsId.equals=" + carsId);

        // Get all the autoparkList where cars equals to carsId + 1
        defaultAutoparkShouldNotBeFound("carsId.equals=" + (carsId + 1));
    }


    @Test
    @Transactional
    public void getAllAutoparksByRentalPointIsEqualToSomething() throws Exception {
        // Initialize the database
        autoparkRepository.saveAndFlush(autopark);
        RentalPoint rentalPoint = RentalPointResourceIT.createEntity(em);
        em.persist(rentalPoint);
        em.flush();
        autopark.setRentalPoint(rentalPoint);
        rentalPoint.setAutopark(autopark);
        autoparkRepository.saveAndFlush(autopark);
        Long rentalPointId = rentalPoint.getId();

        // Get all the autoparkList where rentalPoint equals to rentalPointId
        defaultAutoparkShouldBeFound("rentalPointId.equals=" + rentalPointId);

        // Get all the autoparkList where rentalPoint equals to rentalPointId + 1
        defaultAutoparkShouldNotBeFound("rentalPointId.equals=" + (rentalPointId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoparkShouldBeFound(String filter) throws Exception {
        restAutoparkMockMvc.perform(get("/api/autoparks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autopark.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].availableCars").value(hasItem(DEFAULT_AVAILABLE_CARS)));

        // Check, that the count call also returns 1
        restAutoparkMockMvc.perform(get("/api/autoparks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoparkShouldNotBeFound(String filter) throws Exception {
        restAutoparkMockMvc.perform(get("/api/autoparks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoparkMockMvc.perform(get("/api/autoparks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAutopark() throws Exception {
        // Get the autopark
        restAutoparkMockMvc.perform(get("/api/autoparks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutopark() throws Exception {
        // Initialize the database
        autoparkService.save(autopark);

        int databaseSizeBeforeUpdate = autoparkRepository.findAll().size();

        // Update the autopark
        Autopark updatedAutopark = autoparkRepository.findById(autopark.getId()).get();
        // Disconnect from session so that the updates on updatedAutopark are not directly saved in db
        em.detach(updatedAutopark);
        updatedAutopark
            .location(UPDATED_LOCATION)
            .availableCars(UPDATED_AVAILABLE_CARS);

        restAutoparkMockMvc.perform(put("/api/autoparks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAutopark)))
            .andExpect(status().isOk());

        // Validate the Autopark in the database
        List<Autopark> autoparkList = autoparkRepository.findAll();
        assertThat(autoparkList).hasSize(databaseSizeBeforeUpdate);
        Autopark testAutopark = autoparkList.get(autoparkList.size() - 1);
        assertThat(testAutopark.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testAutopark.getAvailableCars()).isEqualTo(UPDATED_AVAILABLE_CARS);
    }

    @Test
    @Transactional
    public void updateNonExistingAutopark() throws Exception {
        int databaseSizeBeforeUpdate = autoparkRepository.findAll().size();

        // Create the Autopark

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoparkMockMvc.perform(put("/api/autoparks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(autopark)))
            .andExpect(status().isBadRequest());

        // Validate the Autopark in the database
        List<Autopark> autoparkList = autoparkRepository.findAll();
        assertThat(autoparkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutopark() throws Exception {
        // Initialize the database
        autoparkService.save(autopark);

        int databaseSizeBeforeDelete = autoparkRepository.findAll().size();

        // Delete the autopark
        restAutoparkMockMvc.perform(delete("/api/autoparks/{id}", autopark.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Autopark> autoparkList = autoparkRepository.findAll();
        assertThat(autoparkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
