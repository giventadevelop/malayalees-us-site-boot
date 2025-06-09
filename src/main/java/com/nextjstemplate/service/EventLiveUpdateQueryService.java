package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventLiveUpdate;
import com.nextjstemplate.repository.EventLiveUpdateRepository;
import com.nextjstemplate.service.criteria.EventLiveUpdateCriteria;
import com.nextjstemplate.service.dto.EventLiveUpdateDTO;
import com.nextjstemplate.service.mapper.EventLiveUpdateMapper;
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
 * Service for executing complex queries for {@link EventLiveUpdate} entities in the database.
 * The main input is a {@link EventLiveUpdateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventLiveUpdateDTO} or a {@link Page} of {@link EventLiveUpdateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventLiveUpdateQueryService extends QueryService<EventLiveUpdate> {

    private final Logger log = LoggerFactory.getLogger(EventLiveUpdateQueryService.class);

    private final EventLiveUpdateRepository eventLiveUpdateRepository;

    private final EventLiveUpdateMapper eventLiveUpdateMapper;

    public EventLiveUpdateQueryService(EventLiveUpdateRepository eventLiveUpdateRepository, EventLiveUpdateMapper eventLiveUpdateMapper) {
        this.eventLiveUpdateRepository = eventLiveUpdateRepository;
        this.eventLiveUpdateMapper = eventLiveUpdateMapper;
    }

    /**
     * Return a {@link List} of {@link EventLiveUpdateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventLiveUpdateDTO> findByCriteria(EventLiveUpdateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventLiveUpdate> specification = createSpecification(criteria);
        return eventLiveUpdateMapper.toDto(eventLiveUpdateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventLiveUpdateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventLiveUpdateDTO> findByCriteria(EventLiveUpdateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventLiveUpdate> specification = createSpecification(criteria);
        return eventLiveUpdateRepository.findAll(specification, page).map(eventLiveUpdateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventLiveUpdateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventLiveUpdate> specification = createSpecification(criteria);
        return eventLiveUpdateRepository.count(specification);
    }

    /**
     * Function to convert {@link EventLiveUpdateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventLiveUpdate> createSpecification(EventLiveUpdateCriteria criteria) {
        Specification<EventLiveUpdate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventLiveUpdate_.id));
            }
            if (criteria.getUpdateType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdateType(), EventLiveUpdate_.updateType));
            }
            if (criteria.getContentText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentText(), EventLiveUpdate_.contentText));
            }
            if (criteria.getContentImageUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContentImageUrl(), EventLiveUpdate_.contentImageUrl));
            }
            if (criteria.getContentVideoUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContentVideoUrl(), EventLiveUpdate_.contentVideoUrl));
            }
            if (criteria.getContentLinkUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentLinkUrl(), EventLiveUpdate_.contentLinkUrl));
            }
            if (criteria.getDisplayOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDisplayOrder(), EventLiveUpdate_.displayOrder));
            }
            if (criteria.getIsDefault() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDefault(), EventLiveUpdate_.isDefault));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventLiveUpdate_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventLiveUpdate_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventLiveUpdate_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
