package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.BulkOperationLogDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.BulkOperationLog}.
 */
public interface BulkOperationLogService {
    /**
     * Save a bulkOperationLog.
     *
     * @param bulkOperationLogDTO the entity to save.
     * @return the persisted entity.
     */
    BulkOperationLogDTO save(BulkOperationLogDTO bulkOperationLogDTO);

    /**
     * Updates a bulkOperationLog.
     *
     * @param bulkOperationLogDTO the entity to update.
     * @return the persisted entity.
     */
    BulkOperationLogDTO update(BulkOperationLogDTO bulkOperationLogDTO);

    /**
     * Partially updates a bulkOperationLog.
     *
     * @param bulkOperationLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BulkOperationLogDTO> partialUpdate(BulkOperationLogDTO bulkOperationLogDTO);

    /**
     * Get all the bulkOperationLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BulkOperationLogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bulkOperationLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BulkOperationLogDTO> findOne(Long id);

    /**
     * Delete the "id" bulkOperationLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
