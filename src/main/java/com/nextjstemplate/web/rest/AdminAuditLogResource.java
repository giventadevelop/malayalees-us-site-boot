package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.AdminAuditLogRepository;
import com.nextjstemplate.service.AdminAuditLogService;
import com.nextjstemplate.service.dto.AdminAuditLogDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.AdminAuditLog}.
 */
@RestController
@RequestMapping("/api/admin-audit-logs")
public class AdminAuditLogResource {

    private final Logger log = LoggerFactory.getLogger(AdminAuditLogResource.class);

    private static final String ENTITY_NAME = "adminAuditLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminAuditLogService adminAuditLogService;

    private final AdminAuditLogRepository adminAuditLogRepository;

    public AdminAuditLogResource(AdminAuditLogService adminAuditLogService, AdminAuditLogRepository adminAuditLogRepository) {
        this.adminAuditLogService = adminAuditLogService;
        this.adminAuditLogRepository = adminAuditLogRepository;
    }

    /**
     * {@code POST  /admin-audit-logs} : Create a new adminAuditLog.
     *
     * @param adminAuditLogDTO the adminAuditLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminAuditLogDTO, or with status {@code 400 (Bad Request)} if the adminAuditLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdminAuditLogDTO> createAdminAuditLog(@Valid @RequestBody AdminAuditLogDTO adminAuditLogDTO)
        throws URISyntaxException {
        log.debug("REST request to save AdminAuditLog : {}", adminAuditLogDTO);
        if (adminAuditLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminAuditLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdminAuditLogDTO result = adminAuditLogService.save(adminAuditLogDTO);
        return ResponseEntity
            .created(new URI("/api/admin-audit-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /admin-audit-logs/:id} : Updates an existing adminAuditLog.
     *
     * @param id the id of the adminAuditLogDTO to save.
     * @param adminAuditLogDTO the adminAuditLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminAuditLogDTO,
     * or with status {@code 400 (Bad Request)} if the adminAuditLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminAuditLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdminAuditLogDTO> updateAdminAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdminAuditLogDTO adminAuditLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AdminAuditLog : {}, {}", id, adminAuditLogDTO);
        if (adminAuditLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminAuditLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminAuditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdminAuditLogDTO result = adminAuditLogService.update(adminAuditLogDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminAuditLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /admin-audit-logs/:id} : Partial updates given fields of an existing adminAuditLog, field will ignore if it is null
     *
     * @param id the id of the adminAuditLogDTO to save.
     * @param adminAuditLogDTO the adminAuditLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminAuditLogDTO,
     * or with status {@code 400 (Bad Request)} if the adminAuditLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adminAuditLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminAuditLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdminAuditLogDTO> partialUpdateAdminAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdminAuditLogDTO adminAuditLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdminAuditLog partially : {}, {}", id, adminAuditLogDTO);
        if (adminAuditLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminAuditLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminAuditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdminAuditLogDTO> result = adminAuditLogService.partialUpdate(adminAuditLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminAuditLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /admin-audit-logs} : get all the adminAuditLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminAuditLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdminAuditLogDTO>> getAllAdminAuditLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AdminAuditLogs");
        Page<AdminAuditLogDTO> page = adminAuditLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admin-audit-logs/:id} : get the "id" adminAuditLog.
     *
     * @param id the id of the adminAuditLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminAuditLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminAuditLogDTO> getAdminAuditLog(@PathVariable Long id) {
        log.debug("REST request to get AdminAuditLog : {}", id);
        Optional<AdminAuditLogDTO> adminAuditLogDTO = adminAuditLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminAuditLogDTO);
    }

    /**
     * {@code DELETE  /admin-audit-logs/:id} : delete the "id" adminAuditLog.
     *
     * @param id the id of the adminAuditLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminAuditLog(@PathVariable Long id) {
        log.debug("REST request to delete AdminAuditLog : {}", id);
        adminAuditLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
