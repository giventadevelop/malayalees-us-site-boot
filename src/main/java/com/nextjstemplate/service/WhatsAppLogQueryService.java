package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.WhatsAppLog;
import com.nextjstemplate.repository.WhatsAppLogRepository;
import com.nextjstemplate.service.criteria.WhatsAppLogCriteria;
import com.nextjstemplate.service.dto.WhatsAppLogDTO;
import com.nextjstemplate.service.mapper.WhatsAppLogMapper;
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
 * Service for executing complex queries for {@link WhatsAppLog} entities in the database.
 * The main input is a {@link WhatsAppLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WhatsAppLogDTO} or a {@link Page} of {@link WhatsAppLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WhatsAppLogQueryService extends QueryService<WhatsAppLog> {

    private final Logger log = LoggerFactory.getLogger(WhatsAppLogQueryService.class);

    private final WhatsAppLogRepository whatsAppLogRepository;

    private final WhatsAppLogMapper whatsAppLogMapper;

    public WhatsAppLogQueryService(WhatsAppLogRepository whatsAppLogRepository, WhatsAppLogMapper whatsAppLogMapper) {
        this.whatsAppLogRepository = whatsAppLogRepository;
        this.whatsAppLogMapper = whatsAppLogMapper;
    }

    /**
     * Return a {@link List} of {@link WhatsAppLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WhatsAppLogDTO> findByCriteria(WhatsAppLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WhatsAppLog> specification = createSpecification(criteria);
        return whatsAppLogMapper.toDto(whatsAppLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WhatsAppLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WhatsAppLogDTO> findByCriteria(WhatsAppLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WhatsAppLog> specification = createSpecification(criteria);
        return whatsAppLogRepository.findAll(specification, page).map(whatsAppLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WhatsAppLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WhatsAppLog> specification = createSpecification(criteria);
        return whatsAppLogRepository.count(specification);
    }

    /**
     * Function to convert {@link WhatsAppLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WhatsAppLog> createSpecification(WhatsAppLogCriteria criteria) {
        Specification<WhatsAppLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WhatsAppLog_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), WhatsAppLog_.tenantId));
            }
            if (criteria.getRecipientPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecipientPhone(), WhatsAppLog_.recipientPhone));
            }
            if (criteria.getSentAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentAt(), WhatsAppLog_.sentAt));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), WhatsAppLog_.status));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), WhatsAppLog_.type));
            }
            if (criteria.getCampaignId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCampaignId(), WhatsAppLog_.campaignId));
            }
            /*if (criteria.getCampaignId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCampaignId(),
                            root -> root.join(WhatsAppLog_.campaign, JoinType.LEFT).get(CommunicationCampaign_.id)
                        )
                    );
            }*/
        }
        return specification;
    }
}
