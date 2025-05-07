package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.PollResponse;
import com.nextjstemplate.repository.PollResponseRepository;
import com.nextjstemplate.service.PollResponseService;
import com.nextjstemplate.service.dto.PollResponseDTO;
import com.nextjstemplate.service.mapper.PollResponseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.PollResponse}.
 */
@Service
@Transactional
public class PollResponseServiceImpl implements PollResponseService {

    private final Logger log = LoggerFactory.getLogger(PollResponseServiceImpl.class);

    private final PollResponseRepository pollResponseRepository;

    private final PollResponseMapper pollResponseMapper;

    public PollResponseServiceImpl(PollResponseRepository pollResponseRepository, PollResponseMapper pollResponseMapper) {
        this.pollResponseRepository = pollResponseRepository;
        this.pollResponseMapper = pollResponseMapper;
    }

    @Override
    public PollResponseDTO save(PollResponseDTO pollResponseDTO) {
        log.debug("Request to save PollResponse : {}", pollResponseDTO);
        PollResponse pollResponse = pollResponseMapper.toEntity(pollResponseDTO);
        pollResponse = pollResponseRepository.save(pollResponse);
        return pollResponseMapper.toDto(pollResponse);
    }

    @Override
    public PollResponseDTO update(PollResponseDTO pollResponseDTO) {
        log.debug("Request to update PollResponse : {}", pollResponseDTO);
        PollResponse pollResponse = pollResponseMapper.toEntity(pollResponseDTO);
        pollResponse = pollResponseRepository.save(pollResponse);
        return pollResponseMapper.toDto(pollResponse);
    }

    @Override
    public Optional<PollResponseDTO> partialUpdate(PollResponseDTO pollResponseDTO) {
        log.debug("Request to partially update PollResponse : {}", pollResponseDTO);

        return pollResponseRepository
            .findById(pollResponseDTO.getId())
            .map(existingPollResponse -> {
                pollResponseMapper.partialUpdate(existingPollResponse, pollResponseDTO);

                return existingPollResponse;
            })
            .map(pollResponseRepository::save)
            .map(pollResponseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PollResponseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PollResponses");
        return pollResponseRepository.findAll(pageable).map(pollResponseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PollResponseDTO> findOne(Long id) {
        log.debug("Request to get PollResponse : {}", id);
        return pollResponseRepository.findById(id).map(pollResponseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PollResponse : {}", id);
        pollResponseRepository.deleteById(id);
    }
}
