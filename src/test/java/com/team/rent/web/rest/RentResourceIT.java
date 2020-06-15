package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.Rent;
import com.team.rent.repository.RentRepository;
import com.team.rent.service.RentService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.team.rent.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RentResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class RentResourceIT {

    private static final ZonedDateTime DEFAULT_LENDING_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LENDING_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_RETURNING_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RETURNING_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_RENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_RENT_STATUS = "BBBBBBBBBB";

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private RentService rentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentMockMvc;

    private Rent rent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rent createEntity(EntityManager em) {
        Rent rent = new Rent()
            .lendingDate(DEFAULT_LENDING_DATE)
            .returningDate(DEFAULT_RETURNING_DATE)
            .rentStatus(DEFAULT_RENT_STATUS);
        return rent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rent createUpdatedEntity(EntityManager em) {
        Rent rent = new Rent()
            .lendingDate(UPDATED_LENDING_DATE)
            .returningDate(UPDATED_RETURNING_DATE)
            .rentStatus(UPDATED_RENT_STATUS);
        return rent;
    }

    @BeforeEach
    public void initTest() {
        rent = createEntity(em);
    }

    @Test
    @Transactional
    public void createRent() throws Exception {
        int databaseSizeBeforeCreate = rentRepository.findAll().size();

        // Create the Rent
        restRentMockMvc.perform(post("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isCreated());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeCreate + 1);
        Rent testRent = rentList.get(rentList.size() - 1);
        assertThat(testRent.getLendingDate()).isEqualTo(DEFAULT_LENDING_DATE);
        assertThat(testRent.getReturningDate()).isEqualTo(DEFAULT_RETURNING_DATE);
        assertThat(testRent.getRentStatus()).isEqualTo(DEFAULT_RENT_STATUS);
    }

    @Test
    @Transactional
    public void createRentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rentRepository.findAll().size();

        // Create the Rent with an existing ID
        rent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentMockMvc.perform(post("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isBadRequest());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRents() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList
        restRentMockMvc.perform(get("/api/rents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rent.getId().intValue())))
            .andExpect(jsonPath("$.[*].lendingDate").value(hasItem(sameInstant(DEFAULT_LENDING_DATE))))
            .andExpect(jsonPath("$.[*].returningDate").value(hasItem(sameInstant(DEFAULT_RETURNING_DATE))))
            .andExpect(jsonPath("$.[*].rentStatus").value(hasItem(DEFAULT_RENT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getRent() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get the rent
        restRentMockMvc.perform(get("/api/rents/{id}", rent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rent.getId().intValue()))
            .andExpect(jsonPath("$.lendingDate").value(sameInstant(DEFAULT_LENDING_DATE)))
            .andExpect(jsonPath("$.returningDate").value(sameInstant(DEFAULT_RETURNING_DATE)))
            .andExpect(jsonPath("$.rentStatus").value(DEFAULT_RENT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingRent() throws Exception {
        // Get the rent
        restRentMockMvc.perform(get("/api/rents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRent() throws Exception {
        // Initialize the database
        rentService.save(rent);

        int databaseSizeBeforeUpdate = rentRepository.findAll().size();

        // Update the rent
        Rent updatedRent = rentRepository.findById(rent.getId()).get();
        // Disconnect from session so that the updates on updatedRent are not directly saved in db
        em.detach(updatedRent);
        updatedRent
            .lendingDate(UPDATED_LENDING_DATE)
            .returningDate(UPDATED_RETURNING_DATE)
            .rentStatus(UPDATED_RENT_STATUS);

        restRentMockMvc.perform(put("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRent)))
            .andExpect(status().isOk());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeUpdate);
        Rent testRent = rentList.get(rentList.size() - 1);
        assertThat(testRent.getLendingDate()).isEqualTo(UPDATED_LENDING_DATE);
        assertThat(testRent.getReturningDate()).isEqualTo(UPDATED_RETURNING_DATE);
        assertThat(testRent.getRentStatus()).isEqualTo(UPDATED_RENT_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRent() throws Exception {
        int databaseSizeBeforeUpdate = rentRepository.findAll().size();

        // Create the Rent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentMockMvc.perform(put("/api/rents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isBadRequest());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRent() throws Exception {
        // Initialize the database
        rentService.save(rent);

        int databaseSizeBeforeDelete = rentRepository.findAll().size();

        // Delete the rent
        restRentMockMvc.perform(delete("/api/rents/{id}", rent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
