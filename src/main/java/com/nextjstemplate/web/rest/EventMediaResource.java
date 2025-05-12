package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.EventMediaService;
import com.nextjstemplate.service.dto.EventMediaDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventMedia}.
 */
@RestController
@RequestMapping("/api/event-medias")
public class EventMediaResource {

    private final Logger log = LoggerFactory.getLogger(EventMediaResource.class);

    private static final String ENTITY_NAME = "eventMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventMediaService eventMediaService;

    private final EventMediaRepository eventMediaRepository;

    public EventMediaResource(EventMediaService eventMediaService, EventMediaRepository eventMediaRepository) {
        this.eventMediaService = eventMediaService;
        this.eventMediaRepository = eventMediaRepository;
    }

    /**
     * {@code POST  /event-medias} : Create a new eventMedia.
     *
     * @param eventMediaDTO the eventMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventMediaDTO, or with status {@code 400 (Bad Request)} if the eventMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventMediaDTO> createEventMedia(@Valid @RequestBody EventMediaDTO eventMediaDTO) throws URISyntaxException {
        log.debug("REST request to save EventMedia : {}", eventMediaDTO);
        if (eventMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventMediaDTO result = eventMediaService.save(eventMediaDTO);
        return ResponseEntity
            .created(new URI("/api/event-medias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-medias/:id} : Updates an existing eventMedia.
     *
     * @param id the id of the eventMediaDTO to save.
     * @param eventMediaDTO the eventMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventMediaDTO,
     * or with status {@code 400 (Bad Request)} if the eventMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventMediaDTO> updateEventMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventMediaDTO eventMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventMedia : {}, {}", id, eventMediaDTO);
        if (eventMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventMediaDTO result = eventMediaService.update(eventMediaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-medias/:id} : Partial updates given fields of an existing eventMedia, field will ignore if it is null
     *
     * @param id the id of the eventMediaDTO to save.
     * @param eventMediaDTO the eventMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventMediaDTO,
     * or with status {@code 400 (Bad Request)} if the eventMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventMediaDTO> partialUpdateEventMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventMediaDTO eventMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventMedia partially : {}, {}", id, eventMediaDTO);
        if (eventMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventMediaDTO> result = eventMediaService.partialUpdate(eventMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-medias} : get all the eventMedias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventMediaDTO>> getAllEventMedias(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EventMedias");
        Page<EventMediaDTO> page = eventMediaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-medias/:id} : get the "id" eventMedia.
     *
     * @param id the id of the eventMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventMediaDTO> getEventMedia(@PathVariable Long id) {
        log.debug("REST request to get EventMedia : {}", id);
        Optional<EventMediaDTO> eventMediaDTO = eventMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventMediaDTO);
    }

    /**
     * {@code DELETE  /event-medias/:id} : delete the "id" eventMedia.
     *
     * @param id the id of the eventMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventMedia(@PathVariable Long id) {
        log.debug("REST request to delete EventMedia : {}", id);
        eventMediaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
