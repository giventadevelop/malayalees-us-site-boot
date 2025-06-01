package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.TenantSettingsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.TenantSettings}.
 */
public interface TenantSettingsService {
    /**
     * Save a tenantSettings.
     *
     * @param tenantSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    TenantSettingsDTO save(TenantSettingsDTO tenantSettingsDTO);

    /**
     * Updates a tenantSettings.
     *
     * @param tenantSettingsDTO the entity to update.
     * @return the persisted entity.
     */
    TenantSettingsDTO update(TenantSettingsDTO tenantSettingsDTO);

    /**
     * Partially updates a tenantSettings.
     *
     * @param tenantSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TenantSettingsDTO> partialUpdate(TenantSettingsDTO tenantSettingsDTO);

    /**
     * Get all the tenantSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TenantSettingsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tenantSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TenantSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" tenantSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
