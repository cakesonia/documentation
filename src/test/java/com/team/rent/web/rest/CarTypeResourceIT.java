package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.CarType;
import com.team.rent.repository.CarTypeRepository;
import com.team.rent.service.CarTypeService;

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
 * Integration tests for the {@link CarTypeResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CarTypeResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private CarTypeService carTypeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarTypeMockMvc;

    private CarType carType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarType createEntity(EntityManager em) {
        CarType carType = new CarType()
            .status(DEFAULT_STATUS);
        return carType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarType createUpdatedEntity(EntityManager em) {
        CarType carType = new CarType()
            .status(UPDATED_STATUS);
        return carType;
    }

    @BeforeEach
    public void initTest() {
        carType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarType() throws Exception {
        int databaseSizeBeforeCreate = carTypeRepository.findAll().size();

        // Create the CarType
        restCarTypeMockMvc.perform(post("/api/car-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carType)))
            .andExpect(status().isCreated());

        // Validate the CarType in the database
        List<CarType> carTypeList = carTypeRepository.findAll();
        assertThat(carTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CarType testCarType = carTypeList.get(carTypeList.size() - 1);
        assertThat(testCarType.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCarTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carTypeRepository.findAll().size();

        // Create the CarType with an existing ID
        carType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarTypeMockMvc.perform(post("/api/car-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carType)))
            .andExpect(status().isBadRequest());

        // Validate the CarType in the database
        List<CarType> carTypeList = carTypeRepository.findAll();
        assertThat(carTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCarTypes() throws Exception {
        // Initialize the database
        carTypeRepository.saveAndFlush(carType);

        // Get all the carTypeList
        restCarTypeMockMvc.perform(get("/api/car-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carType.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getCarType() throws Exception {
        // Initialize the database
        carTypeRepository.saveAndFlush(carType);

        // Get the carType
        restCarTypeMockMvc.perform(get("/api/car-types/{id}", carType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carType.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingCarType() throws Exception {
        // Get the carType
        restCarTypeMockMvc.perform(get("/api/car-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarType() throws Exception {
        // Initialize the database
        carTypeService.save(carType);

        int databaseSizeBeforeUpdate = carTypeRepository.findAll().size();

        // Update the carType
        CarType updatedCarType = carTypeRepository.findById(carType.getId()).get();
        // Disconnect from session so that the updates on updatedCarType are not directly saved in db
        em.detach(updatedCarType);
        updatedCarType
            .status(UPDATED_STATUS);

        restCarTypeMockMvc.perform(put("/api/car-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCarType)))
            .andExpect(status().isOk());

        // Validate the CarType in the database
        List<CarType> carTypeList = carTypeRepository.findAll();
        assertThat(carTypeList).hasSize(databaseSizeBeforeUpdate);
        CarType testCarType = carTypeList.get(carTypeList.size() - 1);
        assertThat(testCarType.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCarType() throws Exception {
        int databaseSizeBeforeUpdate = carTypeRepository.findAll().size();

        // Create the CarType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarTypeMockMvc.perform(put("/api/car-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carType)))
            .andExpect(status().isBadRequest());

        // Validate the CarType in the database
        List<CarType> carTypeList = carTypeRepository.findAll();
        assertThat(carTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarType() throws Exception {
        // Initialize the database
        carTypeService.save(carType);

        int databaseSizeBeforeDelete = carTypeRepository.findAll().size();

        // Delete the carType
        restCarTypeMockMvc.perform(delete("/api/car-types/{id}", carType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarType> carTypeList = carTypeRepository.findAll();
        assertThat(carTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
