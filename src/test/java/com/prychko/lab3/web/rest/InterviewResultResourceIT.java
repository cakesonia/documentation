package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.InterviewResult;
import com.prychko.lab3.repository.InterviewResultRepository;
import com.prychko.lab3.service.InterviewResultService;

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
 * Integration tests for the {@link InterviewResultResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)

@AutoConfigureMockMvc
@WithMockUser
public class InterviewResultResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private InterviewResultRepository interviewResultRepository;

    @Autowired
    private InterviewResultService interviewResultService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterviewResultMockMvc;

    private InterviewResult interviewResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewResult createEntity(EntityManager em) {
        InterviewResult interviewResult = new InterviewResult()
            .description(DEFAULT_DESCRIPTION);
        return interviewResult;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewResult createUpdatedEntity(EntityManager em) {
        InterviewResult interviewResult = new InterviewResult()
            .description(UPDATED_DESCRIPTION);
        return interviewResult;
    }

    @BeforeEach
    public void initTest() {
        interviewResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterviewResult() throws Exception {
        int databaseSizeBeforeCreate = interviewResultRepository.findAll().size();

        // Create the InterviewResult
        restInterviewResultMockMvc.perform(post("/api/interview-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewResult)))
            .andExpect(status().isCreated());

        // Validate the InterviewResult in the database
        List<InterviewResult> interviewResultList = interviewResultRepository.findAll();
        assertThat(interviewResultList).hasSize(databaseSizeBeforeCreate + 1);
        InterviewResult testInterviewResult = interviewResultList.get(interviewResultList.size() - 1);
        assertThat(testInterviewResult.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInterviewResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interviewResultRepository.findAll().size();

        // Create the InterviewResult with an existing ID
        interviewResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewResultMockMvc.perform(post("/api/interview-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewResult)))
            .andExpect(status().isBadRequest());

        // Validate the InterviewResult in the database
        List<InterviewResult> interviewResultList = interviewResultRepository.findAll();
        assertThat(interviewResultList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInterviewResults() throws Exception {
        // Initialize the database
        interviewResultRepository.saveAndFlush(interviewResult);

        // Get all the interviewResultList
        restInterviewResultMockMvc.perform(get("/api/interview-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getInterviewResult() throws Exception {
        // Initialize the database
        interviewResultRepository.saveAndFlush(interviewResult);

        // Get the interviewResult
        restInterviewResultMockMvc.perform(get("/api/interview-results/{id}", interviewResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interviewResult.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingInterviewResult() throws Exception {
        // Get the interviewResult
        restInterviewResultMockMvc.perform(get("/api/interview-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterviewResult() throws Exception {
        // Initialize the database
        interviewResultService.save(interviewResult);

        int databaseSizeBeforeUpdate = interviewResultRepository.findAll().size();

        // Update the interviewResult
        InterviewResult updatedInterviewResult = interviewResultRepository.findById(interviewResult.getId()).get();
        // Disconnect from session so that the updates on updatedInterviewResult are not directly saved in db
        em.detach(updatedInterviewResult);
        updatedInterviewResult
            .description(UPDATED_DESCRIPTION);

        restInterviewResultMockMvc.perform(put("/api/interview-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInterviewResult)))
            .andExpect(status().isOk());

        // Validate the InterviewResult in the database
        List<InterviewResult> interviewResultList = interviewResultRepository.findAll();
        assertThat(interviewResultList).hasSize(databaseSizeBeforeUpdate);
        InterviewResult testInterviewResult = interviewResultList.get(interviewResultList.size() - 1);
        assertThat(testInterviewResult.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingInterviewResult() throws Exception {
        int databaseSizeBeforeUpdate = interviewResultRepository.findAll().size();

        // Create the InterviewResult

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewResultMockMvc.perform(put("/api/interview-results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewResult)))
            .andExpect(status().isBadRequest());

        // Validate the InterviewResult in the database
        List<InterviewResult> interviewResultList = interviewResultRepository.findAll();
        assertThat(interviewResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInterviewResult() throws Exception {
        // Initialize the database
        interviewResultService.save(interviewResult);

        int databaseSizeBeforeDelete = interviewResultRepository.findAll().size();

        // Delete the interviewResult
        restInterviewResultMockMvc.perform(delete("/api/interview-results/{id}", interviewResult.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterviewResult> interviewResultList = interviewResultRepository.findAll();
        assertThat(interviewResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
