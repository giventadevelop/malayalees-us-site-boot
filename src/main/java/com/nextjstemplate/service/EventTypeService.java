package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventType}.
 */
public interface EventTypeService {
    /**
     * Save a eventType.
     *
     * @param eventTypeDTO the entity to save.
     * @return the persisted entity.
     */
    EventTypeDTO save(EventTypeDTO eventTypeDTO);

    /**
     * Updates a eventType.
     *
     * @param eventTypeDTO the entity to update.
     * @return the persisted entity.
     */
    EventTypeDTO update(EventTypeDTO eventTypeDTO);

    /**
     * Partially updates a eventType.
     *
     * @param eventTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventTypeDTO> partialUpdate(EventTypeDTO eventTypeDTO);

    /**
     * Get all the eventTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventTypeDTO> findOne(Long id);

    /**
     * Delete the "id" eventType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
