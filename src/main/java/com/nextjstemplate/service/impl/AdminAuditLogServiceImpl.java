package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.AdminAuditLog;
import com.nextjstemplate.repository.AdminAuditLogRepository;
import com.nextjstemplate.service.AdminAuditLogService;
import com.nextjstemplate.service.dto.AdminAuditLogDTO;
import com.nextjstemplate.service.mapper.AdminAuditLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.AdminAuditLog}.
 */
@Service
@Transactional
public class AdminAuditLogServiceImpl implements AdminAuditLogService {

    private final Logger log = LoggerFactory.getLogger(AdminAuditLogServiceImpl.class);

    private final AdminAuditLogRepository adminAuditLogRepository;

    private final AdminAuditLogMapper adminAuditLogMapper;

    public AdminAuditLogServiceImpl(AdminAuditLogRepository adminAuditLogRepository, AdminAuditLogMapper adminAuditLogMapper) {
        this.adminAuditLogRepository = adminAuditLogRepository;
        this.adminAuditLogMapper = adminAuditLogMapper;
    }

    @Override
    public AdminAuditLogDTO save(AdminAuditLogDTO adminAuditLogDTO) {
        log.debug("Request to save AdminAuditLog : {}", adminAuditLogDTO);
        AdminAuditLog adminAuditLog = adminAuditLogMapper.toEntity(adminAuditLogDTO);
        adminAuditLog = adminAuditLogRepository.save(adminAuditLog);
        return adminAuditLogMapper.toDto(adminAuditLog);
    }

    @Override
    public AdminAuditLogDTO update(AdminAuditLogDTO adminAuditLogDTO) {
        log.debug("Request to update AdminAuditLog : {}", adminAuditLogDTO);
        AdminAuditLog adminAuditLog = adminAuditLogMapper.toEntity(adminAuditLogDTO);
        adminAuditLog = adminAuditLogRepository.save(adminAuditLog);
        return adminAuditLogMapper.toDto(adminAuditLog);
    }

    @Override
    public Optional<AdminAuditLogDTO> partialUpdate(AdminAuditLogDTO adminAuditLogDTO) {
        log.debug("Request to partially update AdminAuditLog : {}", adminAuditLogDTO);

        return adminAuditLogRepository
            .findById(adminAuditLogDTO.getId())
            .map(existingAdminAuditLog -> {
                adminAuditLogMapper.partialUpdate(existingAdminAuditLog, adminAuditLogDTO);

                return existingAdminAuditLog;
            })
            .map(adminAuditLogRepository::save)
            .map(adminAuditLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminAuditLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdminAuditLogs");
        return adminAuditLogRepository.findAll(pageable).map(adminAuditLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdminAuditLogDTO> findOne(Long id) {
        log.debug("Request to get AdminAuditLog : {}", id);
        return adminAuditLogRepository.findById(id).map(adminAuditLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AdminAuditLog : {}", id);
        adminAuditLogRepository.deleteById(id);
    }
}
