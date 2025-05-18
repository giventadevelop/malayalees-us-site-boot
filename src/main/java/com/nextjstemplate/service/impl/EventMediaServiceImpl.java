package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.EventMediaService;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.service.mapper.EventMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nextjstemplate.service.S3Service;
import org.springframework.web.multipart.MultipartFile;
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.UserProfile;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.EventMedia}.
 */
@Service
@Transactional
public class EventMediaServiceImpl implements EventMediaService {

    private final Logger log = LoggerFactory.getLogger(EventMediaServiceImpl.class);

    private final EventMediaRepository eventMediaRepository;

    private final EventMediaMapper eventMediaMapper;

    private final S3Service s3Service;

    @Autowired
    public EventMediaServiceImpl(EventMediaRepository eventMediaRepository, EventMediaMapper eventMediaMapper,
            S3Service s3Service) {
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaMapper = eventMediaMapper;
        this.s3Service = s3Service;
    }

    @Override
    public EventMediaDTO save(EventMediaDTO eventMediaDTO) {
        log.debug("Request to save EventMedia : {}", eventMediaDTO);
        EventMedia eventMedia = eventMediaMapper.toEntity(eventMediaDTO);
        eventMedia = eventMediaRepository.save(eventMedia);
        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    public EventMediaDTO update(EventMediaDTO eventMediaDTO) {
        log.debug("Request to update EventMedia : {}", eventMediaDTO);
        EventMedia eventMedia = eventMediaMapper.toEntity(eventMediaDTO);
        eventMedia = eventMediaRepository.save(eventMedia);
        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    public Optional<EventMediaDTO> partialUpdate(EventMediaDTO eventMediaDTO) {
        log.debug("Request to partially update EventMedia : {}", eventMediaDTO);

        return eventMediaRepository
                .findById(eventMediaDTO.getId())
                .map(existingEventMedia -> {
                    eventMediaMapper.partialUpdate(existingEventMedia, eventMediaDTO);

                    return existingEventMedia;
                })
                .map(eventMediaRepository::save)
                .map(eventMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventMedias");
        return eventMediaRepository.findAll(pageable).map(eventMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventMediaDTO> findOne(Long id) {
        log.debug("Request to get EventMedia : {}", id);
        return eventMediaRepository.findById(id).map(eventMediaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventMedia : {}", id);
        eventMediaRepository.deleteById(id);
    }

    @Override
    public EventMediaDTO uploadFile(MultipartFile file, Long eventId, Long userProfileId, String title,
            String description, boolean isPublic) {
        // Upload to S3
        String fileUrl = s3Service.uploadFile(file, eventId, title);

        EventMedia eventMedia = new EventMedia();
        eventMedia.setTitle(title);
        eventMedia.setDescription(description);
        eventMedia.setEventMediaType(file.getContentType() != null ? file.getContentType() : "unknown");
        eventMedia.setStorageType("S3");
        eventMedia.setFileUrl(fileUrl);
        eventMedia.setContentType(file.getContentType());
        eventMedia.setFileSize((int) file.getSize());
        eventMedia.setIsPublic(isPublic);
        eventMedia.setCreatedAt(Instant.now());
        eventMedia.setUpdatedAt(Instant.now());
        // Optionally set event and uploadedBy if needed (requires fetching entities)
        // eventMedia.setEvent(...);
        // eventMedia.setUploadedBy(...);

        eventMedia = eventMediaRepository.save(eventMedia);
        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    public List<EventMediaDTO> uploadMultipleFiles(List<MultipartFile> files, Long eventId, Long userProfileId,
            List<String> titles, List<String> descriptions, boolean isPublic) {
        List<EventMediaDTO> result = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String title = (titles != null && i < titles.size()) ? titles.get(i) : file.getOriginalFilename();
            String description = (descriptions != null && i < descriptions.size()) ? descriptions.get(i) : null;
            result.add(uploadFile(file, eventId, userProfileId, title, description, isPublic));
        }
        return result;
    }

    @Override
    public List<EventMediaDTO> getEventMediaWithUrls(Long eventId, Long userProfileId, boolean includePrivate) {
        List<EventMedia> mediaList = eventMediaRepository.findByEvent_Id(eventId);
        List<EventMediaDTO> result = new ArrayList<>();
        for (EventMedia media : mediaList) {
            if (Boolean.TRUE.equals(media.getIsPublic()) || includePrivate) {
                EventMediaDTO dto = eventMediaMapper.toDto(media);
                // Set presigned viewing URL
                if (media.getFileUrl() != null && !media.getFileUrl().isEmpty()) {
                    String presignedUrl = s3Service.generatePresignedUrl(media.getFileUrl(), 2); // 2 hours default
                    dto.setFileUrl(presignedUrl);
                }
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public String getViewingUrl(Long mediaId, Long userProfileId) {
        Optional<EventMedia> mediaOpt = eventMediaRepository.findById(mediaId);
        if (mediaOpt.isPresent()) {
            EventMedia media = mediaOpt.get();
            if (media.getFileUrl() != null && !media.getFileUrl().isEmpty()) {
                return s3Service.generatePresignedUrl(media.getFileUrl(), 2); // 2 hours default
            }
        }
        return "";
    }
}
