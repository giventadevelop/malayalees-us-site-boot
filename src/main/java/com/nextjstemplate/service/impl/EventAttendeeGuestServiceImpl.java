package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventAttendeeGuest;
import com.nextjstemplate.repository.EventAttendeeGuestRepository;
import com.nextjstemplate.service.EventAttendeeGuestService;
import com.nextjstemplate.service.dto.EventAttendeeGuestDTO;
import com.nextjstemplate.service.mapper.EventAttendeeGuestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventAttendeeGuest}.
 */
@Service
@Transactional
public class EventAttendeeGuestServiceImpl implements EventAttendeeGuestService {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeGuestServiceImpl.class);

    private final EventAttendeeGuestRepository eventAttendeeGuestRepository;

    private final EventAttendeeGuestMapper eventAttendeeGuestMapper;

    public EventAttendeeGuestServiceImpl(
        EventAttendeeGuestRepository eventAttendeeGuestRepository,
        EventAttendeeGuestMapper eventAttendeeGuestMapper
    ) {
        this.eventAttendeeGuestRepository = eventAttendeeGuestRepository;
        this.eventAttendeeGuestMapper = eventAttendeeGuestMapper;
    }

    @Override
    public EventAttendeeGuestDTO save(EventAttendeeGuestDTO eventAttendeeGuestDTO) {
        log.debug("Request to save EventAttendeeGuest : {}", eventAttendeeGuestDTO);
        EventAttendeeGuest eventAttendeeGuest = eventAttendeeGuestMapper.toEntity(eventAttendeeGuestDTO);
        eventAttendeeGuest = eventAttendeeGuestRepository.save(eventAttendeeGuest);
        return eventAttendeeGuestMapper.toDto(eventAttendeeGuest);
    }

    @Override
    public EventAttendeeGuestDTO update(EventAttendeeGuestDTO eventAttendeeGuestDTO) {
        log.debug("Request to update EventAttendeeGuest : {}", eventAttendeeGuestDTO);
        EventAttendeeGuest eventAttendeeGuest = eventAttendeeGuestMapper.toEntity(eventAttendeeGuestDTO);
        eventAttendeeGuest = eventAttendeeGuestRepository.save(eventAttendeeGuest);
        return eventAttendeeGuestMapper.toDto(eventAttendeeGuest);
    }

    @Override
    public Optional<EventAttendeeGuestDTO> partialUpdate(EventAttendeeGuestDTO eventAttendeeGuestDTO) {
        log.debug("Request to partially update EventAttendeeGuest : {}", eventAttendeeGuestDTO);

        return eventAttendeeGuestRepository
            .findById(eventAttendeeGuestDTO.getId())
            .map(existingEventAttendeeGuest -> {
                eventAttendeeGuestMapper.partialUpdate(existingEventAttendeeGuest, eventAttendeeGuestDTO);

                return existingEventAttendeeGuest;
            })
            .map(eventAttendeeGuestRepository::save)
            .map(eventAttendeeGuestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventAttendeeGuestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventAttendeeGuests");
        return eventAttendeeGuestRepository.findAll(pageable).map(eventAttendeeGuestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventAttendeeGuestDTO> findOne(Long id) {
        log.debug("Request to get EventAttendeeGuest : {}", id);
        return eventAttendeeGuestRepository.findById(id).map(eventAttendeeGuestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventAttendeeGuest : {}", id);
        eventAttendeeGuestRepository.deleteById(id);
    }
}
