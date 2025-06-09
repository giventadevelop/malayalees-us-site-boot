package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventLiveUpdateAttachment;
import com.nextjstemplate.repository.EventLiveUpdateAttachmentRepository;
import com.nextjstemplate.service.criteria.EventLiveUpdateAttachmentCriteria;
import com.nextjstemplate.service.dto.EventLiveUpdateAttachmentDTO;
import com.nextjstemplate.service.mapper.EventLiveUpdateAttachmentMapper;
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
 * Service for executing complex queries for {@link EventLiveUpdateAttachment} entities in the database.
 * The main input is a {@link EventLiveUpdateAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventLiveUpdateAttachmentDTO} or a {@link Page} of {@link EventLiveUpdateAttachmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventLiveUpdateAttachmentQueryService extends QueryService<EventLiveUpdateAttachment> {

    private final Logger log = LoggerFactory.getLogger(EventLiveUpdateAttachmentQueryService.class);

    private final EventLiveUpdateAttachmentRepository eventLiveUpdateAttachmentRepository;

    private final EventLiveUpdateAttachmentMapper eventLiveUpdateAttachmentMapper;

    public EventLiveUpdateAttachmentQueryService(
        EventLiveUpdateAttachmentRepository eventLiveUpdateAttachmentRepository,
        EventLiveUpdateAttachmentMapper eventLiveUpdateAttachmentMapper
    ) {
        this.eventLiveUpdateAttachmentRepository = eventLiveUpdateAttachmentRepository;
        this.eventLiveUpdateAttachmentMapper = eventLiveUpdateAttachmentMapper;
    }

    /**
     * Return a {@link List} of {@link EventLiveUpdateAttachmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventLiveUpdateAttachmentDTO> findByCriteria(EventLiveUpdateAttachmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventLiveUpdateAttachment> specification = createSpecification(criteria);
        return eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventLiveUpdateAttachmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventLiveUpdateAttachmentDTO> findByCriteria(EventLiveUpdateAttachmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventLiveUpdateAttachment> specification = createSpecification(criteria);
        return eventLiveUpdateAttachmentRepository.findAll(specification, page).map(eventLiveUpdateAttachmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventLiveUpdateAttachmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventLiveUpdateAttachment> specification = createSpecification(criteria);
        return eventLiveUpdateAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EventLiveUpdateAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventLiveUpdateAttachment> createSpecification(EventLiveUpdateAttachmentCriteria criteria) {
        Specification<EventLiveUpdateAttachment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventLiveUpdateAttachment_.id));
            }
            if (criteria.getAttachmentType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAttachmentType(), EventLiveUpdateAttachment_.attachmentType));
            }
            if (criteria.getAttachmentUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAttachmentUrl(), EventLiveUpdateAttachment_.attachmentUrl));
            }
            if (criteria.getDisplayOrder() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDisplayOrder(), EventLiveUpdateAttachment_.displayOrder));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventLiveUpdateAttachment_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventLiveUpdateAttachment_.updatedAt));
            }
            if (criteria.getLiveUpdateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLiveUpdateId(),
                            root -> root.join(EventLiveUpdateAttachment_.liveUpdate, JoinType.LEFT).get(EventLiveUpdate_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
