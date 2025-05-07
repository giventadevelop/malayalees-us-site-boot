package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.CalendarEvent;
import com.nextjstemplate.repository.CalendarEventRepository;
import com.nextjstemplate.service.CalendarEventService;
import com.nextjstemplate.service.dto.CalendarEventDTO;
import com.nextjstemplate.service.mapper.CalendarEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.CalendarEvent}.
 */
@Service
@Transactional
public class CalendarEventServiceImpl implements CalendarEventService {

    private final Logger log = LoggerFactory.getLogger(CalendarEventServiceImpl.class);

    private final CalendarEventRepository calendarEventRepository;

    private final CalendarEventMapper calendarEventMapper;

    public CalendarEventServiceImpl(CalendarEventRepository calendarEventRepository, CalendarEventMapper calendarEventMapper) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventMapper = calendarEventMapper;
    }

    @Override
    public CalendarEventDTO save(CalendarEventDTO calendarEventDTO) {
        log.debug("Request to save CalendarEvent : {}", calendarEventDTO);
        CalendarEvent calendarEvent = calendarEventMapper.toEntity(calendarEventDTO);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return calendarEventMapper.toDto(calendarEvent);
    }

    @Override
    public CalendarEventDTO update(CalendarEventDTO calendarEventDTO) {
        log.debug("Request to update CalendarEvent : {}", calendarEventDTO);
        CalendarEvent calendarEvent = calendarEventMapper.toEntity(calendarEventDTO);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return calendarEventMapper.toDto(calendarEvent);
    }

    @Override
    public Optional<CalendarEventDTO> partialUpdate(CalendarEventDTO calendarEventDTO) {
        log.debug("Request to partially update CalendarEvent : {}", calendarEventDTO);

        return calendarEventRepository
            .findById(calendarEventDTO.getId())
            .map(existingCalendarEvent -> {
                calendarEventMapper.partialUpdate(existingCalendarEvent, calendarEventDTO);

                return existingCalendarEvent;
            })
            .map(calendarEventRepository::save)
            .map(calendarEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CalendarEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CalendarEvents");
        return calendarEventRepository.findAll(pageable).map(calendarEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalendarEventDTO> findOne(Long id) {
        log.debug("Request to get CalendarEvent : {}", id);
        return calendarEventRepository.findById(id).map(calendarEventMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CalendarEvent : {}", id);
        calendarEventRepository.deleteById(id);
    }
}
