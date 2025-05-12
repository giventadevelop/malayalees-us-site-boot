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

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventMedia}.
 */
@Service
@Transactional
public class EventMediaServiceImpl implements EventMediaService {

    private final Logger log = LoggerFactory.getLogger(EventMediaServiceImpl.class);

    private final EventMediaRepository eventMediaRepository;

    private final EventMediaMapper eventMediaMapper;

    public EventMediaServiceImpl(EventMediaRepository eventMediaRepository, EventMediaMapper eventMediaMapper) {
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaMapper = eventMediaMapper;
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
}
