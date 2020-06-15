package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.RentalPoint;
import com.team.rent.domain.Autopark;
import com.team.rent.domain.Client;
import com.team.rent.repository.RentalPointRepository;
import com.team.rent.service.RentalPointService;
import com.team.rent.service.dto.RentalPointCriteria;
import com.team.rent.service.RentalPointQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.team.rent.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RentalPointResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RentalPointResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_WORKTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_WORKTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_WORKTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private RentalPointRepository rentalPointRepository;

    @Mock
    private RentalPointRepository rentalPointRepositoryMock;

    @Mock
    private RentalPointService rentalPointServiceMock;

    @Autowired
    private RentalPointService rentalPointService;

    @Autowired
    private RentalPointQueryService rentalPointQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalPointMockMvc;

    private RentalPoint rentalPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalPoint createEntity(EntityManager em) {
        RentalPoint rentalPoint = new RentalPoint()
            .location(DEFAULT_LOCATION)
            .worktime(DEFAULT_WORKTIME);
        return rentalPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalPoint createUpdatedEntity(EntityManager em) {
        RentalPoint rentalPoint = new RentalPoint()
            .location(UPDATED_LOCATION)
            .worktime(UPDATED_WORKTIME);
        return rentalPoint;
    }

    @BeforeEach
    public void initTest() {
        rentalPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createRentalPoint() throws Exception {
        int databaseSizeBeforeCreate = rentalPointRepository.findAll().size();

        // Create the RentalPoint
        restRentalPointMockMvc.perform(post("/api/rental-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentalPoint)))
            .andExpect(status().isCreated());

        // Validate the RentalPoint in the database
        List<RentalPoint> rentalPointList = rentalPointRepository.findAll();
        assertThat(rentalPointList).hasSize(databaseSizeBeforeCreate + 1);
        RentalPoint testRentalPoint = rentalPointList.get(rentalPointList.size() - 1);
        assertThat(testRentalPoint.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testRentalPoint.getWorktime()).isEqualTo(DEFAULT_WORKTIME);
    }

    @Test
    @Transactional
    public void createRentalPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rentalPointRepository.findAll().size();

        // Create the RentalPoint with an existing ID
        rentalPoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalPointMockMvc.perform(post("/api/rental-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentalPoint)))
            .andExpect(status().isBadRequest());

        // Validate the RentalPoint in the database
        List<RentalPoint> rentalPointList = rentalPointRepository.findAll();
        assertThat(rentalPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRentalPoints() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList
        restRentalPointMockMvc.perform(get("/api/rental-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].worktime").value(hasItem(sameInstant(DEFAULT_WORKTIME))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllRentalPointsWithEagerRelationshipsIsEnabled() throws Exception {
        when(rentalPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRentalPointMockMvc.perform(get("/api/rental-points?eagerload=true"))
            .andExpect(status().isOk());

        verify(rentalPointServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllRentalPointsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rentalPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRentalPointMockMvc.perform(get("/api/rental-points?eagerload=true"))
            .andExpect(status().isOk());

        verify(rentalPointServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getRentalPoint() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get the rentalPoint
        restRentalPointMockMvc.perform(get("/api/rental-points/{id}", rentalPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rentalPoint.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.worktime").value(sameInstant(DEFAULT_WORKTIME)));
    }


    @Test
    @Transactional
    public void getRentalPointsByIdFiltering() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        Long id = rentalPoint.getId();

        defaultRentalPointShouldBeFound("id.equals=" + id);
        defaultRentalPointShouldNotBeFound("id.notEquals=" + id);

        defaultRentalPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRentalPointShouldNotBeFound("id.greaterThan=" + id);

        defaultRentalPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRentalPointShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRentalPointsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where location equals to DEFAULT_LOCATION
        defaultRentalPointShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the rentalPointList where location equals to UPDATED_LOCATION
        defaultRentalPointShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where location not equals to DEFAULT_LOCATION
        defaultRentalPointShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the rentalPointList where location not equals to UPDATED_LOCATION
        defaultRentalPointShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultRentalPointShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the rentalPointList where location equals to UPDATED_LOCATION
        defaultRentalPointShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where location is not null
        defaultRentalPointShouldBeFound("location.specified=true");

        // Get all the rentalPointList where location is null
        defaultRentalPointShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllRentalPointsByLocationContainsSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where location contains DEFAULT_LOCATION
        defaultRentalPointShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the rentalPointList where location contains UPDATED_LOCATION
        defaultRentalPointShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where location does not contain DEFAULT_LOCATION
        defaultRentalPointShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the rentalPointList where location does not contain UPDATED_LOCATION
        defaultRentalPointShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime equals to DEFAULT_WORKTIME
        defaultRentalPointShouldBeFound("worktime.equals=" + DEFAULT_WORKTIME);

        // Get all the rentalPointList where worktime equals to UPDATED_WORKTIME
        defaultRentalPointShouldNotBeFound("worktime.equals=" + UPDATED_WORKTIME);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime not equals to DEFAULT_WORKTIME
        defaultRentalPointShouldNotBeFound("worktime.notEquals=" + DEFAULT_WORKTIME);

        // Get all the rentalPointList where worktime not equals to UPDATED_WORKTIME
        defaultRentalPointShouldBeFound("worktime.notEquals=" + UPDATED_WORKTIME);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsInShouldWork() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime in DEFAULT_WORKTIME or UPDATED_WORKTIME
        defaultRentalPointShouldBeFound("worktime.in=" + DEFAULT_WORKTIME + "," + UPDATED_WORKTIME);

        // Get all the rentalPointList where worktime equals to UPDATED_WORKTIME
        defaultRentalPointShouldNotBeFound("worktime.in=" + UPDATED_WORKTIME);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime is not null
        defaultRentalPointShouldBeFound("worktime.specified=true");

        // Get all the rentalPointList where worktime is null
        defaultRentalPointShouldNotBeFound("worktime.specified=false");
    }

    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime is greater than or equal to DEFAULT_WORKTIME
        defaultRentalPointShouldBeFound("worktime.greaterThanOrEqual=" + DEFAULT_WORKTIME);

        // Get all the rentalPointList where worktime is greater than or equal to UPDATED_WORKTIME
        defaultRentalPointShouldNotBeFound("worktime.greaterThanOrEqual=" + UPDATED_WORKTIME);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime is less than or equal to DEFAULT_WORKTIME
        defaultRentalPointShouldBeFound("worktime.lessThanOrEqual=" + DEFAULT_WORKTIME);

        // Get all the rentalPointList where worktime is less than or equal to SMALLER_WORKTIME
        defaultRentalPointShouldNotBeFound("worktime.lessThanOrEqual=" + SMALLER_WORKTIME);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsLessThanSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime is less than DEFAULT_WORKTIME
        defaultRentalPointShouldNotBeFound("worktime.lessThan=" + DEFAULT_WORKTIME);

        // Get all the rentalPointList where worktime is less than UPDATED_WORKTIME
        defaultRentalPointShouldBeFound("worktime.lessThan=" + UPDATED_WORKTIME);
    }

    @Test
    @Transactional
    public void getAllRentalPointsByWorktimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);

        // Get all the rentalPointList where worktime is greater than DEFAULT_WORKTIME
        defaultRentalPointShouldNotBeFound("worktime.greaterThan=" + DEFAULT_WORKTIME);

        // Get all the rentalPointList where worktime is greater than SMALLER_WORKTIME
        defaultRentalPointShouldBeFound("worktime.greaterThan=" + SMALLER_WORKTIME);
    }


    @Test
    @Transactional
    public void getAllRentalPointsByAutoparkIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);
        Autopark autopark = AutoparkResourceIT.createEntity(em);
        em.persist(autopark);
        em.flush();
        rentalPoint.setAutopark(autopark);
        rentalPointRepository.saveAndFlush(rentalPoint);
        Long autoparkId = autopark.getId();

        // Get all the rentalPointList where autopark equals to autoparkId
        defaultRentalPointShouldBeFound("autoparkId.equals=" + autoparkId);

        // Get all the rentalPointList where autopark equals to autoparkId + 1
        defaultRentalPointShouldNotBeFound("autoparkId.equals=" + (autoparkId + 1));
    }


    @Test
    @Transactional
    public void getAllRentalPointsByClientsIsEqualToSomething() throws Exception {
        // Initialize the database
        rentalPointRepository.saveAndFlush(rentalPoint);
        Client clients = ClientResourceIT.createEntity(em);
        em.persist(clients);
        em.flush();
        rentalPoint.addClients(clients);
        rentalPointRepository.saveAndFlush(rentalPoint);
        Long clientsId = clients.getId();

        // Get all the rentalPointList where clients equals to clientsId
        defaultRentalPointShouldBeFound("clientsId.equals=" + clientsId);

        // Get all the rentalPointList where clients equals to clientsId + 1
        defaultRentalPointShouldNotBeFound("clientsId.equals=" + (clientsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRentalPointShouldBeFound(String filter) throws Exception {
        restRentalPointMockMvc.perform(get("/api/rental-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].worktime").value(hasItem(sameInstant(DEFAULT_WORKTIME))));

        // Check, that the count call also returns 1
        restRentalPointMockMvc.perform(get("/api/rental-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRentalPointShouldNotBeFound(String filter) throws Exception {
        restRentalPointMockMvc.perform(get("/api/rental-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRentalPointMockMvc.perform(get("/api/rental-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRentalPoint() throws Exception {
        // Get the rentalPoint
        restRentalPointMockMvc.perform(get("/api/rental-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRentalPoint() throws Exception {
        // Initialize the database
        rentalPointService.save(rentalPoint);

        int databaseSizeBeforeUpdate = rentalPointRepository.findAll().size();

        // Update the rentalPoint
        RentalPoint updatedRentalPoint = rentalPointRepository.findById(rentalPoint.getId()).get();
        // Disconnect from session so that the updates on updatedRentalPoint are not directly saved in db
        em.detach(updatedRentalPoint);
        updatedRentalPoint
            .location(UPDATED_LOCATION)
            .worktime(UPDATED_WORKTIME);

        restRentalPointMockMvc.perform(put("/api/rental-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRentalPoint)))
            .andExpect(status().isOk());

        // Validate the RentalPoint in the database
        List<RentalPoint> rentalPointList = rentalPointRepository.findAll();
        assertThat(rentalPointList).hasSize(databaseSizeBeforeUpdate);
        RentalPoint testRentalPoint = rentalPointList.get(rentalPointList.size() - 1);
        assertThat(testRentalPoint.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testRentalPoint.getWorktime()).isEqualTo(UPDATED_WORKTIME);
    }

    @Test
    @Transactional
    public void updateNonExistingRentalPoint() throws Exception {
        int databaseSizeBeforeUpdate = rentalPointRepository.findAll().size();

        // Create the RentalPoint

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalPointMockMvc.perform(put("/api/rental-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentalPoint)))
            .andExpect(status().isBadRequest());

        // Validate the RentalPoint in the database
        List<RentalPoint> rentalPointList = rentalPointRepository.findAll();
        assertThat(rentalPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRentalPoint() throws Exception {
        // Initialize the database
        rentalPointService.save(rentalPoint);

        int databaseSizeBeforeDelete = rentalPointRepository.findAll().size();

        // Delete the rentalPoint
        restRentalPointMockMvc.perform(delete("/api/rental-points/{id}", rentalPoint.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RentalPoint> rentalPointList = rentalPointRepository.findAll();
        assertThat(rentalPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
