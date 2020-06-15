package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.Client;
import com.team.rent.domain.Request;
import com.team.rent.domain.Rent;
import com.team.rent.domain.Fine;
import com.team.rent.domain.RentalPoint;
import com.team.rent.repository.ClientRepository;
import com.team.rent.service.ClientService;
import com.team.rent.service.dto.ClientCriteria;
import com.team.rent.service.ClientQueryService;

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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ClientResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "9122456543";
    private static final String UPDATED_PHONE = "0732405342";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientQueryService clientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .fullName(DEFAULT_FULL_NAME)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS);
        return client;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client
        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testClient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testClient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client with an existing ID
        client.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setFullName(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }
    
    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }


    @Test
    @Transactional
    public void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientShouldBeFound("id.equals=" + id);
        defaultClientShouldNotBeFound("id.notEquals=" + id);

        defaultClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.greaterThan=" + id);

        defaultClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllClientsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where fullName equals to DEFAULT_FULL_NAME
        defaultClientShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the clientList where fullName equals to UPDATED_FULL_NAME
        defaultClientShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where fullName not equals to DEFAULT_FULL_NAME
        defaultClientShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the clientList where fullName not equals to UPDATED_FULL_NAME
        defaultClientShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultClientShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the clientList where fullName equals to UPDATED_FULL_NAME
        defaultClientShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where fullName is not null
        defaultClientShouldBeFound("fullName.specified=true");

        // Get all the clientList where fullName is null
        defaultClientShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where fullName contains DEFAULT_FULL_NAME
        defaultClientShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the clientList where fullName contains UPDATED_FULL_NAME
        defaultClientShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where fullName does not contain DEFAULT_FULL_NAME
        defaultClientShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the clientList where fullName does not contain UPDATED_FULL_NAME
        defaultClientShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllClientsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone equals to DEFAULT_PHONE
        defaultClientShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the clientList where phone equals to UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone not equals to DEFAULT_PHONE
        defaultClientShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the clientList where phone not equals to UPDATED_PHONE
        defaultClientShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultClientShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the clientList where phone equals to UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone is not null
        defaultClientShouldBeFound("phone.specified=true");

        // Get all the clientList where phone is null
        defaultClientShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone contains DEFAULT_PHONE
        defaultClientShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the clientList where phone contains UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone does not contain DEFAULT_PHONE
        defaultClientShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the clientList where phone does not contain UPDATED_PHONE
        defaultClientShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllClientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where address equals to DEFAULT_ADDRESS
        defaultClientShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the clientList where address equals to UPDATED_ADDRESS
        defaultClientShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllClientsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where address not equals to DEFAULT_ADDRESS
        defaultClientShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the clientList where address not equals to UPDATED_ADDRESS
        defaultClientShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllClientsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultClientShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the clientList where address equals to UPDATED_ADDRESS
        defaultClientShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllClientsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where address is not null
        defaultClientShouldBeFound("address.specified=true");

        // Get all the clientList where address is null
        defaultClientShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByAddressContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where address contains DEFAULT_ADDRESS
        defaultClientShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the clientList where address contains UPDATED_ADDRESS
        defaultClientShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllClientsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where address does not contain DEFAULT_ADDRESS
        defaultClientShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the clientList where address does not contain UPDATED_ADDRESS
        defaultClientShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllClientsByRequestsIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Request requests = RequestResourceIT.createEntity(em);
        em.persist(requests);
        em.flush();
        client.addRequests(requests);
        clientRepository.saveAndFlush(client);
        Long requestsId = requests.getId();

        // Get all the clientList where requests equals to requestsId
        defaultClientShouldBeFound("requestsId.equals=" + requestsId);

        // Get all the clientList where requests equals to requestsId + 1
        defaultClientShouldNotBeFound("requestsId.equals=" + (requestsId + 1));
    }


    @Test
    @Transactional
    public void getAllClientsByRentsIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Rent rents = RentResourceIT.createEntity(em);
        em.persist(rents);
        em.flush();
        client.addRents(rents);
        clientRepository.saveAndFlush(client);
        Long rentsId = rents.getId();

        // Get all the clientList where rents equals to rentsId
        defaultClientShouldBeFound("rentsId.equals=" + rentsId);

        // Get all the clientList where rents equals to rentsId + 1
        defaultClientShouldNotBeFound("rentsId.equals=" + (rentsId + 1));
    }


    @Test
    @Transactional
    public void getAllClientsByFinesIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Fine fines = FineResourceIT.createEntity(em);
        em.persist(fines);
        em.flush();
        client.addFines(fines);
        clientRepository.saveAndFlush(client);
        Long finesId = fines.getId();

        // Get all the clientList where fines equals to finesId
        defaultClientShouldBeFound("finesId.equals=" + finesId);

        // Get all the clientList where fines equals to finesId + 1
        defaultClientShouldNotBeFound("finesId.equals=" + (finesId + 1));
    }


    @Test
    @Transactional
    public void getAllClientsByRentalPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        RentalPoint rentalPoints = RentalPointResourceIT.createEntity(em);
        em.persist(rentalPoints);
        em.flush();
        client.addRentalPoints(rentalPoints);
        clientRepository.saveAndFlush(client);
        Long rentalPointsId = rentalPoints.getId();

        // Get all the clientList where rentalPoints equals to rentalPointsId
        defaultClientShouldBeFound("rentalPointsId.equals=" + rentalPointsId);

        // Get all the clientList where rentalPoints equals to rentalPointsId + 1
        defaultClientShouldNotBeFound("rentalPointsId.equals=" + (rentalPointsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restClientMockMvc.perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc.perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);

        restClientMockMvc.perform(put("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClient)))
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testClient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Create the Client

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc.perform(put("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
