package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.VacancyService;
import com.prychko.lab3.domain.Vacancy;
import com.prychko.lab3.repository.VacancyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Vacancy}.
 */
@Service
@Transactional
public class VacancyServiceImpl implements VacancyService {

    private final Logger log = LoggerFactory.getLogger(VacancyServiceImpl.class);

    private final VacancyRepository vacancyRepository;

    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    /**
     * Save a vacancy.
     *
     * @param vacancy the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Vacancy save(Vacancy vacancy) {
        log.debug("Request to save Vacancy : {}", vacancy);
        return vacancyRepository.save(vacancy);
    }

    /**
     * Get all the vacancies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Vacancy> findAll(Pageable pageable) {
        log.debug("Request to get all Vacancies");
        return vacancyRepository.findAll(pageable);
    }

    /**
     * Get one vacancy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Vacancy> findOne(Long id) {
        log.debug("Request to get Vacancy : {}", id);
        return vacancyRepository.findById(id);
    }

    /**
     * Delete the vacancy by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vacancy : {}", id);
        vacancyRepository.deleteById(id);
    }
}
