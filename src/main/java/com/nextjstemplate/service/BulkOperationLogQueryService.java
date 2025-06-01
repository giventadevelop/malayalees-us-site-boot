package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.BulkOperationLog;
import com.nextjstemplate.repository.BulkOperationLogRepository;
import com.nextjstemplate.service.criteria.BulkOperationLogCriteria;
import com.nextjstemplate.service.dto.BulkOperationLogDTO;
import com.nextjstemplate.service.mapper.BulkOperationLogMapper;
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
 * Service for executing complex queries for {@link BulkOperationLog} entities in the database.
 * The main input is a {@link BulkOperationLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BulkOperationLogDTO} or a {@link Page} of {@link BulkOperationLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BulkOperationLogQueryService extends QueryService<BulkOperationLog> {

    private final Logger log = LoggerFactory.getLogger(BulkOperationLogQueryService.class);

    private final BulkOperationLogRepository bulkOperationLogRepository;

    private final BulkOperationLogMapper bulkOperationLogMapper;

    public BulkOperationLogQueryService(
        BulkOperationLogRepository bulkOperationLogRepository,
        BulkOperationLogMapper bulkOperationLogMapper
    ) {
        this.bulkOperationLogRepository = bulkOperationLogRepository;
        this.bulkOperationLogMapper = bulkOperationLogMapper;
    }

    /**
     * Return a {@link List} of {@link BulkOperationLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BulkOperationLogDTO> findByCriteria(BulkOperationLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BulkOperationLog> specification = createSpecification(criteria);
        return bulkOperationLogMapper.toDto(bulkOperationLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BulkOperationLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BulkOperationLogDTO> findByCriteria(BulkOperationLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BulkOperationLog> specification = createSpecification(criteria);
        return bulkOperationLogRepository.findAll(specification, page).map(bulkOperationLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BulkOperationLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BulkOperationLog> specification = createSpecification(criteria);
        return bulkOperationLogRepository.count(specification);
    }

    /**
     * Function to convert {@link BulkOperationLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BulkOperationLog> createSpecification(BulkOperationLogCriteria criteria) {
        Specification<BulkOperationLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BulkOperationLog_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), BulkOperationLog_.tenantId));
            }
            if (criteria.getOperationType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOperationType(), BulkOperationLog_.operationType));
            }
            if (criteria.getTargetCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetCount(), BulkOperationLog_.targetCount));
            }
            if (criteria.getSuccessCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSuccessCount(), BulkOperationLog_.successCount));
            }
            if (criteria.getErrorCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getErrorCount(), BulkOperationLog_.errorCount));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), BulkOperationLog_.createdAt));
            }
            if (criteria.getPerformedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPerformedById(),
                            root -> root.join(BulkOperationLog_.performedBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
