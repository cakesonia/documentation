package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.VacancyStatusService;
import com.prychko.lab3.domain.VacancyStatus;
import com.prychko.lab3.repository.VacancyStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link VacancyStatus}.
 */
@Service
@Transactional
public class VacancyStatusServiceImpl implements VacancyStatusService {

    private final Logger log = LoggerFactory.getLogger(VacancyStatusServiceImpl.class);

    private final VacancyStatusRepository vacancyStatusRepository;

    public VacancyStatusServiceImpl(VacancyStatusRepository vacancyStatusRepository) {
        this.vacancyStatusRepository = vacancyStatusRepository;
    }

    /**
     * Save a vacancyStatus.
     *
     * @param vacancyStatus the entity to save.
     * @return the persisted entity.
     */
    @Override
    public VacancyStatus save(VacancyStatus vacancyStatus) {
        log.debug("Request to save VacancyStatus : {}", vacancyStatus);
        return vacancyStatusRepository.save(vacancyStatus);
    }

    /**
     * Get all the vacancyStatuses.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VacancyStatus> findAll() {
        log.debug("Request to get all VacancyStatuses");
        return vacancyStatusRepository.findAll();
    }

    /**
     * Get one vacancyStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VacancyStatus> findOne(Long id) {
        log.debug("Request to get VacancyStatus : {}", id);
        return vacancyStatusRepository.findById(id);
    }

    /**
     * Delete the vacancyStatus by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VacancyStatus : {}", id);
        vacancyStatusRepository.deleteById(id);
    }
}
