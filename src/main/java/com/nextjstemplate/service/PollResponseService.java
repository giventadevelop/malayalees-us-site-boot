package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.PollResponseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.PollResponse}.
 */
public interface PollResponseService {
    /**
     * Save a pollResponse.
     *
     * @param pollResponseDTO the entity to save.
     * @return the persisted entity.
     */
    PollResponseDTO save(PollResponseDTO pollResponseDTO);

    /**
     * Updates a pollResponse.
     *
     * @param pollResponseDTO the entity to update.
     * @return the persisted entity.
     */
    PollResponseDTO update(PollResponseDTO pollResponseDTO);

    /**
     * Partially updates a pollResponse.
     *
     * @param pollResponseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PollResponseDTO> partialUpdate(PollResponseDTO pollResponseDTO);

    /**
     * Get all the pollResponses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PollResponseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pollResponse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PollResponseDTO> findOne(Long id);

    /**
     * Delete the "id" pollResponse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
