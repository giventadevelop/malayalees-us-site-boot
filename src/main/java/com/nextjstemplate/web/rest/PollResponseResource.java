package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.PollResponseRepository;
import com.nextjstemplate.service.PollResponseQueryService;
import com.nextjstemplate.service.PollResponseService;
import com.nextjstemplate.service.criteria.PollResponseCriteria;
import com.nextjstemplate.service.dto.PollResponseDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.PollResponse}.
 */
@RestController
@RequestMapping("/api/poll-responses")
public class PollResponseResource {

    private final Logger log = LoggerFactory.getLogger(PollResponseResource.class);

    private static final String ENTITY_NAME = "pollResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PollResponseService pollResponseService;

    private final PollResponseRepository pollResponseRepository;

    private final PollResponseQueryService pollResponseQueryService;

    public PollResponseResource(
        PollResponseService pollResponseService,
        PollResponseRepository pollResponseRepository,
        PollResponseQueryService pollResponseQueryService
    ) {
        this.pollResponseService = pollResponseService;
        this.pollResponseRepository = pollResponseRepository;
        this.pollResponseQueryService = pollResponseQueryService;
    }

    /**
     * {@code POST  /poll-responses} : Create a new pollResponse.
     *
     * @param pollResponseDTO the pollResponseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pollResponseDTO, or with status {@code 400 (Bad Request)} if the pollResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PollResponseDTO> createPollResponse(@Valid @RequestBody PollResponseDTO pollResponseDTO)
        throws URISyntaxException {
        log.debug("REST request to save PollResponse : {}", pollResponseDTO);
        if (pollResponseDTO.getId() != null) {
            throw new BadRequestAlertException("A new pollResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PollResponseDTO result = pollResponseService.save(pollResponseDTO);
        return ResponseEntity
            .created(new URI("/api/poll-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poll-responses/:id} : Updates an existing pollResponse.
     *
     * @param id the id of the pollResponseDTO to save.
     * @param pollResponseDTO the pollResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollResponseDTO,
     * or with status {@code 400 (Bad Request)} if the pollResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pollResponseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PollResponseDTO> updatePollResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PollResponseDTO pollResponseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PollResponse : {}, {}", id, pollResponseDTO);
        if (pollResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollResponseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PollResponseDTO result = pollResponseService.update(pollResponseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollResponseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poll-responses/:id} : Partial updates given fields of an existing pollResponse, field will ignore if it is null
     *
     * @param id the id of the pollResponseDTO to save.
     * @param pollResponseDTO the pollResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollResponseDTO,
     * or with status {@code 400 (Bad Request)} if the pollResponseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pollResponseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pollResponseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PollResponseDTO> partialUpdatePollResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PollResponseDTO pollResponseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PollResponse partially : {}, {}", id, pollResponseDTO);
        if (pollResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollResponseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PollResponseDTO> result = pollResponseService.partialUpdate(pollResponseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollResponseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /poll-responses} : get all the pollResponses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pollResponses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PollResponseDTO>> getAllPollResponses(
        PollResponseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PollResponses by criteria: {}", criteria);

        Page<PollResponseDTO> page = pollResponseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /poll-responses/count} : count all the pollResponses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPollResponses(PollResponseCriteria criteria) {
        log.debug("REST request to count PollResponses by criteria: {}", criteria);
        return ResponseEntity.ok().body(pollResponseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /poll-responses/:id} : get the "id" pollResponse.
     *
     * @param id the id of the pollResponseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pollResponseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PollResponseDTO> getPollResponse(@PathVariable Long id) {
        log.debug("REST request to get PollResponse : {}", id);
        Optional<PollResponseDTO> pollResponseDTO = pollResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pollResponseDTO);
    }

    /**
     * {@code DELETE  /poll-responses/:id} : delete the "id" pollResponse.
     *
     * @param id the id of the pollResponseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePollResponse(@PathVariable Long id) {
        log.debug("REST request to delete PollResponse : {}", id);
        pollResponseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
