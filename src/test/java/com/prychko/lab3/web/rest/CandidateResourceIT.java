package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.Candidate;
import com.prychko.lab3.domain.Interview;
import com.prychko.lab3.domain.Vacancy;
import com.prychko.lab3.repository.CandidateRepository;
import com.prychko.lab3.service.CandidateService;
import com.prychko.lab3.service.dto.CandidateCriteria;
import com.prychko.lab3.service.CandidateQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CandidateResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CandidateResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CV_URL = "AAAAAAAAAA";
    private static final String UPDATED_CV_URL = "BBBBBBBBBB";

    @Autowired
    private CandidateRepository candidateRepository;

    @Mock
    private CandidateRepository candidateRepositoryMock;

    @Mock
    private CandidateService candidateServiceMock;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CandidateQueryService candidateQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidateMockMvc;

    private Candidate candidate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .fullName(DEFAULT_FULL_NAME)
            .email(DEFAULT_EMAIL)
            .cvUrl(DEFAULT_CV_URL);
        return candidate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createUpdatedEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .cvUrl(UPDATED_CV_URL);
        return candidate;
    }

    @BeforeEach
    public void initTest() {
        candidate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCandidate() throws Exception {
        int databaseSizeBeforeCreate = candidateRepository.findAll().size();

        // Create the Candidate
        restCandidateMockMvc.perform(post("/api/candidates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isCreated());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate + 1);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testCandidate.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCandidate.getCvUrl()).isEqualTo(DEFAULT_CV_URL);
    }

    @Test
    @Transactional
    public void createCandidateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = candidateRepository.findAll().size();

        // Create the Candidate with an existing ID
        candidate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateMockMvc.perform(post("/api/candidates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = candidateRepository.findAll().size();
        // set the field null
        candidate.setFullName(null);

        // Create the Candidate, which fails.

        restCandidateMockMvc.perform(post("/api/candidates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isBadRequest());

        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = candidateRepository.findAll().size();
        // set the field null
        candidate.setEmail(null);

        // Create the Candidate, which fails.

        restCandidateMockMvc.perform(post("/api/candidates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isBadRequest());

        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCandidates() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList
        restCandidateMockMvc.perform(get("/api/candidates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].cvUrl").value(hasItem(DEFAULT_CV_URL)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCandidatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(candidateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCandidateMockMvc.perform(get("/api/candidates?eagerload=true"))
            .andExpect(status().isOk());

        verify(candidateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCandidatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(candidateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCandidateMockMvc.perform(get("/api/candidates?eagerload=true"))
            .andExpect(status().isOk());

        verify(candidateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get the candidate
        restCandidateMockMvc.perform(get("/api/candidates/{id}", candidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidate.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.cvUrl").value(DEFAULT_CV_URL));
    }


    @Test
    @Transactional
    public void getCandidatesByIdFiltering() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        Long id = candidate.getId();

        defaultCandidateShouldBeFound("id.equals=" + id);
        defaultCandidateShouldNotBeFound("id.notEquals=" + id);

        defaultCandidateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCandidateShouldNotBeFound("id.greaterThan=" + id);

        defaultCandidateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCandidateShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCandidatesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where fullName equals to DEFAULT_FULL_NAME
        defaultCandidateShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the candidateList where fullName equals to UPDATED_FULL_NAME
        defaultCandidateShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllCandidatesByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where fullName not equals to DEFAULT_FULL_NAME
        defaultCandidateShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the candidateList where fullName not equals to UPDATED_FULL_NAME
        defaultCandidateShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllCandidatesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultCandidateShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the candidateList where fullName equals to UPDATED_FULL_NAME
        defaultCandidateShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllCandidatesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where fullName is not null
        defaultCandidateShouldBeFound("fullName.specified=true");

        // Get all the candidateList where fullName is null
        defaultCandidateShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCandidatesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where fullName contains DEFAULT_FULL_NAME
        defaultCandidateShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the candidateList where fullName contains UPDATED_FULL_NAME
        defaultCandidateShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllCandidatesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where fullName does not contain DEFAULT_FULL_NAME
        defaultCandidateShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the candidateList where fullName does not contain UPDATED_FULL_NAME
        defaultCandidateShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllCandidatesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where email equals to DEFAULT_EMAIL
        defaultCandidateShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the candidateList where email equals to UPDATED_EMAIL
        defaultCandidateShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where email not equals to DEFAULT_EMAIL
        defaultCandidateShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the candidateList where email not equals to UPDATED_EMAIL
        defaultCandidateShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCandidateShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the candidateList where email equals to UPDATED_EMAIL
        defaultCandidateShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where email is not null
        defaultCandidateShouldBeFound("email.specified=true");

        // Get all the candidateList where email is null
        defaultCandidateShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllCandidatesByEmailContainsSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where email contains DEFAULT_EMAIL
        defaultCandidateShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the candidateList where email contains UPDATED_EMAIL
        defaultCandidateShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where email does not contain DEFAULT_EMAIL
        defaultCandidateShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the candidateList where email does not contain UPDATED_EMAIL
        defaultCandidateShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllCandidatesByCvUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where cvUrl equals to DEFAULT_CV_URL
        defaultCandidateShouldBeFound("cvUrl.equals=" + DEFAULT_CV_URL);

        // Get all the candidateList where cvUrl equals to UPDATED_CV_URL
        defaultCandidateShouldNotBeFound("cvUrl.equals=" + UPDATED_CV_URL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByCvUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where cvUrl not equals to DEFAULT_CV_URL
        defaultCandidateShouldNotBeFound("cvUrl.notEquals=" + DEFAULT_CV_URL);

        // Get all the candidateList where cvUrl not equals to UPDATED_CV_URL
        defaultCandidateShouldBeFound("cvUrl.notEquals=" + UPDATED_CV_URL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByCvUrlIsInShouldWork() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where cvUrl in DEFAULT_CV_URL or UPDATED_CV_URL
        defaultCandidateShouldBeFound("cvUrl.in=" + DEFAULT_CV_URL + "," + UPDATED_CV_URL);

        // Get all the candidateList where cvUrl equals to UPDATED_CV_URL
        defaultCandidateShouldNotBeFound("cvUrl.in=" + UPDATED_CV_URL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByCvUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where cvUrl is not null
        defaultCandidateShouldBeFound("cvUrl.specified=true");

        // Get all the candidateList where cvUrl is null
        defaultCandidateShouldNotBeFound("cvUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllCandidatesByCvUrlContainsSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where cvUrl contains DEFAULT_CV_URL
        defaultCandidateShouldBeFound("cvUrl.contains=" + DEFAULT_CV_URL);

        // Get all the candidateList where cvUrl contains UPDATED_CV_URL
        defaultCandidateShouldNotBeFound("cvUrl.contains=" + UPDATED_CV_URL);
    }

    @Test
    @Transactional
    public void getAllCandidatesByCvUrlNotContainsSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where cvUrl does not contain DEFAULT_CV_URL
        defaultCandidateShouldNotBeFound("cvUrl.doesNotContain=" + DEFAULT_CV_URL);

        // Get all the candidateList where cvUrl does not contain UPDATED_CV_URL
        defaultCandidateShouldBeFound("cvUrl.doesNotContain=" + UPDATED_CV_URL);
    }


    @Test
    @Transactional
    public void getAllCandidatesByInterviewsIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);
        Interview interviews = InterviewResourceIT.createEntity(em);
        em.persist(interviews);
        em.flush();
        candidate.addInterviews(interviews);
        candidateRepository.saveAndFlush(candidate);
        Long interviewsId = interviews.getId();

        // Get all the candidateList where interviews equals to interviewsId
        defaultCandidateShouldBeFound("interviewsId.equals=" + interviewsId);

        // Get all the candidateList where interviews equals to interviewsId + 1
        defaultCandidateShouldNotBeFound("interviewsId.equals=" + (interviewsId + 1));
    }


    @Test
    @Transactional
    public void getAllCandidatesByVacanciesIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);
        Vacancy vacancies = VacancyResourceIT.createEntity(em);
        em.persist(vacancies);
        em.flush();
        candidate.addVacancies(vacancies);
        candidateRepository.saveAndFlush(candidate);
        Long vacanciesId = vacancies.getId();

        // Get all the candidateList where vacancies equals to vacanciesId
        defaultCandidateShouldBeFound("vacanciesId.equals=" + vacanciesId);

        // Get all the candidateList where vacancies equals to vacanciesId + 1
        defaultCandidateShouldNotBeFound("vacanciesId.equals=" + (vacanciesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCandidateShouldBeFound(String filter) throws Exception {
        restCandidateMockMvc.perform(get("/api/candidates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].cvUrl").value(hasItem(DEFAULT_CV_URL)));

        // Check, that the count call also returns 1
        restCandidateMockMvc.perform(get("/api/candidates/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCandidateShouldNotBeFound(String filter) throws Exception {
        restCandidateMockMvc.perform(get("/api/candidates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCandidateMockMvc.perform(get("/api/candidates/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCandidate() throws Exception {
        // Get the candidate
        restCandidateMockMvc.perform(get("/api/candidates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCandidate() throws Exception {
        // Initialize the database
        candidateService.save(candidate);

        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

        // Update the candidate
        Candidate updatedCandidate = candidateRepository.findById(candidate.getId()).get();
        // Disconnect from session so that the updates on updatedCandidate are not directly saved in db
        em.detach(updatedCandidate);
        updatedCandidate
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .cvUrl(UPDATED_CV_URL);

        restCandidateMockMvc.perform(put("/api/candidates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCandidate)))
            .andExpect(status().isOk());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testCandidate.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCandidate.getCvUrl()).isEqualTo(UPDATED_CV_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

        // Create the Candidate

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateMockMvc.perform(put("/api/candidates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCandidate() throws Exception {
        // Initialize the database
        candidateService.save(candidate);

        int databaseSizeBeforeDelete = candidateRepository.findAll().size();

        // Delete the candidate
        restCandidateMockMvc.perform(delete("/api/candidates/{id}", candidate.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
