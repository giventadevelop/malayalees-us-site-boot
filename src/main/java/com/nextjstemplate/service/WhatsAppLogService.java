package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.WhatsAppLogDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.WhatsAppLog}.
 */
public interface WhatsAppLogService {
    /**
     * Save a whatsAppLog.
     *
     * @param whatsAppLogDTO the entity to save.
     * @return the persisted entity.
     */
    WhatsAppLogDTO save(WhatsAppLogDTO whatsAppLogDTO);

    /**
     * Updates a whatsAppLog.
     *
     * @param whatsAppLogDTO the entity to update.
     * @return the persisted entity.
     */
    WhatsAppLogDTO update(WhatsAppLogDTO whatsAppLogDTO);

    /**
     * Partially updates a whatsAppLog.
     *
     * @param whatsAppLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WhatsAppLogDTO> partialUpdate(WhatsAppLogDTO whatsAppLogDTO);

    /**
     * Get all the whatsAppLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WhatsAppLogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" whatsAppLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WhatsAppLogDTO> findOne(Long id);

    /**
     * Delete the "id" whatsAppLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
