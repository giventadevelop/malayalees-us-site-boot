package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.TenantSettingsService;
import com.nextjstemplate.service.dto.TenantSettingsDTO;
import com.nextjstemplate.service.mapper.TenantSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.TenantSettings}.
 */
@Service
@Transactional
public class TenantSettingsServiceImpl implements TenantSettingsService {

    private final Logger log = LoggerFactory.getLogger(TenantSettingsServiceImpl.class);

    private final TenantSettingsRepository tenantSettingsRepository;

    private final TenantSettingsMapper tenantSettingsMapper;

    public TenantSettingsServiceImpl(TenantSettingsRepository tenantSettingsRepository, TenantSettingsMapper tenantSettingsMapper) {
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.tenantSettingsMapper = tenantSettingsMapper;
    }

    @Override
    public TenantSettingsDTO save(TenantSettingsDTO tenantSettingsDTO) {
        log.debug("Request to save TenantSettings : {}", tenantSettingsDTO);
        TenantSettings tenantSettings = tenantSettingsMapper.toEntity(tenantSettingsDTO);
        tenantSettings = tenantSettingsRepository.save(tenantSettings);
        return tenantSettingsMapper.toDto(tenantSettings);
    }

    @Override
    public TenantSettingsDTO update(TenantSettingsDTO tenantSettingsDTO) {
        log.debug("Request to update TenantSettings : {}", tenantSettingsDTO);
        TenantSettings tenantSettings = tenantSettingsMapper.toEntity(tenantSettingsDTO);
        tenantSettings = tenantSettingsRepository.save(tenantSettings);
        return tenantSettingsMapper.toDto(tenantSettings);
    }

    @Override
    public Optional<TenantSettingsDTO> partialUpdate(TenantSettingsDTO tenantSettingsDTO) {
        log.debug("Request to partially update TenantSettings : {}", tenantSettingsDTO);

        return tenantSettingsRepository
            .findById(tenantSettingsDTO.getId())
            .map(existingTenantSettings -> {
                tenantSettingsMapper.partialUpdate(existingTenantSettings, tenantSettingsDTO);

                return existingTenantSettings;
            })
            .map(tenantSettingsRepository::save)
            .map(tenantSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TenantSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TenantSettings");
        return tenantSettingsRepository.findAll(pageable).map(tenantSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TenantSettingsDTO> findOne(Long id) {
        log.debug("Request to get TenantSettings : {}", id);
        return tenantSettingsRepository.findById(id).map(tenantSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TenantSettings : {}", id);
        tenantSettingsRepository.deleteById(id);
    }
}
