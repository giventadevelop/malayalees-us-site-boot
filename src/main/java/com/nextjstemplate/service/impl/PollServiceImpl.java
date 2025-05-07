package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.repository.PollRepository;
import com.nextjstemplate.service.PollService;
import com.nextjstemplate.service.dto.PollDTO;
import com.nextjstemplate.service.mapper.PollMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.Poll}.
 */
@Service
@Transactional
public class PollServiceImpl implements PollService {

    private final Logger log = LoggerFactory.getLogger(PollServiceImpl.class);

    private final PollRepository pollRepository;

    private final PollMapper pollMapper;

    public PollServiceImpl(PollRepository pollRepository, PollMapper pollMapper) {
        this.pollRepository = pollRepository;
        this.pollMapper = pollMapper;
    }

    @Override
    public PollDTO save(PollDTO pollDTO) {
        log.debug("Request to save Poll : {}", pollDTO);
        Poll poll = pollMapper.toEntity(pollDTO);
        poll = pollRepository.save(poll);
        return pollMapper.toDto(poll);
    }

    @Override
    public PollDTO update(PollDTO pollDTO) {
        log.debug("Request to update Poll : {}", pollDTO);
        Poll poll = pollMapper.toEntity(pollDTO);
        poll = pollRepository.save(poll);
        return pollMapper.toDto(poll);
    }

    @Override
    public Optional<PollDTO> partialUpdate(PollDTO pollDTO) {
        log.debug("Request to partially update Poll : {}", pollDTO);

        return pollRepository
            .findById(pollDTO.getId())
            .map(existingPoll -> {
                pollMapper.partialUpdate(existingPoll, pollDTO);

                return existingPoll;
            })
            .map(pollRepository::save)
            .map(pollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PollDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Polls");
        return pollRepository.findAll(pageable).map(pollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PollDTO> findOne(Long id) {
        log.debug("Request to get Poll : {}", id);
        return pollRepository.findById(id).map(pollMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Poll : {}", id);
        pollRepository.deleteById(id);
    }
}
