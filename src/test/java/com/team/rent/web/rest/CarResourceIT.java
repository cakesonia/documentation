package com.team.rent.web.rest;

import com.team.rent.RentcarApp;
import com.team.rent.domain.Car;
import com.team.rent.domain.Request;
import com.team.rent.domain.CarType;
import com.team.rent.domain.CarBrand;
import com.team.rent.domain.Autopark;
import com.team.rent.repository.CarRepository;
import com.team.rent.service.CarService;
import com.team.rent.service.dto.CarCriteria;
import com.team.rent.service.CarQueryService;

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

import static com.team.rent.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@SpringBootTest(classes = RentcarApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CarResourceIT {

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;
    private static final Integer SMALLER_PRICE = 1 - 1;

    private static final ZonedDateTime DEFAULT_MANUFACTURED_YEAR = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MANUFACTURED_YEAR = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MANUFACTURED_YEAR = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private CarQueryService carQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
            .price(DEFAULT_PRICE)
            .manufacturedYear(DEFAULT_MANUFACTURED_YEAR);
        return car;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car()
            .price(UPDATED_PRICE)
            .manufacturedYear(UPDATED_MANUFACTURED_YEAR);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    public void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car
        restCarMockMvc.perform(post("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCar.getManufacturedYear()).isEqualTo(DEFAULT_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void createCarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car with an existing ID
        car.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc.perform(post("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc.perform(get("/api/cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].manufacturedYear").value(hasItem(sameInstant(DEFAULT_MANUFACTURED_YEAR))));
    }
    
    @Test
    @Transactional
    public void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.manufacturedYear").value(sameInstant(DEFAULT_MANUFACTURED_YEAR)));
    }


    @Test
    @Transactional
    public void getCarsByIdFiltering() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarShouldBeFound("id.equals=" + id);
        defaultCarShouldNotBeFound("id.notEquals=" + id);

        defaultCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.greaterThan=" + id);

        defaultCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCarsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price equals to DEFAULT_PRICE
        defaultCarShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the carList where price equals to UPDATED_PRICE
        defaultCarShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCarsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price not equals to DEFAULT_PRICE
        defaultCarShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the carList where price not equals to UPDATED_PRICE
        defaultCarShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCarsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCarShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the carList where price equals to UPDATED_PRICE
        defaultCarShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCarsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price is not null
        defaultCarShouldBeFound("price.specified=true");

        // Get all the carList where price is null
        defaultCarShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllCarsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price is greater than or equal to DEFAULT_PRICE
        defaultCarShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the carList where price is greater than or equal to UPDATED_PRICE
        defaultCarShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCarsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price is less than or equal to DEFAULT_PRICE
        defaultCarShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the carList where price is less than or equal to SMALLER_PRICE
        defaultCarShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllCarsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price is less than DEFAULT_PRICE
        defaultCarShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the carList where price is less than UPDATED_PRICE
        defaultCarShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCarsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where price is greater than DEFAULT_PRICE
        defaultCarShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the carList where price is greater than SMALLER_PRICE
        defaultCarShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear equals to DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.equals=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear equals to UPDATED_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.equals=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear not equals to DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.notEquals=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear not equals to UPDATED_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.notEquals=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear in DEFAULT_MANUFACTURED_YEAR or UPDATED_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.in=" + DEFAULT_MANUFACTURED_YEAR + "," + UPDATED_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear equals to UPDATED_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.in=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is not null
        defaultCarShouldBeFound("manufacturedYear.specified=true");

        // Get all the carList where manufacturedYear is null
        defaultCarShouldNotBeFound("manufacturedYear.specified=false");
    }

    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is greater than or equal to DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.greaterThanOrEqual=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is greater than or equal to UPDATED_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.greaterThanOrEqual=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is less than or equal to DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.lessThanOrEqual=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is less than or equal to SMALLER_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.lessThanOrEqual=" + SMALLER_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is less than DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.lessThan=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is less than UPDATED_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.lessThan=" + UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void getAllCarsByManufacturedYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufacturedYear is greater than DEFAULT_MANUFACTURED_YEAR
        defaultCarShouldNotBeFound("manufacturedYear.greaterThan=" + DEFAULT_MANUFACTURED_YEAR);

        // Get all the carList where manufacturedYear is greater than SMALLER_MANUFACTURED_YEAR
        defaultCarShouldBeFound("manufacturedYear.greaterThan=" + SMALLER_MANUFACTURED_YEAR);
    }


    @Test
    @Transactional
    public void getAllCarsByRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        Request request = RequestResourceIT.createEntity(em);
        em.persist(request);
        em.flush();
        car.setRequest(request);
        request.setCar(car);
        carRepository.saveAndFlush(car);
        Long requestId = request.getId();

        // Get all the carList where request equals to requestId
        defaultCarShouldBeFound("requestId.equals=" + requestId);

        // Get all the carList where request equals to requestId + 1
        defaultCarShouldNotBeFound("requestId.equals=" + (requestId + 1));
    }


    @Test
    @Transactional
    public void getAllCarsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        CarType type = CarTypeResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        car.setType(type);
        carRepository.saveAndFlush(car);
        Long typeId = type.getId();

        // Get all the carList where type equals to typeId
        defaultCarShouldBeFound("typeId.equals=" + typeId);

        // Get all the carList where type equals to typeId + 1
        defaultCarShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }


    @Test
    @Transactional
    public void getAllCarsByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        CarBrand brand = CarBrandResourceIT.createEntity(em);
        em.persist(brand);
        em.flush();
        car.setBrand(brand);
        carRepository.saveAndFlush(car);
        Long brandId = brand.getId();

        // Get all the carList where brand equals to brandId
        defaultCarShouldBeFound("brandId.equals=" + brandId);

        // Get all the carList where brand equals to brandId + 1
        defaultCarShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }


    @Test
    @Transactional
    public void getAllCarsByAutoparkIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        Autopark autopark = AutoparkResourceIT.createEntity(em);
        em.persist(autopark);
        em.flush();
        car.setAutopark(autopark);
        carRepository.saveAndFlush(car);
        Long autoparkId = autopark.getId();

        // Get all the carList where autopark equals to autoparkId
        defaultCarShouldBeFound("autoparkId.equals=" + autoparkId);

        // Get all the carList where autopark equals to autoparkId + 1
        defaultCarShouldNotBeFound("autoparkId.equals=" + (autoparkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarShouldBeFound(String filter) throws Exception {
        restCarMockMvc.perform(get("/api/cars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].manufacturedYear").value(hasItem(sameInstant(DEFAULT_MANUFACTURED_YEAR))));

        // Check, that the count call also returns 1
        restCarMockMvc.perform(get("/api/cars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarShouldNotBeFound(String filter) throws Exception {
        restCarMockMvc.perform(get("/api/cars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarMockMvc.perform(get("/api/cars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCar() throws Exception {
        // Initialize the database
        carService.save(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
            .price(UPDATED_PRICE)
            .manufacturedYear(UPDATED_MANUFACTURED_YEAR);

        restCarMockMvc.perform(put("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCar)))
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCar.getManufacturedYear()).isEqualTo(UPDATED_MANUFACTURED_YEAR);
    }

    @Test
    @Transactional
    public void updateNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Create the Car

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc.perform(put("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCar() throws Exception {
        // Initialize the database
        carService.save(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc.perform(delete("/api/cars/{id}", car.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
