package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.AdminAuditLog;
import com.nextjstemplate.repository.AdminAuditLogRepository;
import com.nextjstemplate.service.criteria.AdminAuditLogCriteria;
import com.nextjstemplate.service.dto.AdminAuditLogDTO;
import com.nextjstemplate.service.mapper.AdminAuditLogMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AdminAuditLog} entities in the database.
 * The main input is a {@link AdminAuditLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdminAuditLogDTO} or a {@link Page} of {@link AdminAuditLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdminAuditLogQueryService extends QueryService<AdminAuditLog> {

    private final Logger log = LoggerFactory.getLogger(AdminAuditLogQueryService.class);

    private final AdminAuditLogRepository adminAuditLogRepository;

    private final AdminAuditLogMapper adminAuditLogMapper;

    public AdminAuditLogQueryService(AdminAuditLogRepository adminAuditLogRepository, AdminAuditLogMapper adminAuditLogMapper) {
        this.adminAuditLogRepository = adminAuditLogRepository;
        this.adminAuditLogMapper = adminAuditLogMapper;
    }

    /**
     * Return a {@link List} of {@link AdminAuditLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdminAuditLogDTO> findByCriteria(AdminAuditLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AdminAuditLog> specification = createSpecification(criteria);
        return adminAuditLogMapper.toDto(adminAuditLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdminAuditLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdminAuditLogDTO> findByCriteria(AdminAuditLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AdminAuditLog> specification = createSpecification(criteria);
        return adminAuditLogRepository.findAll(specification, page).map(adminAuditLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdminAuditLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AdminAuditLog> specification = createSpecification(criteria);
        return adminAuditLogRepository.count(specification);
    }

    /**
     * Function to convert {@link AdminAuditLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AdminAuditLog> createSpecification(AdminAuditLogCriteria criteria) {
        Specification<AdminAuditLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AdminAuditLog_.id));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAction(), AdminAuditLog_.action));
            }
            if (criteria.getTableName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTableName(), AdminAuditLog_.tableName));
            }
            if (criteria.getRecordId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecordId(), AdminAuditLog_.recordId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AdminAuditLog_.createdAt));
            }
            if (criteria.getAdminId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAdminId(),
                            root -> root.join(AdminAuditLog_.admin, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
