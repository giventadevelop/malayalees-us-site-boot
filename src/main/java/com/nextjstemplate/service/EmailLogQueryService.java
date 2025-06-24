package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EmailLog;
import com.nextjstemplate.repository.EmailLogRepository;
import com.nextjstemplate.service.criteria.EmailLogCriteria;
import com.nextjstemplate.service.dto.EmailLogDTO;
import com.nextjstemplate.service.mapper.EmailLogMapper;
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
 * Service for executing complex queries for {@link EmailLog} entities in the database.
 * The main input is a {@link EmailLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmailLogDTO} or a {@link Page} of {@link EmailLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailLogQueryService extends QueryService<EmailLog> {

    private final Logger log = LoggerFactory.getLogger(EmailLogQueryService.class);

    private final EmailLogRepository emailLogRepository;

    private final EmailLogMapper emailLogMapper;

    public EmailLogQueryService(EmailLogRepository emailLogRepository, EmailLogMapper emailLogMapper) {
        this.emailLogRepository = emailLogRepository;
        this.emailLogMapper = emailLogMapper;
    }

    /**
     * Return a {@link List} of {@link EmailLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmailLogDTO> findByCriteria(EmailLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmailLog> specification = createSpecification(criteria);
        return emailLogMapper.toDto(emailLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmailLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailLogDTO> findByCriteria(EmailLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailLog> specification = createSpecification(criteria);
        return emailLogRepository.findAll(specification, page).map(emailLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmailLog> specification = createSpecification(criteria);
        return emailLogRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailLog> createSpecification(EmailLogCriteria criteria) {
        Specification<EmailLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmailLog_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EmailLog_.tenantId));
            }
            if (criteria.getRecipientEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecipientEmail(), EmailLog_.recipientEmail));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), EmailLog_.subject));
            }
            if (criteria.getSentAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentAt(), EmailLog_.sentAt));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), EmailLog_.status));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), EmailLog_.type));
            }
            if (criteria.getCampaignId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCampaignId(), EmailLog_.campaignId));
            }

           /* if (criteria.getCampaignId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCampaignId(),
                            root -> root.join(EmailLog_.campaign, JoinType.LEFT).get(CommunicationCampaign_.id)
                        )
                    );
            }*/
        }
        return specification;
    }
}
