package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.ApplicationStatus;
import com.prychko.lab3.repository.ApplicationStatusRepository;
import com.prychko.lab3.service.ApplicationStatusService;

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
 * Integration tests for the {@link ApplicationStatusResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)

@AutoConfigureMockMvc
@WithMockUser
public class ApplicationStatusResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Autowired
    private ApplicationStatusService applicationStatusService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationStatusMockMvc;

    private ApplicationStatus applicationStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationStatus createEntity(EntityManager em) {
        ApplicationStatus applicationStatus = new ApplicationStatus()
            .status(DEFAULT_STATUS);
        return applicationStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationStatus createUpdatedEntity(EntityManager em) {
        ApplicationStatus applicationStatus = new ApplicationStatus()
            .status(UPDATED_STATUS);
        return applicationStatus;
    }

    @BeforeEach
    public void initTest() {
        applicationStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationStatus() throws Exception {
        int databaseSizeBeforeCreate = applicationStatusRepository.findAll().size();

        // Create the ApplicationStatus
        restApplicationStatusMockMvc.perform(post("/api/application-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationStatus)))
            .andExpect(status().isCreated());

        // Validate the ApplicationStatus in the database
        List<ApplicationStatus> applicationStatusList = applicationStatusRepository.findAll();
        assertThat(applicationStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationStatus testApplicationStatus = applicationStatusList.get(applicationStatusList.size() - 1);
        assertThat(testApplicationStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createApplicationStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationStatusRepository.findAll().size();

        // Create the ApplicationStatus with an existing ID
        applicationStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationStatusMockMvc.perform(post("/api/application-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationStatus)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationStatus in the database
        List<ApplicationStatus> applicationStatusList = applicationStatusRepository.findAll();
        assertThat(applicationStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationStatusRepository.findAll().size();
        // set the field null
        applicationStatus.setStatus(null);

        // Create the ApplicationStatus, which fails.

        restApplicationStatusMockMvc.perform(post("/api/application-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationStatus)))
            .andExpect(status().isBadRequest());

        List<ApplicationStatus> applicationStatusList = applicationStatusRepository.findAll();
        assertThat(applicationStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationStatuses() throws Exception {
        // Initialize the database
        applicationStatusRepository.saveAndFlush(applicationStatus);

        // Get all the applicationStatusList
        restApplicationStatusMockMvc.perform(get("/api/application-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getApplicationStatus() throws Exception {
        // Initialize the database
        applicationStatusRepository.saveAndFlush(applicationStatus);

        // Get the applicationStatus
        restApplicationStatusMockMvc.perform(get("/api/application-statuses/{id}", applicationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationStatus() throws Exception {
        // Get the applicationStatus
        restApplicationStatusMockMvc.perform(get("/api/application-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationStatus() throws Exception {
        // Initialize the database
        applicationStatusService.save(applicationStatus);

        int databaseSizeBeforeUpdate = applicationStatusRepository.findAll().size();

        // Update the applicationStatus
        ApplicationStatus updatedApplicationStatus = applicationStatusRepository.findById(applicationStatus.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationStatus are not directly saved in db
        em.detach(updatedApplicationStatus);
        updatedApplicationStatus
            .status(UPDATED_STATUS);

        restApplicationStatusMockMvc.perform(put("/api/application-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplicationStatus)))
            .andExpect(status().isOk());

        // Validate the ApplicationStatus in the database
        List<ApplicationStatus> applicationStatusList = applicationStatusRepository.findAll();
        assertThat(applicationStatusList).hasSize(databaseSizeBeforeUpdate);
        ApplicationStatus testApplicationStatus = applicationStatusList.get(applicationStatusList.size() - 1);
        assertThat(testApplicationStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = applicationStatusRepository.findAll().size();

        // Create the ApplicationStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationStatusMockMvc.perform(put("/api/application-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationStatus)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationStatus in the database
        List<ApplicationStatus> applicationStatusList = applicationStatusRepository.findAll();
        assertThat(applicationStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplicationStatus() throws Exception {
        // Initialize the database
        applicationStatusService.save(applicationStatus);

        int databaseSizeBeforeDelete = applicationStatusRepository.findAll().size();

        // Delete the applicationStatus
        restApplicationStatusMockMvc.perform(delete("/api/application-statuses/{id}", applicationStatus.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationStatus> applicationStatusList = applicationStatusRepository.findAll();
        assertThat(applicationStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
