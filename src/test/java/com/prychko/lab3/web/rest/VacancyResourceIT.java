package com.prychko.lab3.web.rest;

import com.prychko.lab3.Lab3App;
import com.prychko.lab3.domain.Vacancy;
import com.prychko.lab3.domain.VacancyStatus;
import com.prychko.lab3.domain.Application;
import com.prychko.lab3.domain.Candidate;
import com.prychko.lab3.repository.VacancyRepository;
import com.prychko.lab3.service.VacancyService;
import com.prychko.lab3.service.dto.VacancyCriteria;
import com.prychko.lab3.service.VacancyQueryService;

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
 * Integration tests for the {@link VacancyResource} REST controller.
 */
@SpringBootTest(classes = Lab3App.class)

@AutoConfigureMockMvc
@WithMockUser
public class VacancyResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SALARY = 1;
    private static final Integer UPDATED_SALARY = 2;
    private static final Integer SMALLER_SALARY = 1 - 1;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private VacancyQueryService vacancyQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVacancyMockMvc;

    private Vacancy vacancy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacancy createEntity(EntityManager em) {
        Vacancy vacancy = new Vacancy()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .salary(DEFAULT_SALARY)
            .createdDate(DEFAULT_CREATED_DATE);
        return vacancy;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacancy createUpdatedEntity(EntityManager em) {
        Vacancy vacancy = new Vacancy()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .salary(UPDATED_SALARY)
            .createdDate(UPDATED_CREATED_DATE);
        return vacancy;
    }

    @BeforeEach
    public void initTest() {
        vacancy = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacancy() throws Exception {
        int databaseSizeBeforeCreate = vacancyRepository.findAll().size();

        // Create the Vacancy
        restVacancyMockMvc.perform(post("/api/vacancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancy)))
            .andExpect(status().isCreated());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeCreate + 1);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVacancy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVacancy.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testVacancy.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createVacancyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vacancyRepository.findAll().size();

        // Create the Vacancy with an existing ID
        vacancy.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacancyMockMvc.perform(post("/api/vacancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancy)))
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacancyRepository.findAll().size();
        // set the field null
        vacancy.setTitle(null);

        // Create the Vacancy, which fails.

        restVacancyMockMvc.perform(post("/api/vacancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancy)))
            .andExpect(status().isBadRequest());

        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVacancies() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList
        restVacancyMockMvc.perform(get("/api/vacancies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacancy.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }
    
    @Test
    @Transactional
    public void getVacancy() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get the vacancy
        restVacancyMockMvc.perform(get("/api/vacancies/{id}", vacancy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vacancy.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }


    @Test
    @Transactional
    public void getVacanciesByIdFiltering() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        Long id = vacancy.getId();

        defaultVacancyShouldBeFound("id.equals=" + id);
        defaultVacancyShouldNotBeFound("id.notEquals=" + id);

        defaultVacancyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVacancyShouldNotBeFound("id.greaterThan=" + id);

        defaultVacancyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVacancyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVacanciesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where title equals to DEFAULT_TITLE
        defaultVacancyShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the vacancyList where title equals to UPDATED_TITLE
        defaultVacancyShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where title not equals to DEFAULT_TITLE
        defaultVacancyShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the vacancyList where title not equals to UPDATED_TITLE
        defaultVacancyShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultVacancyShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the vacancyList where title equals to UPDATED_TITLE
        defaultVacancyShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where title is not null
        defaultVacancyShouldBeFound("title.specified=true");

        // Get all the vacancyList where title is null
        defaultVacancyShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllVacanciesByTitleContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where title contains DEFAULT_TITLE
        defaultVacancyShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the vacancyList where title contains UPDATED_TITLE
        defaultVacancyShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where title does not contain DEFAULT_TITLE
        defaultVacancyShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the vacancyList where title does not contain UPDATED_TITLE
        defaultVacancyShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllVacanciesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description equals to DEFAULT_DESCRIPTION
        defaultVacancyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the vacancyList where description equals to UPDATED_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVacanciesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description not equals to DEFAULT_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the vacancyList where description not equals to UPDATED_DESCRIPTION
        defaultVacancyShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVacanciesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultVacancyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the vacancyList where description equals to UPDATED_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVacanciesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description is not null
        defaultVacancyShouldBeFound("description.specified=true");

        // Get all the vacancyList where description is null
        defaultVacancyShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllVacanciesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description contains DEFAULT_DESCRIPTION
        defaultVacancyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the vacancyList where description contains UPDATED_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVacanciesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description does not contain DEFAULT_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the vacancyList where description does not contain UPDATED_DESCRIPTION
        defaultVacancyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary equals to DEFAULT_SALARY
        defaultVacancyShouldBeFound("salary.equals=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary equals to UPDATED_SALARY
        defaultVacancyShouldNotBeFound("salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary not equals to DEFAULT_SALARY
        defaultVacancyShouldNotBeFound("salary.notEquals=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary not equals to UPDATED_SALARY
        defaultVacancyShouldBeFound("salary.notEquals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary in DEFAULT_SALARY or UPDATED_SALARY
        defaultVacancyShouldBeFound("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY);

        // Get all the vacancyList where salary equals to UPDATED_SALARY
        defaultVacancyShouldNotBeFound("salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is not null
        defaultVacancyShouldBeFound("salary.specified=true");

        // Get all the vacancyList where salary is null
        defaultVacancyShouldNotBeFound("salary.specified=false");
    }

    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is greater than or equal to DEFAULT_SALARY
        defaultVacancyShouldBeFound("salary.greaterThanOrEqual=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is greater than or equal to UPDATED_SALARY
        defaultVacancyShouldNotBeFound("salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is less than or equal to DEFAULT_SALARY
        defaultVacancyShouldBeFound("salary.lessThanOrEqual=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is less than or equal to SMALLER_SALARY
        defaultVacancyShouldNotBeFound("salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is less than DEFAULT_SALARY
        defaultVacancyShouldNotBeFound("salary.lessThan=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is less than UPDATED_SALARY
        defaultVacancyShouldBeFound("salary.lessThan=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    public void getAllVacanciesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is greater than DEFAULT_SALARY
        defaultVacancyShouldNotBeFound("salary.greaterThan=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is greater than SMALLER_SALARY
        defaultVacancyShouldBeFound("salary.greaterThan=" + SMALLER_SALARY);
    }


    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate equals to DEFAULT_CREATED_DATE
        defaultVacancyShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the vacancyList where createdDate equals to UPDATED_CREATED_DATE
        defaultVacancyShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultVacancyShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the vacancyList where createdDate not equals to UPDATED_CREATED_DATE
        defaultVacancyShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultVacancyShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the vacancyList where createdDate equals to UPDATED_CREATED_DATE
        defaultVacancyShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate is not null
        defaultVacancyShouldBeFound("createdDate.specified=true");

        // Get all the vacancyList where createdDate is null
        defaultVacancyShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultVacancyShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the vacancyList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultVacancyShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultVacancyShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the vacancyList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultVacancyShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate is less than DEFAULT_CREATED_DATE
        defaultVacancyShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the vacancyList where createdDate is less than UPDATED_CREATED_DATE
        defaultVacancyShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVacanciesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultVacancyShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the vacancyList where createdDate is greater than SMALLER_CREATED_DATE
        defaultVacancyShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllVacanciesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);
        VacancyStatus status = VacancyStatusResourceIT.createEntity(em);
        em.persist(status);
        em.flush();
        vacancy.addStatus(status);
        vacancyRepository.saveAndFlush(vacancy);
        Long statusId = status.getId();

        // Get all the vacancyList where status equals to statusId
        defaultVacancyShouldBeFound("statusId.equals=" + statusId);

        // Get all the vacancyList where status equals to statusId + 1
        defaultVacancyShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }


    @Test
    @Transactional
    public void getAllVacanciesByApplicationsIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);
        Application applications = ApplicationResourceIT.createEntity(em);
        em.persist(applications);
        em.flush();
        vacancy.addApplications(applications);
        vacancyRepository.saveAndFlush(vacancy);
        Long applicationsId = applications.getId();

        // Get all the vacancyList where applications equals to applicationsId
        defaultVacancyShouldBeFound("applicationsId.equals=" + applicationsId);

        // Get all the vacancyList where applications equals to applicationsId + 1
        defaultVacancyShouldNotBeFound("applicationsId.equals=" + (applicationsId + 1));
    }


    @Test
    @Transactional
    public void getAllVacanciesByCandidatesIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);
        Candidate candidates = CandidateResourceIT.createEntity(em);
        em.persist(candidates);
        em.flush();
        vacancy.addCandidates(candidates);
        vacancyRepository.saveAndFlush(vacancy);
        Long candidatesId = candidates.getId();

        // Get all the vacancyList where candidates equals to candidatesId
        defaultVacancyShouldBeFound("candidatesId.equals=" + candidatesId);

        // Get all the vacancyList where candidates equals to candidatesId + 1
        defaultVacancyShouldNotBeFound("candidatesId.equals=" + (candidatesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVacancyShouldBeFound(String filter) throws Exception {
        restVacancyMockMvc.perform(get("/api/vacancies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacancy.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));

        // Check, that the count call also returns 1
        restVacancyMockMvc.perform(get("/api/vacancies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVacancyShouldNotBeFound(String filter) throws Exception {
        restVacancyMockMvc.perform(get("/api/vacancies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVacancyMockMvc.perform(get("/api/vacancies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVacancy() throws Exception {
        // Get the vacancy
        restVacancyMockMvc.perform(get("/api/vacancies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacancy() throws Exception {
        // Initialize the database
        vacancyService.save(vacancy);

        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();

        // Update the vacancy
        Vacancy updatedVacancy = vacancyRepository.findById(vacancy.getId()).get();
        // Disconnect from session so that the updates on updatedVacancy are not directly saved in db
        em.detach(updatedVacancy);
        updatedVacancy
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .salary(UPDATED_SALARY)
            .createdDate(UPDATED_CREATED_DATE);

        restVacancyMockMvc.perform(put("/api/vacancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVacancy)))
            .andExpect(status().isOk());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVacancy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVacancy.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testVacancy.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();

        // Create the Vacancy

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVacancyMockMvc.perform(put("/api/vacancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vacancy)))
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVacancy() throws Exception {
        // Initialize the database
        vacancyService.save(vacancy);

        int databaseSizeBeforeDelete = vacancyRepository.findAll().size();

        // Delete the vacancy
        restVacancyMockMvc.perform(delete("/api/vacancies/{id}", vacancy.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
