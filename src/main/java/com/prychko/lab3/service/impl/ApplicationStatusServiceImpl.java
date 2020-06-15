package com.prychko.lab3.service.impl;

import com.prychko.lab3.service.ApplicationStatusService;
import com.prychko.lab3.domain.ApplicationStatus;
import com.prychko.lab3.repository.ApplicationStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ApplicationStatus}.
 */
@Service
@Transactional
public class ApplicationStatusServiceImpl implements ApplicationStatusService {

    private final Logger log = LoggerFactory.getLogger(ApplicationStatusServiceImpl.class);

    private final ApplicationStatusRepository applicationStatusRepository;

    public ApplicationStatusServiceImpl(ApplicationStatusRepository applicationStatusRepository) {
        this.applicationStatusRepository = applicationStatusRepository;
    }

    /**
     * Save a applicationStatus.
     *
     * @param applicationStatus the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ApplicationStatus save(ApplicationStatus applicationStatus) {
        log.debug("Request to save ApplicationStatus : {}", applicationStatus);
        return applicationStatusRepository.save(applicationStatus);
    }

    /**
     * Get all the applicationStatuses.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationStatus> findAll() {
        log.debug("Request to get all ApplicationStatuses");
        return applicationStatusRepository.findAll();
    }

    /**
     * Get one applicationStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationStatus> findOne(Long id) {
        log.debug("Request to get ApplicationStatus : {}", id);
        return applicationStatusRepository.findById(id);
    }

    /**
     * Delete the applicationStatus by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationStatus : {}", id);
        applicationStatusRepository.deleteById(id);
    }
}
