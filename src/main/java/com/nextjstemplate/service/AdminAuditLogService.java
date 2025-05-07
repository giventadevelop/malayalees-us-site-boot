package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.AdminAuditLogDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.AdminAuditLog}.
 */
public interface AdminAuditLogService {
    /**
     * Save a adminAuditLog.
     *
     * @param adminAuditLogDTO the entity to save.
     * @return the persisted entity.
     */
    AdminAuditLogDTO save(AdminAuditLogDTO adminAuditLogDTO);

    /**
     * Updates a adminAuditLog.
     *
     * @param adminAuditLogDTO the entity to update.
     * @return the persisted entity.
     */
    AdminAuditLogDTO update(AdminAuditLogDTO adminAuditLogDTO);

    /**
     * Partially updates a adminAuditLog.
     *
     * @param adminAuditLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdminAuditLogDTO> partialUpdate(AdminAuditLogDTO adminAuditLogDTO);

    /**
     * Get all the adminAuditLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdminAuditLogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" adminAuditLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdminAuditLogDTO> findOne(Long id);

    /**
     * Delete the "id" adminAuditLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
