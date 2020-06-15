package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.InterviewType;
import com.prychko.lab3.repository.InterviewTypeRepository;
import com.prychko.lab3.service.InterviewTypeService;

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
 * Integration tests for the {@link InterviewTypeResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)

@AutoConfigureMockMvc
@WithMockUser
public class InterviewTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private InterviewTypeRepository interviewTypeRepository;

    @Autowired
    private InterviewTypeService interviewTypeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterviewTypeMockMvc;

    private InterviewType interviewType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewType createEntity(EntityManager em) {
        InterviewType interviewType = new InterviewType()
            .type(DEFAULT_TYPE);
        return interviewType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewType createUpdatedEntity(EntityManager em) {
        InterviewType interviewType = new InterviewType()
            .type(UPDATED_TYPE);
        return interviewType;
    }

    @BeforeEach
    public void initTest() {
        interviewType = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterviewType() throws Exception {
        int databaseSizeBeforeCreate = interviewTypeRepository.findAll().size();

        // Create the InterviewType
        restInterviewTypeMockMvc.perform(post("/api/interview-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewType)))
            .andExpect(status().isCreated());

        // Validate the InterviewType in the database
        List<InterviewType> interviewTypeList = interviewTypeRepository.findAll();
        assertThat(interviewTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InterviewType testInterviewType = interviewTypeList.get(interviewTypeList.size() - 1);
        assertThat(testInterviewType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createInterviewTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interviewTypeRepository.findAll().size();

        // Create the InterviewType with an existing ID
        interviewType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewTypeMockMvc.perform(post("/api/interview-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewType)))
            .andExpect(status().isBadRequest());

        // Validate the InterviewType in the database
        List<InterviewType> interviewTypeList = interviewTypeRepository.findAll();
        assertThat(interviewTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = interviewTypeRepository.findAll().size();
        // set the field null
        interviewType.setType(null);

        // Create the InterviewType, which fails.

        restInterviewTypeMockMvc.perform(post("/api/interview-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewType)))
            .andExpect(status().isBadRequest());

        List<InterviewType> interviewTypeList = interviewTypeRepository.findAll();
        assertThat(interviewTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInterviewTypes() throws Exception {
        // Initialize the database
        interviewTypeRepository.saveAndFlush(interviewType);

        // Get all the interviewTypeList
        restInterviewTypeMockMvc.perform(get("/api/interview-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getInterviewType() throws Exception {
        // Initialize the database
        interviewTypeRepository.saveAndFlush(interviewType);

        // Get the interviewType
        restInterviewTypeMockMvc.perform(get("/api/interview-types/{id}", interviewType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interviewType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingInterviewType() throws Exception {
        // Get the interviewType
        restInterviewTypeMockMvc.perform(get("/api/interview-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterviewType() throws Exception {
        // Initialize the database
        interviewTypeService.save(interviewType);

        int databaseSizeBeforeUpdate = interviewTypeRepository.findAll().size();

        // Update the interviewType
        InterviewType updatedInterviewType = interviewTypeRepository.findById(interviewType.getId()).get();
        // Disconnect from session so that the updates on updatedInterviewType are not directly saved in db
        em.detach(updatedInterviewType);
        updatedInterviewType
            .type(UPDATED_TYPE);

        restInterviewTypeMockMvc.perform(put("/api/interview-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInterviewType)))
            .andExpect(status().isOk());

        // Validate the InterviewType in the database
        List<InterviewType> interviewTypeList = interviewTypeRepository.findAll();
        assertThat(interviewTypeList).hasSize(databaseSizeBeforeUpdate);
        InterviewType testInterviewType = interviewTypeList.get(interviewTypeList.size() - 1);
        assertThat(testInterviewType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingInterviewType() throws Exception {
        int databaseSizeBeforeUpdate = interviewTypeRepository.findAll().size();

        // Create the InterviewType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewTypeMockMvc.perform(put("/api/interview-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewType)))
            .andExpect(status().isBadRequest());

        // Validate the InterviewType in the database
        List<InterviewType> interviewTypeList = interviewTypeRepository.findAll();
        assertThat(interviewTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInterviewType() throws Exception {
        // Initialize the database
        interviewTypeService.save(interviewType);

        int databaseSizeBeforeDelete = interviewTypeRepository.findAll().size();

        // Delete the interviewType
        restInterviewTypeMockMvc.perform(delete("/api/interview-types/{id}", interviewType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterviewType> interviewTypeList = interviewTypeRepository.findAll();
        assertThat(interviewTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
