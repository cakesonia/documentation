package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.Interview;
import com.prychko.lab3.domain.InterviewResult;
import com.prychko.lab3.domain.InterviewType;
import com.prychko.lab3.domain.Candidate;
import com.prychko.lab3.domain.Interviewer;
import com.prychko.lab3.repository.InterviewRepository;
import com.prychko.lab3.service.InterviewService;
import com.prychko.lab3.service.dto.InterviewCriteria;
import com.prychko.lab3.service.InterviewQueryService;

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

import static com.prychko.lab3.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InterviewResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)

@AutoConfigureMockMvc
@WithMockUser
public class InterviewResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private InterviewQueryService interviewQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterviewMockMvc;

    private Interview interview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interview createEntity(EntityManager em) {
        Interview interview = new Interview()
            .date(DEFAULT_DATE);
        return interview;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interview createUpdatedEntity(EntityManager em) {
        Interview interview = new Interview()
            .date(UPDATED_DATE);
        return interview;
    }

    @BeforeEach
    public void initTest() {
        interview = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterview() throws Exception {
        int databaseSizeBeforeCreate = interviewRepository.findAll().size();

        // Create the Interview
        restInterviewMockMvc.perform(post("/api/interviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interview)))
            .andExpect(status().isCreated());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeCreate + 1);
        Interview testInterview = interviewList.get(interviewList.size() - 1);
        assertThat(testInterview.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createInterviewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interviewRepository.findAll().size();

        // Create the Interview with an existing ID
        interview.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewMockMvc.perform(post("/api/interviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interview)))
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInterviews() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList
        restInterviewMockMvc.perform(get("/api/interviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interview.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getInterview() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get the interview
        restInterviewMockMvc.perform(get("/api/interviews/{id}", interview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interview.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }


    @Test
    @Transactional
    public void getInterviewsByIdFiltering() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        Long id = interview.getId();

        defaultInterviewShouldBeFound("id.equals=" + id);
        defaultInterviewShouldNotBeFound("id.notEquals=" + id);

        defaultInterviewShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterviewShouldNotBeFound("id.greaterThan=" + id);

        defaultInterviewShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterviewShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInterviewsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date equals to DEFAULT_DATE
        defaultInterviewShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the interviewList where date equals to UPDATED_DATE
        defaultInterviewShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInterviewsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date not equals to DEFAULT_DATE
        defaultInterviewShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the interviewList where date not equals to UPDATED_DATE
        defaultInterviewShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInterviewsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date in DEFAULT_DATE or UPDATED_DATE
        defaultInterviewShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the interviewList where date equals to UPDATED_DATE
        defaultInterviewShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInterviewsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date is not null
        defaultInterviewShouldBeFound("date.specified=true");

        // Get all the interviewList where date is null
        defaultInterviewShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterviewsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date is greater than or equal to DEFAULT_DATE
        defaultInterviewShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the interviewList where date is greater than or equal to UPDATED_DATE
        defaultInterviewShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInterviewsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date is less than or equal to DEFAULT_DATE
        defaultInterviewShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the interviewList where date is less than or equal to SMALLER_DATE
        defaultInterviewShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllInterviewsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date is less than DEFAULT_DATE
        defaultInterviewShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the interviewList where date is less than UPDATED_DATE
        defaultInterviewShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInterviewsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList where date is greater than DEFAULT_DATE
        defaultInterviewShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the interviewList where date is greater than SMALLER_DATE
        defaultInterviewShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllInterviewsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);
        InterviewResult result = InterviewResultResourceIT.createEntity(em);
        em.persist(result);
        em.flush();
        interview.setResult(result);
        interviewRepository.saveAndFlush(interview);
        Long resultId = result.getId();

        // Get all the interviewList where result equals to resultId
        defaultInterviewShouldBeFound("resultId.equals=" + resultId);

        // Get all the interviewList where result equals to resultId + 1
        defaultInterviewShouldNotBeFound("resultId.equals=" + (resultId + 1));
    }


    @Test
    @Transactional
    public void getAllInterviewsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);
        InterviewType type = InterviewTypeResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        interview.addType(type);
        interviewRepository.saveAndFlush(interview);
        Long typeId = type.getId();

        // Get all the interviewList where type equals to typeId
        defaultInterviewShouldBeFound("typeId.equals=" + typeId);

        // Get all the interviewList where type equals to typeId + 1
        defaultInterviewShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }


    @Test
    @Transactional
    public void getAllInterviewsByCandidateIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);
        Candidate candidate = CandidateResourceIT.createEntity(em);
        em.persist(candidate);
        em.flush();
        interview.setCandidate(candidate);
        interviewRepository.saveAndFlush(interview);
        Long candidateId = candidate.getId();

        // Get all the interviewList where candidate equals to candidateId
        defaultInterviewShouldBeFound("candidateId.equals=" + candidateId);

        // Get all the interviewList where candidate equals to candidateId + 1
        defaultInterviewShouldNotBeFound("candidateId.equals=" + (candidateId + 1));
    }


    @Test
    @Transactional
    public void getAllInterviewsByInterviewerIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);
        Interviewer interviewer = InterviewerResourceIT.createEntity(em);
        em.persist(interviewer);
        em.flush();
        interview.setInterviewer(interviewer);
        interviewRepository.saveAndFlush(interview);
        Long interviewerId = interviewer.getId();

        // Get all the interviewList where interviewer equals to interviewerId
        defaultInterviewShouldBeFound("interviewerId.equals=" + interviewerId);

        // Get all the interviewList where interviewer equals to interviewerId + 1
        defaultInterviewShouldNotBeFound("interviewerId.equals=" + (interviewerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterviewShouldBeFound(String filter) throws Exception {
        restInterviewMockMvc.perform(get("/api/interviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interview.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));

        // Check, that the count call also returns 1
        restInterviewMockMvc.perform(get("/api/interviews/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterviewShouldNotBeFound(String filter) throws Exception {
        restInterviewMockMvc.perform(get("/api/interviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterviewMockMvc.perform(get("/api/interviews/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInterview() throws Exception {
        // Get the interview
        restInterviewMockMvc.perform(get("/api/interviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterview() throws Exception {
        // Initialize the database
        interviewService.save(interview);

        int databaseSizeBeforeUpdate = interviewRepository.findAll().size();

        // Update the interview
        Interview updatedInterview = interviewRepository.findById(interview.getId()).get();
        // Disconnect from session so that the updates on updatedInterview are not directly saved in db
        em.detach(updatedInterview);
        updatedInterview
            .date(UPDATED_DATE);

        restInterviewMockMvc.perform(put("/api/interviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInterview)))
            .andExpect(status().isOk());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeUpdate);
        Interview testInterview = interviewList.get(interviewList.size() - 1);
        assertThat(testInterview.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingInterview() throws Exception {
        int databaseSizeBeforeUpdate = interviewRepository.findAll().size();

        // Create the Interview

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewMockMvc.perform(put("/api/interviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interview)))
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInterview() throws Exception {
        // Initialize the database
        interviewService.save(interview);

        int databaseSizeBeforeDelete = interviewRepository.findAll().size();

        // Delete the interview
        restInterviewMockMvc.perform(delete("/api/interviews/{id}", interview.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
