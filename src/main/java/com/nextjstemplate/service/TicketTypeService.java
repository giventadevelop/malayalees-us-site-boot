package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.TicketTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.TicketType}.
 */
public interface TicketTypeService {
    /**
     * Save a ticketType.
     *
     * @param ticketTypeDTO the entity to save.
     * @return the persisted entity.
     */
    TicketTypeDTO save(TicketTypeDTO ticketTypeDTO);

    /**
     * Updates a ticketType.
     *
     * @param ticketTypeDTO the entity to update.
     * @return the persisted entity.
     */
    TicketTypeDTO update(TicketTypeDTO ticketTypeDTO);

    /**
     * Partially updates a ticketType.
     *
     * @param ticketTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TicketTypeDTO> partialUpdate(TicketTypeDTO ticketTypeDTO);

    /**
     * Get all the ticketTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TicketTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ticketType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketTypeDTO> findOne(Long id);

    /**
     * Delete the "id" ticketType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
