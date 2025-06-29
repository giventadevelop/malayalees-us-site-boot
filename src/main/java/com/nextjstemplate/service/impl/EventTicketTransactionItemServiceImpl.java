package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.service.EventTicketTransactionItemService;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventTicketTransactionItem}.
 */
@Service
@Transactional
public class EventTicketTransactionItemServiceImpl implements EventTicketTransactionItemService {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionItemServiceImpl.class);

    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;

    private final EventTicketTransactionItemMapper eventTicketTransactionItemMapper;

    public EventTicketTransactionItemServiceImpl(
        EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
        EventTicketTransactionItemMapper eventTicketTransactionItemMapper
    ) {
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
    }

    @Override
    public EventTicketTransactionItemDTO save(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to save EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);
        EventTicketTransactionItem eventTicketTransactionItem = eventTicketTransactionItemMapper.toEntity(eventTicketTransactionItemDTO);
        eventTicketTransactionItem = eventTicketTransactionItemRepository.save(eventTicketTransactionItem);
        return eventTicketTransactionItemMapper.toDto(eventTicketTransactionItem);
    }

    @Override
    public EventTicketTransactionItemDTO update(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to update EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);
        EventTicketTransactionItem eventTicketTransactionItem = eventTicketTransactionItemMapper.toEntity(eventTicketTransactionItemDTO);
        eventTicketTransactionItem = eventTicketTransactionItemRepository.save(eventTicketTransactionItem);
        return eventTicketTransactionItemMapper.toDto(eventTicketTransactionItem);
    }

    @Override
    public Optional<EventTicketTransactionItemDTO> partialUpdate(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to partially update EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);

        return eventTicketTransactionItemRepository
            .findById(eventTicketTransactionItemDTO.getId())
            .map(existingEventTicketTransactionItem -> {
                eventTicketTransactionItemMapper.partialUpdate(existingEventTicketTransactionItem, eventTicketTransactionItemDTO);

                return existingEventTicketTransactionItem;
            })
            .map(eventTicketTransactionItemRepository::save)
            .map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTicketTransactionItems");
        return eventTicketTransactionItemRepository.findAll(pageable).map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTicketTransactionItemDTO> findOne(Long id) {
        log.debug("Request to get EventTicketTransactionItem : {}", id);
        return eventTicketTransactionItemRepository.findById(id).map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventTicketTransactionItem : {}", id);
        eventTicketTransactionItemRepository.deleteById(id);
    }
}
