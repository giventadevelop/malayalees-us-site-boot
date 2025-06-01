package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventDetailsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventDetails}.
 */
public interface EventDetailsService {
    /**
     * Save a eventDetails.
     *
     * @param eventDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    EventDetailsDTO save(EventDetailsDTO eventDetailsDTO);

    /**
     * Updates a eventDetails.
     *
     * @param eventDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    EventDetailsDTO update(EventDetailsDTO eventDetailsDTO);

    /**
     * Partially updates a eventDetails.
     *
     * @param eventDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventDetailsDTO> partialUpdate(EventDetailsDTO eventDetailsDTO);

    /**
     * Get all the eventDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" eventDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
