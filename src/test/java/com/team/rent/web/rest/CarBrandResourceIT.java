package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.CarBrand;
import com.team.rent.repository.CarBrandRepository;
import com.team.rent.service.CarBrandService;

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
 * Integration tests for the {@link CarBrandResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CarBrandResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarBrandService carBrandService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarBrandMockMvc;

    private CarBrand carBrand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarBrand createEntity(EntityManager em) {
        CarBrand carBrand = new CarBrand()
            .status(DEFAULT_STATUS);
        return carBrand;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarBrand createUpdatedEntity(EntityManager em) {
        CarBrand carBrand = new CarBrand()
            .status(UPDATED_STATUS);
        return carBrand;
    }

    @BeforeEach
    public void initTest() {
        carBrand = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarBrand() throws Exception {
        int databaseSizeBeforeCreate = carBrandRepository.findAll().size();

        // Create the CarBrand
        restCarBrandMockMvc.perform(post("/api/car-brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carBrand)))
            .andExpect(status().isCreated());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeCreate + 1);
        CarBrand testCarBrand = carBrandList.get(carBrandList.size() - 1);
        assertThat(testCarBrand.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCarBrandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carBrandRepository.findAll().size();

        // Create the CarBrand with an existing ID
        carBrand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarBrandMockMvc.perform(post("/api/car-brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carBrand)))
            .andExpect(status().isBadRequest());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCarBrands() throws Exception {
        // Initialize the database
        carBrandRepository.saveAndFlush(carBrand);

        // Get all the carBrandList
        restCarBrandMockMvc.perform(get("/api/car-brands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getCarBrand() throws Exception {
        // Initialize the database
        carBrandRepository.saveAndFlush(carBrand);

        // Get the carBrand
        restCarBrandMockMvc.perform(get("/api/car-brands/{id}", carBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carBrand.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingCarBrand() throws Exception {
        // Get the carBrand
        restCarBrandMockMvc.perform(get("/api/car-brands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarBrand() throws Exception {
        // Initialize the database
        carBrandService.save(carBrand);

        int databaseSizeBeforeUpdate = carBrandRepository.findAll().size();

        // Update the carBrand
        CarBrand updatedCarBrand = carBrandRepository.findById(carBrand.getId()).get();
        // Disconnect from session so that the updates on updatedCarBrand are not directly saved in db
        em.detach(updatedCarBrand);
        updatedCarBrand
            .status(UPDATED_STATUS);

        restCarBrandMockMvc.perform(put("/api/car-brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCarBrand)))
            .andExpect(status().isOk());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeUpdate);
        CarBrand testCarBrand = carBrandList.get(carBrandList.size() - 1);
        assertThat(testCarBrand.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCarBrand() throws Exception {
        int databaseSizeBeforeUpdate = carBrandRepository.findAll().size();

        // Create the CarBrand

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarBrandMockMvc.perform(put("/api/car-brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carBrand)))
            .andExpect(status().isBadRequest());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarBrand() throws Exception {
        // Initialize the database
        carBrandService.save(carBrand);

        int databaseSizeBeforeDelete = carBrandRepository.findAll().size();

        // Delete the carBrand
        restCarBrandMockMvc.perform(delete("/api/car-brands/{id}", carBrand.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
