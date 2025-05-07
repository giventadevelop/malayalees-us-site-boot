package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.PollDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.Poll}.
 */
public interface PollService {
    /**
     * Save a poll.
     *
     * @param pollDTO the entity to save.
     * @return the persisted entity.
     */
    PollDTO save(PollDTO pollDTO);

    /**
     * Updates a poll.
     *
     * @param pollDTO the entity to update.
     * @return the persisted entity.
     */
    PollDTO update(PollDTO pollDTO);

    /**
     * Partially updates a poll.
     *
     * @param pollDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PollDTO> partialUpdate(PollDTO pollDTO);

    /**
     * Get all the polls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PollDTO> findAll(Pageable pageable);

    /**
     * Get the "id" poll.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PollDTO> findOne(Long id);

    /**
     * Delete the "id" poll.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
