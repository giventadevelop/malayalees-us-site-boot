package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.EventMediaQueryService;
import com.nextjstemplate.service.EventMediaService;
import com.nextjstemplate.service.criteria.EventMediaCriteria;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

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

    private final EventMediaQueryService eventMediaQueryService;

    public EventMediaResource(
            EventMediaService eventMediaService,
            EventMediaRepository eventMediaRepository,
            EventMediaQueryService eventMediaQueryService) {
        this.eventMediaService = eventMediaService;
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaQueryService = eventMediaQueryService;
    }

    /**
     * {@code POST  /event-medias} : Create a new eventMedia.
     *
     * @param eventMediaDTO the eventMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new eventMediaDTO, or with status {@code 400 (Bad Request)}
     *         if the eventMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventMediaDTO> createEventMedia(@Valid @RequestBody EventMediaDTO eventMediaDTO)
            throws URISyntaxException {
        log.debug("REST request to save EventMedia : {}", eventMediaDTO);
        if (eventMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventMediaDTO result = eventMediaService.save(eventMediaDTO);
        return ResponseEntity
                .created(new URI("/api/event-medias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /event-medias/:id} : Updates an existing eventMedia.
     *
     * @param id            the id of the eventMediaDTO to save.
     * @param eventMediaDTO the eventMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventMediaDTO,
     *         or with status {@code 400 (Bad Request)} if the eventMediaDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventMediaDTO> updateEventMedia(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody EventMediaDTO eventMediaDTO) throws URISyntaxException {
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
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventMediaDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /event-medias/:id} : Partial updates given fields of an
     * existing eventMedia, field will ignore if it is null
     *
     * @param id            the id of the eventMediaDTO to save.
     * @param eventMediaDTO the eventMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventMediaDTO,
     *         or with status {@code 400 (Bad Request)} if the eventMediaDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the eventMediaDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventMediaDTO> partialUpdateEventMedia(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody EventMediaDTO eventMediaDTO) throws URISyntaxException {
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
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventMediaDTO.getId().toString()));
    }

    /**
     * {@code GET  /event-medias} : get all the eventMedias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of eventMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventMediaDTO>> getAllEventMedias(
            EventMediaCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get EventMedias by criteria: {}", criteria);

        Page<EventMediaDTO> page = eventMediaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-medias/count} : count all the eventMedias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventMedias(EventMediaCriteria criteria) {
        log.debug("REST request to count EventMedias by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventMediaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-medias/:id} : get the "id" eventMedia.
     *
     * @param id the id of the eventMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the eventMediaDTO, or with status {@code 404 (Not Found)}.
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

    // --- Begin: S3/Upload endpoints migrated from EventMediaUploadResource ---

    /**
     * POST /event-medias/upload : Upload a single media file to S3.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventMediaDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "eventId", required = false) Long eventId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            @RequestParam(value = "eventFlyer", required = false) Boolean eventFlyer,
            @RequestParam(value = "isEventManagementOfficialDocument", required = false) Boolean isEventManagementOfficialDocument,
            Authentication authentication) throws URISyntaxException {
        log.debug("REST request to upload EventMedia file: {} for event: {}", file.getOriginalFilename(), eventId);
        if (file.isEmpty()) {
            throw new BadRequestAlertException("File cannot be empty", ENTITY_NAME, "fileempty");
        }
        Long userProfileId = getCurrentUserProfileId(authentication);
        boolean isPublicValue = isPublic != null ? isPublic : false;
        EventMediaDTO result = eventMediaService.uploadFile(file, eventId, userProfileId, title, description,
                isPublicValue, eventFlyer, isEventManagementOfficialDocument);
        return ResponseEntity
                .created(new URI("/api/event-medias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * POST /event-medias/upload-multiple : Upload multiple media files to S3.
     */
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<EventMediaDTO>> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "eventId", required = false) Long eventId,
            @RequestParam(value = "titles", required = false) List<String> titles,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            @RequestParam(value = "eventFlyer", required = false) Boolean eventFlyer,
            @RequestParam(value = "isEventManagementOfficialDocument", required = false) Boolean isEventManagementOfficialDocument,
            Authentication authentication) {
        log.debug("REST request to upload {} EventMedia files for event: {}", files.size(), eventId);
        if (files.isEmpty()) {
            throw new BadRequestAlertException("Files list cannot be empty", ENTITY_NAME, "filesempty");
        }
        boolean hasEmptyFile = files.stream().anyMatch(MultipartFile::isEmpty);
        if (hasEmptyFile) {
            throw new BadRequestAlertException("One or more files are empty", ENTITY_NAME, "fileempty");
        }
        Long userProfileId = getCurrentUserProfileId(authentication);
        boolean isPublicValue = isPublic != null ? isPublic : false;
        List<EventMediaDTO> results = eventMediaService.uploadMultipleFiles(files, eventId, userProfileId, titles,
                descriptions, isPublicValue, eventFlyer, isEventManagementOfficialDocument);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, "eventMedia.uploaded", String.valueOf(results.size())))
                .body(results);
    }

    /**
     * GET /event-medias/event/{eventId}/view : Get EventMedias for an event with
     * viewing URLs.
     */
    @GetMapping("/event/{eventId}/view")
    public ResponseEntity<List<EventMediaDTO>> getEventMediaForViewing(
            @PathVariable Long eventId,
            @RequestParam(value = "includePrivate", defaultValue = "false") boolean includePrivate,
            Authentication authentication) {
        log.debug("REST request to get EventMedias with viewing URLs for event: {}", eventId);
        Long userProfileId = getCurrentUserProfileId(authentication);
        List<EventMediaDTO> eventMedia = eventMediaService.getEventMediaWithUrls(eventId, userProfileId,
                includePrivate);
        return ResponseEntity.ok(eventMedia);
    }

    /**
     * GET /event-medias/{id}/download-url : Get download URL (presigned) for a
     * specific EventMedia.
     */
    @GetMapping("/{id}/download-url")
    public ResponseEntity<Map<String, Object>> getDownloadUrl(
            @PathVariable Long id,
            @RequestParam(value = "expirationHours", defaultValue = "2") int expirationHours,
            Authentication authentication) {
        log.debug("REST request to get download URL for EventMedia: {}", id);
        Long userProfileId = getCurrentUserProfileId(authentication);
        String downloadUrl = eventMediaService.getViewingUrl(id, userProfileId);
        Map<String, Object> response = new HashMap<>();
        response.put("downloadUrl", downloadUrl);
        response.put("mediaId", id);
        response.put("expiresAt", Instant.now().plusSeconds(expirationHours * 3600));
        return ResponseEntity.ok(response);
    }

    /**
     * POST /event-medias/refresh-urls : Refresh presigned URLs for multiple
     * EventMedias.
     */
    @PostMapping("/refresh-urls")
    public ResponseEntity<List<Map<String, Object>>> refreshUrls(
            @RequestBody UrlRefreshRequest request,
            Authentication authentication) {
        log.debug("REST request to refresh URLs for {} EventMedias", request.getMediaIds().size());
        Long userProfileId = getCurrentUserProfileId(authentication);
        List<Map<String, Object>> refreshedMedia = request.getMediaIds().stream()
                .map(mediaId -> {
                    try {
                        String newUrl = eventMediaService.getViewingUrl(mediaId, userProfileId);
                        Map<String, Object> dto = new HashMap<>();
                        dto.put("id", mediaId);
                        dto.put("viewingUrl", newUrl);
                        dto.put("expiresAt", Instant.now().plusSeconds(request.getExpirationHours() * 3600));
                        return dto;
                    } catch (Exception e) {
                        log.error("Error refreshing URL for EventMedia: {}", mediaId, e);
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        return ResponseEntity.ok(refreshedMedia);
    }

    // Helper method for authentication
    private Long getCurrentUserProfileId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new SecurityException("Authentication required");
        }
        // Implement based on your authentication mechanism
        // Example implementations:
        // For custom UserPrincipal:
        // if (authentication.getPrincipal() instanceof UserPrincipal) {
        // UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        // return principal.getUserProfileId();
        // }
        // For JWT with custom claims:
        // if (authentication instanceof JwtAuthenticationToken) {
        // JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        // String userProfileId =
        // jwtToken.getTokenAttributes().get("userProfileId").toString();
        // return Long.parseLong(userProfileId);
        // }
        // Placeholder implementation - replace with your actual logic
        return 1L;
    }

    // Inner class for refresh request
    public static class UrlRefreshRequest {
        private List<Long> mediaIds;
        private int expirationHours = 2;

        public List<Long> getMediaIds() {
            return mediaIds;
        }

        public void setMediaIds(List<Long> mediaIds) {
            this.mediaIds = mediaIds;
        }

        public int getExpirationHours() {
            return expirationHours;
        }

        public void setExpirationHours(int expirationHours) {
            this.expirationHours = expirationHours;
        }
    }
    // --- End: S3/Upload endpoints ---
}
