package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.repository.EventRepository;
import com.nextjstemplate.service.criteria.EventCriteria;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.mapper.EventMapper;
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
 * Service for executing complex queries for {@link Event} entities in the database.
 * The main input is a {@link EventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventDTO} or a {@link Page} of {@link EventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventQueryService extends QueryService<Event> {

    private final Logger log = LoggerFactory.getLogger(EventQueryService.class);

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    public EventQueryService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    /**
     * Return a {@link List} of {@link EventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventDTO> findByCriteria(EventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Event> specification = createSpecification(criteria);
        return eventMapper.toDto(eventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> findByCriteria(EventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Event> specification = createSpecification(criteria);
        return eventRepository.findAll(specification, page).map(eventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Event> specification = createSpecification(criteria);
        return eventRepository.count(specification);
    }

    /**
     * Function to convert {@link EventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Event> createSpecification(EventCriteria criteria) {
        Specification<Event> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Event_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Event_.title));
            }
            if (criteria.getCaption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCaption(), Event_.caption));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Event_.description));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Event_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Event_.endDate));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Event_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Event_.endTime));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Event_.location));
            }
            if (criteria.getCapacity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacity(), Event_.capacity));
            }
            if (criteria.getAdmissionType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdmissionType(), Event_.admissionType));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Event_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Event_.updatedAt));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(Event_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
            if (criteria.getEventTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventTypeId(), root -> root.join(Event_.eventType, JoinType.LEFT).get(EventType_.id))
                    );
            }
        }
        return specification;
    }
}
