package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.EventTicketTransactionQueryService;
import com.nextjstemplate.service.EventTicketTransactionService;
import com.nextjstemplate.service.EmailSenderService;
import com.nextjstemplate.service.criteria.EventTicketTransactionCriteria;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
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
 * REST controller for managing
 * {@link com.nextjstemplate.domain.EventTicketTransaction}.
 */
@RestController
@RequestMapping("/api/event-ticket-transactions")
public class EventTicketTransactionResource {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionResource.class);

    private static final String ENTITY_NAME = "eventTicketTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTicketTransactionService eventTicketTransactionService;

    private final EventTicketTransactionRepository eventTicketTransactionRepository;

    private final EventTicketTransactionQueryService eventTicketTransactionQueryService;

    private final EmailSenderService emailSenderService;

    public EventTicketTransactionResource(
            EventTicketTransactionService eventTicketTransactionService,
            EventTicketTransactionRepository eventTicketTransactionRepository,
            EventTicketTransactionQueryService eventTicketTransactionQueryService,
            EmailSenderService emailSenderService) {
        this.eventTicketTransactionService = eventTicketTransactionService;
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionQueryService = eventTicketTransactionQueryService;
        this.emailSenderService = emailSenderService;
    }

    /**
     * {@code POST  /event-ticket-transactions} : Create a new
     * eventTicketTransaction.
     *
     * @param eventTicketTransactionDTO the eventTicketTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new eventTicketTransactionDTO, or with status
     *         {@code 400 (Bad Request)} if the eventTicketTransaction has already
     *         an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventTicketTransactionDTO> createEventTicketTransaction(
            @Valid @RequestBody EventTicketTransactionDTO eventTicketTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save EventTicketTransaction : {}", eventTicketTransactionDTO);
        if (eventTicketTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventTicketTransaction cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }
        EventTicketTransactionDTO result = eventTicketTransactionService.save(eventTicketTransactionDTO);
        // Send confirmation email (non-blocking, log error if fails)
        try {
            String to = result.getEmail();
            String subject = "Your Ticket Purchase Confirmation";
            String eventName =  "Event";
            String body = String.format(
                    "Dear %s,\n\nThank you for your purchase!\n\nEvent: %s\nTickets: %d\nTotal Paid: %s\n\nTransaction Ref: %s\n\nSee you at the event!",
                    result.getFirstName() != null ? result.getFirstName() : "Customer",
                    eventName,
                    result.getQuantity(),
                    result.getFinalAmount(),
                    result.getTransactionReference());
            emailSenderService.sendEmail(to, subject, body);
        } catch (Exception e) {
            log.error("Failed to send confirmation email for ticket transaction {}: {}", result.getId(), e.getMessage(),
                    e);
        }
        return ResponseEntity
                .created(new URI("/api/event-ticket-transactions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /event-ticket-transactions/:id} : Updates an existing
     * eventTicketTransaction.
     *
     * @param id                        the id of the eventTicketTransactionDTO to
     *                                  save.
     * @param eventTicketTransactionDTO the eventTicketTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventTicketTransactionDTO,
     *         or with status {@code 400 (Bad Request)} if the
     *         eventTicketTransactionDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventTicketTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventTicketTransactionDTO> updateEventTicketTransaction(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody EventTicketTransactionDTO eventTicketTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update EventTicketTransaction : {}, {}", id, eventTicketTransactionDTO);
        if (eventTicketTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventTicketTransactionDTO result = eventTicketTransactionService.update(eventTicketTransactionDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventTicketTransactionDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /event-ticket-transactions/:id} : Partial updates given fields
     * of an existing eventTicketTransaction, field will ignore if it is null
     *
     * @param id                        the id of the eventTicketTransactionDTO to
     *                                  save.
     * @param eventTicketTransactionDTO the eventTicketTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventTicketTransactionDTO,
     *         or with status {@code 400 (Bad Request)} if the
     *         eventTicketTransactionDTO is not valid,
     *         or with status {@code 404 (Not Found)} if the
     *         eventTicketTransactionDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventTicketTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventTicketTransactionDTO> partialUpdateEventTicketTransaction(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody EventTicketTransactionDTO eventTicketTransactionDTO) throws URISyntaxException {
        log.debug("REST request to partial update EventTicketTransaction partially : {}, {}", id,
                eventTicketTransactionDTO);
        if (eventTicketTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventTicketTransactionDTO> result = eventTicketTransactionService
                .partialUpdate(eventTicketTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventTicketTransactionDTO.getId().toString()));
    }

    /**
     * {@code GET  /event-ticket-transactions} : get all the
     * eventTicketTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of eventTicketTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventTicketTransactionDTO>> getAllEventTicketTransactions(
            EventTicketTransactionCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get EventTicketTransactions by criteria: {}", criteria);

        Page<EventTicketTransactionDTO> page = eventTicketTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-ticket-transactions/count} : count all the
     * eventTicketTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventTicketTransactions(EventTicketTransactionCriteria criteria) {
        log.debug("REST request to count EventTicketTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTicketTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-ticket-transactions/:id} : get the "id"
     * eventTicketTransaction.
     *
     * @param id the id of the eventTicketTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the eventTicketTransactionDTO, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventTicketTransactionDTO> getEventTicketTransaction(@PathVariable Long id) {
        log.debug("REST request to get EventTicketTransaction : {}", id);
        Optional<EventTicketTransactionDTO> eventTicketTransactionDTO = eventTicketTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTicketTransactionDTO);
    }

    /**
     * {@code DELETE  /event-ticket-transactions/:id} : delete the "id"
     * eventTicketTransaction.
     *
     * @param id the id of the eventTicketTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventTicketTransaction(@PathVariable Long id) {
        log.debug("REST request to delete EventTicketTransaction : {}", id);
        eventTicketTransactionService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
