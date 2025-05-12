package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.CalendarEventRepository;
import com.nextjstemplate.service.CalendarEventService;
import com.nextjstemplate.service.dto.CalendarEventDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.CalendarEvent}.
 */
@RestController
@RequestMapping("/api/calendar-events")
public class CalendarEventResource {

    private final Logger log = LoggerFactory.getLogger(CalendarEventResource.class);

    private static final String ENTITY_NAME = "calendarEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendarEventService calendarEventService;

    private final CalendarEventRepository calendarEventRepository;

    public CalendarEventResource(CalendarEventService calendarEventService, CalendarEventRepository calendarEventRepository) {
        this.calendarEventService = calendarEventService;
        this.calendarEventRepository = calendarEventRepository;
    }

    /**
     * {@code POST  /calendar-events} : Create a new calendarEvent.
     *
     * @param calendarEventDTO the calendarEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendarEventDTO, or with status {@code 400 (Bad Request)} if the calendarEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CalendarEventDTO> createCalendarEvent(@Valid @RequestBody CalendarEventDTO calendarEventDTO)
        throws URISyntaxException {
        log.debug("REST request to save CalendarEvent : {}", calendarEventDTO);
        if (calendarEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new calendarEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalendarEventDTO result = calendarEventService.save(calendarEventDTO);
        return ResponseEntity
            .created(new URI("/api/calendar-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendar-events/:id} : Updates an existing calendarEvent.
     *
     * @param id the id of the calendarEventDTO to save.
     * @param calendarEventDTO the calendarEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarEventDTO,
     * or with status {@code 400 (Bad Request)} if the calendarEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendarEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CalendarEventDTO> updateCalendarEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CalendarEventDTO calendarEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CalendarEvent : {}, {}", id, calendarEventDTO);
        if (calendarEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendarEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CalendarEventDTO result = calendarEventService.update(calendarEventDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendarEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /calendar-events/:id} : Partial updates given fields of an existing calendarEvent, field will ignore if it is null
     *
     * @param id the id of the calendarEventDTO to save.
     * @param calendarEventDTO the calendarEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarEventDTO,
     * or with status {@code 400 (Bad Request)} if the calendarEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the calendarEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the calendarEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CalendarEventDTO> partialUpdateCalendarEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CalendarEventDTO calendarEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CalendarEvent partially : {}, {}", id, calendarEventDTO);
        if (calendarEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendarEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CalendarEventDTO> result = calendarEventService.partialUpdate(calendarEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendarEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /calendar-events} : get all the calendarEvents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendarEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CalendarEventDTO>> getAllCalendarEvents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CalendarEvents");
        Page<CalendarEventDTO> page = calendarEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calendar-events/:id} : get the "id" calendarEvent.
     *
     * @param id the id of the calendarEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendarEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalendarEventDTO> getCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to get CalendarEvent : {}", id);
        Optional<CalendarEventDTO> calendarEventDTO = calendarEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calendarEventDTO);
    }

    /**
     * {@code DELETE  /calendar-events/:id} : delete the "id" calendarEvent.
     *
     * @param id the id of the calendarEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to delete CalendarEvent : {}", id);
        calendarEventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
