package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.VacancyStatus;
import com.prychko.lab3.repository.VacancyStatusRepository;
import com.prychko.lab3.service.VacancyStatusService;

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
 * Integration tests for the {@link VacancyStatusResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)

@AutoConfigureMockMvc
@WithMockUser
public class VacancyStatusResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private VacancyStatusRepository vacancyStatusRepository;

    @Autowired
    private VacancyStatusService vacancyStatusService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVacancyStatusMockMvc;

    private VacancyStatus vacancyStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VacancyStatus createEntity(EntityManager em) {
        VacancyStatus vacancyStatus = new VacancyStatus()
            .status(DEFAULT_STATUS);
        return vacancyStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VacancyStatus createUpdatedEntity(EntityManager em) {
        VacancyStatus vacancyStatus = new VacancyStatus()
            .status(UPDATED_STATUS);
        return vacancyStatus;
    }

    @BeforeEach
    public void initTest() {
        vacancyStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacancyStatus() throws Exception {
        int databaseSizeBeforeCreate = vacancyStatusRepository.findAll().size();

        // Create the VacancyStatus
        restVacancyStatusMockMvc.perform(post("/api/vacancy-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancyStatus)))
            .andExpect(status().isCreated());

        // Validate the VacancyStatus in the database
        List<VacancyStatus> vacancyStatusList = vacancyStatusRepository.findAll();
        assertThat(vacancyStatusList).hasSize(databaseSizeBeforeCreate + 1);
        VacancyStatus testVacancyStatus = vacancyStatusList.get(vacancyStatusList.size() - 1);
        assertThat(testVacancyStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createVacancyStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vacancyStatusRepository.findAll().size();

        // Create the VacancyStatus with an existing ID
        vacancyStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacancyStatusMockMvc.perform(post("/api/vacancy-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancyStatus)))
            .andExpect(status().isBadRequest());

        // Validate the VacancyStatus in the database
        List<VacancyStatus> vacancyStatusList = vacancyStatusRepository.findAll();
        assertThat(vacancyStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacancyStatusRepository.findAll().size();
        // set the field null
        vacancyStatus.setStatus(null);

        // Create the VacancyStatus, which fails.

        restVacancyStatusMockMvc.perform(post("/api/vacancy-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancyStatus)))
            .andExpect(status().isBadRequest());

        List<VacancyStatus> vacancyStatusList = vacancyStatusRepository.findAll();
        assertThat(vacancyStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVacancyStatuses() throws Exception {
        // Initialize the database
        vacancyStatusRepository.saveAndFlush(vacancyStatus);

        // Get all the vacancyStatusList
        restVacancyStatusMockMvc.perform(get("/api/vacancy-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacancyStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getVacancyStatus() throws Exception {
        // Initialize the database
        vacancyStatusRepository.saveAndFlush(vacancyStatus);

        // Get the vacancyStatus
        restVacancyStatusMockMvc.perform(get("/api/vacancy-statuses/{id}", vacancyStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vacancyStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingVacancyStatus() throws Exception {
        // Get the vacancyStatus
        restVacancyStatusMockMvc.perform(get("/api/vacancy-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacancyStatus() throws Exception {
        // Initialize the database
        vacancyStatusService.save(vacancyStatus);

        int databaseSizeBeforeUpdate = vacancyStatusRepository.findAll().size();

        // Update the vacancyStatus
        VacancyStatus updatedVacancyStatus = vacancyStatusRepository.findById(vacancyStatus.getId()).get();
        // Disconnect from session so that the updates on updatedVacancyStatus are not directly saved in db
        em.detach(updatedVacancyStatus);
        updatedVacancyStatus
            .status(UPDATED_STATUS);

        restVacancyStatusMockMvc.perform(put("/api/vacancy-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVacancyStatus)))
            .andExpect(status().isOk());

        // Validate the VacancyStatus in the database
        List<VacancyStatus> vacancyStatusList = vacancyStatusRepository.findAll();
        assertThat(vacancyStatusList).hasSize(databaseSizeBeforeUpdate);
        VacancyStatus testVacancyStatus = vacancyStatusList.get(vacancyStatusList.size() - 1);
        assertThat(testVacancyStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingVacancyStatus() throws Exception {
        int databaseSizeBeforeUpdate = vacancyStatusRepository.findAll().size();

        // Create the VacancyStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVacancyStatusMockMvc.perform(put("/api/vacancy-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancyStatus)))
            .andExpect(status().isBadRequest());

        // Validate the VacancyStatus in the database
        List<VacancyStatus> vacancyStatusList = vacancyStatusRepository.findAll();
        assertThat(vacancyStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVacancyStatus() throws Exception {
        // Initialize the database
        vacancyStatusService.save(vacancyStatus);

        int databaseSizeBeforeDelete = vacancyStatusRepository.findAll().size();

        // Delete the vacancyStatus
        restVacancyStatusMockMvc.perform(delete("/api/vacancy-statuses/{id}", vacancyStatus.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VacancyStatus> vacancyStatusList = vacancyStatusRepository.findAll();
        assertThat(vacancyStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
