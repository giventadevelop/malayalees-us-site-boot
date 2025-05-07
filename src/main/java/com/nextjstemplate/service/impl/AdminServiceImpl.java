package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.Admin;
import com.nextjstemplate.repository.AdminRepository;
import com.nextjstemplate.service.AdminService;
import com.nextjstemplate.service.dto.AdminDTO;
import com.nextjstemplate.service.mapper.AdminMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.Admin}.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AdminRepository adminRepository;

    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    @Override
    public AdminDTO save(AdminDTO adminDTO) {
        log.debug("Request to save Admin : {}", adminDTO);
        Admin admin = adminMapper.toEntity(adminDTO);
        admin = adminRepository.save(admin);
        return adminMapper.toDto(admin);
    }

    @Override
    public AdminDTO update(AdminDTO adminDTO) {
        log.debug("Request to update Admin : {}", adminDTO);
        Admin admin = adminMapper.toEntity(adminDTO);
        admin = adminRepository.save(admin);
        return adminMapper.toDto(admin);
    }

    @Override
    public Optional<AdminDTO> partialUpdate(AdminDTO adminDTO) {
        log.debug("Request to partially update Admin : {}", adminDTO);

        return adminRepository
            .findById(adminDTO.getId())
            .map(existingAdmin -> {
                adminMapper.partialUpdate(existingAdmin, adminDTO);

                return existingAdmin;
            })
            .map(adminRepository::save)
            .map(adminMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Admins");
        return adminRepository.findAll(pageable).map(adminMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdminDTO> findOne(Long id) {
        log.debug("Request to get Admin : {}", id);
        return adminRepository.findById(id).map(adminMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Admin : {}", id);
        adminRepository.deleteById(id);
    }
}
