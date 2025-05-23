package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventMediaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventMedia}.
 */
public interface EventMediaService {
        /**
         * Save a eventMedia.
         *
         * @param eventMediaDTO the entity to save.
         * @return the persisted entity.
         */
        EventMediaDTO save(EventMediaDTO eventMediaDTO);

        /**
         * Updates a eventMedia.
         *
         * @param eventMediaDTO the entity to update.
         * @return the persisted entity.
         */
        EventMediaDTO update(EventMediaDTO eventMediaDTO);

        /**
         * Partially updates a eventMedia.
         *
         * @param eventMediaDTO the entity to update partially.
         * @return the persisted entity.
         */
        Optional<EventMediaDTO> partialUpdate(EventMediaDTO eventMediaDTO);

        /**
         * Get all the eventMedias.
         *
         * @param pageable the pagination information.
         * @return the list of entities.
         */
        Page<EventMediaDTO> findAll(Pageable pageable);

        /**
         * Get the "id" eventMedia.
         *
         * @param id the id of the entity.
         * @return the entity.
         */
        Optional<EventMediaDTO> findOne(Long id);

        /**
         * Delete the "id" eventMedia.
         *
         * @param id the id of the entity.
         */
        void delete(Long id);

        /**
         * Upload a single file and create an EventMedia entry.
         */
        EventMediaDTO uploadFile(MultipartFile file, Long eventId, Long userProfileId, String title, String description,
                        boolean isPublic, Boolean eventFlyer, Boolean isEventManagementOfficialDocument);

        /**
         * Upload multiple files and create EventMedia entries.
         */
        List<EventMediaDTO> uploadMultipleFiles(List<MultipartFile> files, Long eventId, Long userProfileId,
                        List<String> titles, List<String> descriptions, boolean isPublic, Boolean eventFlyer,
                        Boolean isEventManagementOfficialDocument);

        List<EventMediaDTO> getEventMediaWithUrls(Long eventId, Long userProfileId, boolean includePrivate);

        String getViewingUrl(Long mediaId, Long userProfileId);
}
