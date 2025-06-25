package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.WhatsAppLogRepository;
import com.nextjstemplate.service.WhatsAppLogQueryService;
import com.nextjstemplate.service.WhatsAppLogService;
import com.nextjstemplate.service.criteria.WhatsAppLogCriteria;
import com.nextjstemplate.service.dto.WhatsAppLogDTO;
import com.nextjstemplate.service.WhatsAppSenderService;
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
import org.springframework.beans.factory.annotation.Autowired;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.WhatsAppLog}.
 */
@RestController
@RequestMapping("/api/whats-app-logs")
public class WhatsAppLogResource {

    private final Logger log = LoggerFactory.getLogger(WhatsAppLogResource.class);

    private static final String ENTITY_NAME = "whatsAppLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WhatsAppLogService whatsAppLogService;

    private final WhatsAppLogRepository whatsAppLogRepository;

    private final WhatsAppLogQueryService whatsAppLogQueryService;

    private final WhatsAppSenderService whatsAppSenderService;

    @Autowired
    public WhatsAppLogResource(
            WhatsAppLogService whatsAppLogService,
            WhatsAppLogRepository whatsAppLogRepository,
            WhatsAppLogQueryService whatsAppLogQueryService,
            WhatsAppSenderService whatsAppSenderService) {
        this.whatsAppLogService = whatsAppLogService;
        this.whatsAppLogRepository = whatsAppLogRepository;
        this.whatsAppLogQueryService = whatsAppLogQueryService;
        this.whatsAppSenderService = whatsAppSenderService;
    }

    /**
     * {@code POST  /whats-app-logs} : Create a new whatsAppLog.
     *
     * @param whatsAppLogDTO the whatsAppLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new whatsAppLogDTO, or with status {@code 400 (Bad Request)}
     *         if the whatsAppLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WhatsAppLogDTO> createWhatsAppLog(@Valid @RequestBody WhatsAppLogDTO whatsAppLogDTO)
            throws URISyntaxException {
        log.debug("REST request to save WhatsAppLog : {}", whatsAppLogDTO);
        if (whatsAppLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new whatsAppLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WhatsAppLogDTO result = whatsAppLogService.save(whatsAppLogDTO);
        return ResponseEntity
                .created(new URI("/api/whats-app-logs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /whats-app-logs/:id} : Updates an existing whatsAppLog.
     *
     * @param id             the id of the whatsAppLogDTO to save.
     * @param whatsAppLogDTO the whatsAppLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated whatsAppLogDTO,
     *         or with status {@code 400 (Bad Request)} if the whatsAppLogDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         whatsAppLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WhatsAppLogDTO> updateWhatsAppLog(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody WhatsAppLogDTO whatsAppLogDTO) throws URISyntaxException {
        log.debug("REST request to update WhatsAppLog : {}, {}", id, whatsAppLogDTO);
        if (whatsAppLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, whatsAppLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!whatsAppLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WhatsAppLogDTO result = whatsAppLogService.update(whatsAppLogDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        whatsAppLogDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /whats-app-logs/:id} : Partial updates given fields of an
     * existing whatsAppLog, field will ignore if it is null
     *
     * @param id             the id of the whatsAppLogDTO to save.
     * @param whatsAppLogDTO the whatsAppLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated whatsAppLogDTO,
     *         or with status {@code 400 (Bad Request)} if the whatsAppLogDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the whatsAppLogDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         whatsAppLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WhatsAppLogDTO> partialUpdateWhatsAppLog(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody WhatsAppLogDTO whatsAppLogDTO) throws URISyntaxException {
        log.debug("REST request to partial update WhatsAppLog partially : {}, {}", id, whatsAppLogDTO);
        if (whatsAppLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, whatsAppLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!whatsAppLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WhatsAppLogDTO> result = whatsAppLogService.partialUpdate(whatsAppLogDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        whatsAppLogDTO.getId().toString()));
    }

    /**
     * {@code GET  /whats-app-logs} : get all the whatsAppLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of whatsAppLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WhatsAppLogDTO>> getAllWhatsAppLogs(
            WhatsAppLogCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get WhatsAppLogs by criteria: {}", criteria);

        Page<WhatsAppLogDTO> page = whatsAppLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /whats-app-logs/count} : count all the whatsAppLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWhatsAppLogs(WhatsAppLogCriteria criteria) {
        log.debug("REST request to count WhatsAppLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(whatsAppLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /whats-app-logs/:id} : get the "id" whatsAppLog.
     *
     * @param id the id of the whatsAppLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the whatsAppLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WhatsAppLogDTO> getWhatsAppLog(@PathVariable Long id) {
        log.debug("REST request to get WhatsAppLog : {}", id);
        Optional<WhatsAppLogDTO> whatsAppLogDTO = whatsAppLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(whatsAppLogDTO);
    }

    /**
     * {@code DELETE  /whats-app-logs/:id} : delete the "id" whatsAppLog.
     *
     * @param id the id of the whatsAppLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWhatsAppLog(@PathVariable Long id) {
        log.debug("REST request to delete WhatsAppLog : {}", id);
        whatsAppLogService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * {@code POST  /whats-app-logs/test-send} : Send a test WhatsApp message using
     * Twilio.
     *
     * @param to the recipient phone number (in E.164 format, e.g., +12345556789)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if sent.
     */
    @PostMapping("/test-send")
    public ResponseEntity<String> testSendWhatsApp(@RequestParam(value = "to", required = false) String to) {
        String recipient = (to != null && !to.isBlank()) ? to : "+12345556789"; // Replace with a valid test number
        String userName = "Test User";
        String eventName = "Music Night 2025";
        String eventDate = "July 20, 2025";
        String eventVenue = "Grand Hall, City Center";
        String template = "Hello {{userName}},\nYour ticket for {{eventName}} on {{eventDate}} at {{eventVenue}} is confirmed!\nThank you for your purchase.";
        String messageBody = template
                .replace("{{userName}}", userName)
                .replace("{{eventName}}", eventName)
                .replace("{{eventDate}}", eventDate)
                .replace("{{eventVenue}}", eventVenue);
        try {
            String sid = whatsAppSenderService.sendMessage(recipient, messageBody);
            return ResponseEntity.ok("Test WhatsApp message sent to " + recipient + ". SID: " + sid);
        } catch (Exception e) {
            log.error("Failed to send test WhatsApp message: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to send test WhatsApp message: " + e.getMessage());
        }
    }

    /**
     * {@code POST  /whats-app-logs/test-send-image} : Send a test WhatsApp message
     * with an image using Twilio.
     *
     * @param to the recipient phone number (in E.164 format, e.g., +12345556789)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if sent.
     */
    @PostMapping("/test-send-image")
    public ResponseEntity<String> testSendWhatsAppWithImage(@RequestParam(value = "to", required = false) String to) {
        String recipient = (to != null && !to.isBlank()) ? to : "+12345556789"; // Replace with a valid test number
        String userName = "Test User";
        String eventName = "Music Night 2025";
        String eventDate = "July 20, 2025";
        String eventVenue = "Grand Hall, City Center";
        String template = "Hello {{userName}},\nYour ticket for {{eventName}} on {{eventDate}} at {{eventVenue}} is confirmed!\nThank you for your purchase.";
        String messageBody = template
                .replace("{{userName}}", userName)
                .replace("{{eventName}}", eventName)
                .replace("{{eventDate}}", eventDate)
                .replace("{{eventVenue}}", eventVenue);
        String imageUrl = "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/event-id/1/email-templates/event-poster-music-event.jpg";
        try {
            String sid = whatsAppSenderService.sendMessageWithImage(recipient, messageBody, imageUrl);
            return ResponseEntity.ok("Test WhatsApp message with image sent to " + recipient + ". SID: " + sid);
        } catch (Exception e) {
            log.error("Failed to send test WhatsApp message with image: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Failed to send test WhatsApp message with image: " + e.getMessage());
        }
    }
}
