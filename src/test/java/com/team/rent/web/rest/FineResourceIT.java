package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.Fine;
import com.team.rent.repository.FineRepository;
import com.team.rent.service.FineService;

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
 * Integration tests for the {@link FineResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class FineResourceIT {

    private static final Integer DEFAULT_FINE_SIZE = 1;
    private static final Integer UPDATED_FINE_SIZE = 2;

    private static final String DEFAULT_FINE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_FINE_REASON = "BBBBBBBBBB";

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private FineService fineService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFineMockMvc;

    private Fine fine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fine createEntity(EntityManager em) {
        Fine fine = new Fine()
            .fineSize(DEFAULT_FINE_SIZE)
            .fineReason(DEFAULT_FINE_REASON);
        return fine;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fine createUpdatedEntity(EntityManager em) {
        Fine fine = new Fine()
            .fineSize(UPDATED_FINE_SIZE)
            .fineReason(UPDATED_FINE_REASON);
        return fine;
    }

    @BeforeEach
    public void initTest() {
        fine = createEntity(em);
    }

    @Test
    @Transactional
    public void createFine() throws Exception {
        int databaseSizeBeforeCreate = fineRepository.findAll().size();

        // Create the Fine
        restFineMockMvc.perform(post("/api/fines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fine)))
            .andExpect(status().isCreated());

        // Validate the Fine in the database
        List<Fine> fineList = fineRepository.findAll();
        assertThat(fineList).hasSize(databaseSizeBeforeCreate + 1);
        Fine testFine = fineList.get(fineList.size() - 1);
        assertThat(testFine.getFineSize()).isEqualTo(DEFAULT_FINE_SIZE);
        assertThat(testFine.getFineReason()).isEqualTo(DEFAULT_FINE_REASON);
    }

    @Test
    @Transactional
    public void createFineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fineRepository.findAll().size();

        // Create the Fine with an existing ID
        fine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFineMockMvc.perform(post("/api/fines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fine)))
            .andExpect(status().isBadRequest());

        // Validate the Fine in the database
        List<Fine> fineList = fineRepository.findAll();
        assertThat(fineList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFines() throws Exception {
        // Initialize the database
        fineRepository.saveAndFlush(fine);

        // Get all the fineList
        restFineMockMvc.perform(get("/api/fines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fine.getId().intValue())))
            .andExpect(jsonPath("$.[*].fineSize").value(hasItem(DEFAULT_FINE_SIZE)))
            .andExpect(jsonPath("$.[*].fineReason").value(hasItem(DEFAULT_FINE_REASON)));
    }
    
    @Test
    @Transactional
    public void getFine() throws Exception {
        // Initialize the database
        fineRepository.saveAndFlush(fine);

        // Get the fine
        restFineMockMvc.perform(get("/api/fines/{id}", fine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fine.getId().intValue()))
            .andExpect(jsonPath("$.fineSize").value(DEFAULT_FINE_SIZE))
            .andExpect(jsonPath("$.fineReason").value(DEFAULT_FINE_REASON));
    }

    @Test
    @Transactional
    public void getNonExistingFine() throws Exception {
        // Get the fine
        restFineMockMvc.perform(get("/api/fines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFine() throws Exception {
        // Initialize the database
        fineService.save(fine);

        int databaseSizeBeforeUpdate = fineRepository.findAll().size();

        // Update the fine
        Fine updatedFine = fineRepository.findById(fine.getId()).get();
        // Disconnect from session so that the updates on updatedFine are not directly saved in db
        em.detach(updatedFine);
        updatedFine
            .fineSize(UPDATED_FINE_SIZE)
            .fineReason(UPDATED_FINE_REASON);

        restFineMockMvc.perform(put("/api/fines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFine)))
            .andExpect(status().isOk());

        // Validate the Fine in the database
        List<Fine> fineList = fineRepository.findAll();
        assertThat(fineList).hasSize(databaseSizeBeforeUpdate);
        Fine testFine = fineList.get(fineList.size() - 1);
        assertThat(testFine.getFineSize()).isEqualTo(UPDATED_FINE_SIZE);
        assertThat(testFine.getFineReason()).isEqualTo(UPDATED_FINE_REASON);
    }

    @Test
    @Transactional
    public void updateNonExistingFine() throws Exception {
        int databaseSizeBeforeUpdate = fineRepository.findAll().size();

        // Create the Fine

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFineMockMvc.perform(put("/api/fines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fine)))
            .andExpect(status().isBadRequest());

        // Validate the Fine in the database
        List<Fine> fineList = fineRepository.findAll();
        assertThat(fineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFine() throws Exception {
        // Initialize the database
        fineService.save(fine);

        int databaseSizeBeforeDelete = fineRepository.findAll().size();

        // Delete the fine
        restFineMockMvc.perform(delete("/api/fines/{id}", fine.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fine> fineList = fineRepository.findAll();
        assertThat(fineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
