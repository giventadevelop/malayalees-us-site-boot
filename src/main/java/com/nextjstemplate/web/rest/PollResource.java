package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.PollRepository;
import com.nextjstemplate.service.PollService;
import com.nextjstemplate.service.dto.PollDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.Poll}.
 */
@RestController
@RequestMapping("/api/polls")
public class PollResource {

    private final Logger log = LoggerFactory.getLogger(PollResource.class);

    private static final String ENTITY_NAME = "poll";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PollService pollService;

    private final PollRepository pollRepository;

    public PollResource(PollService pollService, PollRepository pollRepository) {
        this.pollService = pollService;
        this.pollRepository = pollRepository;
    }

    /**
     * {@code POST  /polls} : Create a new poll.
     *
     * @param pollDTO the pollDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pollDTO, or with status {@code 400 (Bad Request)} if the poll has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PollDTO> createPoll(@Valid @RequestBody PollDTO pollDTO) throws URISyntaxException {
        log.debug("REST request to save Poll : {}", pollDTO);
        if (pollDTO.getId() != null) {
            throw new BadRequestAlertException("A new poll cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PollDTO result = pollService.save(pollDTO);
        return ResponseEntity
            .created(new URI("/api/polls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /polls/:id} : Updates an existing poll.
     *
     * @param id the id of the pollDTO to save.
     * @param pollDTO the pollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollDTO,
     * or with status {@code 400 (Bad Request)} if the pollDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PollDTO> updatePoll(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PollDTO pollDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Poll : {}, {}", id, pollDTO);
        if (pollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PollDTO result = pollService.update(pollDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /polls/:id} : Partial updates given fields of an existing poll, field will ignore if it is null
     *
     * @param id the id of the pollDTO to save.
     * @param pollDTO the pollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollDTO,
     * or with status {@code 400 (Bad Request)} if the pollDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pollDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PollDTO> partialUpdatePoll(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PollDTO pollDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Poll partially : {}, {}", id, pollDTO);
        if (pollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PollDTO> result = pollService.partialUpdate(pollDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /polls} : get all the polls.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of polls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PollDTO>> getAllPolls(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Polls");
        Page<PollDTO> page = pollService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /polls/:id} : get the "id" poll.
     *
     * @param id the id of the pollDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pollDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PollDTO> getPoll(@PathVariable Long id) {
        log.debug("REST request to get Poll : {}", id);
        Optional<PollDTO> pollDTO = pollService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pollDTO);
    }

    /**
     * {@code DELETE  /polls/:id} : delete the "id" poll.
     *
     * @param id the id of the pollDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        log.debug("REST request to delete Poll : {}", id);
        pollService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
