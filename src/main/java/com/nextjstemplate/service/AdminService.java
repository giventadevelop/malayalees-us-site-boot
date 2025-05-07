package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.AdminDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.Admin}.
 */
public interface AdminService {
    /**
     * Save a admin.
     *
     * @param adminDTO the entity to save.
     * @return the persisted entity.
     */
    AdminDTO save(AdminDTO adminDTO);

    /**
     * Updates a admin.
     *
     * @param adminDTO the entity to update.
     * @return the persisted entity.
     */
    AdminDTO update(AdminDTO adminDTO);

    /**
     * Partially updates a admin.
     *
     * @param adminDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdminDTO> partialUpdate(AdminDTO adminDTO);

    /**
     * Get all the admins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdminDTO> findAll(Pageable pageable);

    /**
     * Get the "id" admin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdminDTO> findOne(Long id);

    /**
     * Delete the "id" admin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
