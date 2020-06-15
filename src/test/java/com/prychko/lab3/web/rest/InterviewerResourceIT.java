package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.Interviewer;
import com.prychko.lab3.domain.Interview;
import com.prychko.lab3.repository.InterviewerRepository;
import com.prychko.lab3.service.InterviewerService;
import com.prychko.lab3.service.dto.InterviewerCriteria;
import com.prychko.lab3.service.InterviewerQueryService;

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
 * Integration tests for the {@link InterviewerResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)

@AutoConfigureMockMvc
@WithMockUser
public class InterviewerResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    @Autowired
    private InterviewerRepository interviewerRepository;

    @Autowired
    private InterviewerService interviewerService;

    @Autowired
    private InterviewerQueryService interviewerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterviewerMockMvc;

    private Interviewer interviewer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interviewer createEntity(EntityManager em) {
        Interviewer interviewer = new Interviewer()
            .fullName(DEFAULT_FULL_NAME)
            .email(DEFAULT_EMAIL)
            .position(DEFAULT_POSITION);
        return interviewer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interviewer createUpdatedEntity(EntityManager em) {
        Interviewer interviewer = new Interviewer()
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .position(UPDATED_POSITION);
        return interviewer;
    }

    @BeforeEach
    public void initTest() {
        interviewer = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterviewer() throws Exception {
        int databaseSizeBeforeCreate = interviewerRepository.findAll().size();

        // Create the Interviewer
        restInterviewerMockMvc.perform(post("/api/interviewers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewer)))
            .andExpect(status().isCreated());

        // Validate the Interviewer in the database
        List<Interviewer> interviewerList = interviewerRepository.findAll();
        assertThat(interviewerList).hasSize(databaseSizeBeforeCreate + 1);
        Interviewer testInterviewer = interviewerList.get(interviewerList.size() - 1);
        assertThat(testInterviewer.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testInterviewer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInterviewer.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    public void createInterviewerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interviewerRepository.findAll().size();

        // Create the Interviewer with an existing ID
        interviewer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewerMockMvc.perform(post("/api/interviewers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewer)))
            .andExpect(status().isBadRequest());

        // Validate the Interviewer in the database
        List<Interviewer> interviewerList = interviewerRepository.findAll();
        assertThat(interviewerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = interviewerRepository.findAll().size();
        // set the field null
        interviewer.setFullName(null);

        // Create the Interviewer, which fails.

        restInterviewerMockMvc.perform(post("/api/interviewers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewer)))
            .andExpect(status().isBadRequest());

        List<Interviewer> interviewerList = interviewerRepository.findAll();
        assertThat(interviewerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInterviewers() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList
        restInterviewerMockMvc.perform(get("/api/interviewers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }
    
    @Test
    @Transactional
    public void getInterviewer() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get the interviewer
        restInterviewerMockMvc.perform(get("/api/interviewers/{id}", interviewer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interviewer.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }


    @Test
    @Transactional
    public void getInterviewersByIdFiltering() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        Long id = interviewer.getId();

        defaultInterviewerShouldBeFound("id.equals=" + id);
        defaultInterviewerShouldNotBeFound("id.notEquals=" + id);

        defaultInterviewerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterviewerShouldNotBeFound("id.greaterThan=" + id);

        defaultInterviewerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterviewerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInterviewersByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where fullName equals to DEFAULT_FULL_NAME
        defaultInterviewerShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the interviewerList where fullName equals to UPDATED_FULL_NAME
        defaultInterviewerShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllInterviewersByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where fullName not equals to DEFAULT_FULL_NAME
        defaultInterviewerShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the interviewerList where fullName not equals to UPDATED_FULL_NAME
        defaultInterviewerShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllInterviewersByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultInterviewerShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the interviewerList where fullName equals to UPDATED_FULL_NAME
        defaultInterviewerShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllInterviewersByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where fullName is not null
        defaultInterviewerShouldBeFound("fullName.specified=true");

        // Get all the interviewerList where fullName is null
        defaultInterviewerShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllInterviewersByFullNameContainsSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where fullName contains DEFAULT_FULL_NAME
        defaultInterviewerShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the interviewerList where fullName contains UPDATED_FULL_NAME
        defaultInterviewerShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllInterviewersByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where fullName does not contain DEFAULT_FULL_NAME
        defaultInterviewerShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the interviewerList where fullName does not contain UPDATED_FULL_NAME
        defaultInterviewerShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllInterviewersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where email equals to DEFAULT_EMAIL
        defaultInterviewerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the interviewerList where email equals to UPDATED_EMAIL
        defaultInterviewerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllInterviewersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where email not equals to DEFAULT_EMAIL
        defaultInterviewerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the interviewerList where email not equals to UPDATED_EMAIL
        defaultInterviewerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllInterviewersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultInterviewerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the interviewerList where email equals to UPDATED_EMAIL
        defaultInterviewerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllInterviewersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where email is not null
        defaultInterviewerShouldBeFound("email.specified=true");

        // Get all the interviewerList where email is null
        defaultInterviewerShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllInterviewersByEmailContainsSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where email contains DEFAULT_EMAIL
        defaultInterviewerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the interviewerList where email contains UPDATED_EMAIL
        defaultInterviewerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllInterviewersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where email does not contain DEFAULT_EMAIL
        defaultInterviewerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the interviewerList where email does not contain UPDATED_EMAIL
        defaultInterviewerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllInterviewersByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where position equals to DEFAULT_POSITION
        defaultInterviewerShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the interviewerList where position equals to UPDATED_POSITION
        defaultInterviewerShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllInterviewersByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where position not equals to DEFAULT_POSITION
        defaultInterviewerShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the interviewerList where position not equals to UPDATED_POSITION
        defaultInterviewerShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllInterviewersByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultInterviewerShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the interviewerList where position equals to UPDATED_POSITION
        defaultInterviewerShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllInterviewersByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where position is not null
        defaultInterviewerShouldBeFound("position.specified=true");

        // Get all the interviewerList where position is null
        defaultInterviewerShouldNotBeFound("position.specified=false");
    }
                @Test
    @Transactional
    public void getAllInterviewersByPositionContainsSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where position contains DEFAULT_POSITION
        defaultInterviewerShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the interviewerList where position contains UPDATED_POSITION
        defaultInterviewerShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllInterviewersByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);

        // Get all the interviewerList where position does not contain DEFAULT_POSITION
        defaultInterviewerShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the interviewerList where position does not contain UPDATED_POSITION
        defaultInterviewerShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }


    @Test
    @Transactional
    public void getAllInterviewersByInterviewsIsEqualToSomething() throws Exception {
        // Initialize the database
        interviewerRepository.saveAndFlush(interviewer);
        Interview interviews = InterviewResourceIT.createEntity(em);
        em.persist(interviews);
        em.flush();
        interviewer.addInterviews(interviews);
        interviewerRepository.saveAndFlush(interviewer);
        Long interviewsId = interviews.getId();

        // Get all the interviewerList where interviews equals to interviewsId
        defaultInterviewerShouldBeFound("interviewsId.equals=" + interviewsId);

        // Get all the interviewerList where interviews equals to interviewsId + 1
        defaultInterviewerShouldNotBeFound("interviewsId.equals=" + (interviewsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterviewerShouldBeFound(String filter) throws Exception {
        restInterviewerMockMvc.perform(get("/api/interviewers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restInterviewerMockMvc.perform(get("/api/interviewers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterviewerShouldNotBeFound(String filter) throws Exception {
        restInterviewerMockMvc.perform(get("/api/interviewers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterviewerMockMvc.perform(get("/api/interviewers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInterviewer() throws Exception {
        // Get the interviewer
        restInterviewerMockMvc.perform(get("/api/interviewers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterviewer() throws Exception {
        // Initialize the database
        interviewerService.save(interviewer);

        int databaseSizeBeforeUpdate = interviewerRepository.findAll().size();

        // Update the interviewer
        Interviewer updatedInterviewer = interviewerRepository.findById(interviewer.getId()).get();
        // Disconnect from session so that the updates on updatedInterviewer are not directly saved in db
        em.detach(updatedInterviewer);
        updatedInterviewer
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .position(UPDATED_POSITION);

        restInterviewerMockMvc.perform(put("/api/interviewers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInterviewer)))
            .andExpect(status().isOk());

        // Validate the Interviewer in the database
        List<Interviewer> interviewerList = interviewerRepository.findAll();
        assertThat(interviewerList).hasSize(databaseSizeBeforeUpdate);
        Interviewer testInterviewer = interviewerList.get(interviewerList.size() - 1);
        assertThat(testInterviewer.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testInterviewer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInterviewer.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void updateNonExistingInterviewer() throws Exception {
        int databaseSizeBeforeUpdate = interviewerRepository.findAll().size();

        // Create the Interviewer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewerMockMvc.perform(put("/api/interviewers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(interviewer)))
            .andExpect(status().isBadRequest());

        // Validate the Interviewer in the database
        List<Interviewer> interviewerList = interviewerRepository.findAll();
        assertThat(interviewerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInterviewer() throws Exception {
        // Initialize the database
        interviewerService.save(interviewer);

        int databaseSizeBeforeDelete = interviewerRepository.findAll().size();

        // Delete the interviewer
        restInterviewerMockMvc.perform(delete("/api/interviewers/{id}", interviewer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Interviewer> interviewerList = interviewerRepository.findAll();
        assertThat(interviewerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
