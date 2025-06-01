package com.nextjstemplate.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.TicketType}.
 */
@Service
@Transactional
public class TicketTypeServiceImpl /*implements TicketTypeService*/ {/*

    private final Logger log = LoggerFactory.getLogger(TicketTypeServiceImpl.class);

    private final TicketTypeRepository ticketTypeRepository;

    private final TicketTypeMapper ticketTypeMapper;

    public TicketTypeServiceImpl(TicketTypeRepository ticketTypeRepository, TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    @Override
    public TicketTypeDTO save(TicketTypeDTO ticketTypeDTO) {
        log.debug("Request to save TicketType : {}", ticketTypeDTO);
        TicketType ticketType = ticketTypeMapper.toEntity(ticketTypeDTO);
        ticketType = ticketTypeRepository.save(ticketType);
        return ticketTypeMapper.toDto(ticketType);
    }

    @Override
    public TicketTypeDTO update(TicketTypeDTO ticketTypeDTO) {
        log.debug("Request to update TicketType : {}", ticketTypeDTO);
        TicketType ticketType = ticketTypeMapper.toEntity(ticketTypeDTO);
        ticketType = ticketTypeRepository.save(ticketType);
        return ticketTypeMapper.toDto(ticketType);
    }

    @Override
    public Optional<TicketTypeDTO> partialUpdate(TicketTypeDTO ticketTypeDTO) {
        log.debug("Request to partially update TicketType : {}", ticketTypeDTO);

        return ticketTypeRepository
            .findById(ticketTypeDTO.getId())
            .map(existingTicketType -> {
                ticketTypeMapper.partialUpdate(existingTicketType, ticketTypeDTO);

                return existingTicketType;
            })
            .map(ticketTypeRepository::save)
            .map(ticketTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TicketTypes");
        return ticketTypeRepository.findAll(pageable).map(ticketTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketTypeDTO> findOne(Long id) {
        log.debug("Request to get TicketType : {}", id);
        return ticketTypeRepository.findById(id).map(ticketTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TicketType : {}", id);
        ticketTypeRepository.deleteById(id);
    }*/
}
