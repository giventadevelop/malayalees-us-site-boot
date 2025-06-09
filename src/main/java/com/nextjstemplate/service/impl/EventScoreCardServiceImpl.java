package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventScoreCard;
import com.nextjstemplate.repository.EventScoreCardRepository;
import com.nextjstemplate.service.EventScoreCardService;
import com.nextjstemplate.service.dto.EventScoreCardDTO;
import com.nextjstemplate.service.mapper.EventScoreCardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventScoreCard}.
 */
@Service
@Transactional
public class EventScoreCardServiceImpl implements EventScoreCardService {

    private final Logger log = LoggerFactory.getLogger(EventScoreCardServiceImpl.class);

    private final EventScoreCardRepository eventScoreCardRepository;

    private final EventScoreCardMapper eventScoreCardMapper;

    public EventScoreCardServiceImpl(EventScoreCardRepository eventScoreCardRepository, EventScoreCardMapper eventScoreCardMapper) {
        this.eventScoreCardRepository = eventScoreCardRepository;
        this.eventScoreCardMapper = eventScoreCardMapper;
    }

    @Override
    public EventScoreCardDTO save(EventScoreCardDTO eventScoreCardDTO) {
        log.debug("Request to save EventScoreCard : {}", eventScoreCardDTO);
        EventScoreCard eventScoreCard = eventScoreCardMapper.toEntity(eventScoreCardDTO);
        eventScoreCard = eventScoreCardRepository.save(eventScoreCard);
        return eventScoreCardMapper.toDto(eventScoreCard);
    }

    @Override
    public EventScoreCardDTO update(EventScoreCardDTO eventScoreCardDTO) {
        log.debug("Request to update EventScoreCard : {}", eventScoreCardDTO);
        EventScoreCard eventScoreCard = eventScoreCardMapper.toEntity(eventScoreCardDTO);
        eventScoreCard = eventScoreCardRepository.save(eventScoreCard);
        return eventScoreCardMapper.toDto(eventScoreCard);
    }

    @Override
    public Optional<EventScoreCardDTO> partialUpdate(EventScoreCardDTO eventScoreCardDTO) {
        log.debug("Request to partially update EventScoreCard : {}", eventScoreCardDTO);

        return eventScoreCardRepository
            .findById(eventScoreCardDTO.getId())
            .map(existingEventScoreCard -> {
                eventScoreCardMapper.partialUpdate(existingEventScoreCard, eventScoreCardDTO);

                return existingEventScoreCard;
            })
            .map(eventScoreCardRepository::save)
            .map(eventScoreCardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventScoreCardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventScoreCards");
        return eventScoreCardRepository.findAll(pageable).map(eventScoreCardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventScoreCardDTO> findOne(Long id) {
        log.debug("Request to get EventScoreCard : {}", id);
        return eventScoreCardRepository.findById(id).map(eventScoreCardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventScoreCard : {}", id);
        eventScoreCardRepository.deleteById(id);
    }
}
