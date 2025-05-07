package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.CalendarEvent;
import com.nextjstemplate.repository.CalendarEventRepository;
import com.nextjstemplate.service.criteria.CalendarEventCriteria;
import com.nextjstemplate.service.dto.CalendarEventDTO;
import com.nextjstemplate.service.mapper.CalendarEventMapper;
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
 * Service for executing complex queries for {@link CalendarEvent} entities in the database.
 * The main input is a {@link CalendarEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CalendarEventDTO} or a {@link Page} of {@link CalendarEventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CalendarEventQueryService extends QueryService<CalendarEvent> {

    private final Logger log = LoggerFactory.getLogger(CalendarEventQueryService.class);

    private final CalendarEventRepository calendarEventRepository;

    private final CalendarEventMapper calendarEventMapper;

    public CalendarEventQueryService(CalendarEventRepository calendarEventRepository, CalendarEventMapper calendarEventMapper) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventMapper = calendarEventMapper;
    }

    /**
     * Return a {@link List} of {@link CalendarEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CalendarEventDTO> findByCriteria(CalendarEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CalendarEvent> specification = createSpecification(criteria);
        return calendarEventMapper.toDto(calendarEventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CalendarEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CalendarEventDTO> findByCriteria(CalendarEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CalendarEvent> specification = createSpecification(criteria);
        return calendarEventRepository.findAll(specification, page).map(calendarEventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CalendarEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CalendarEvent> specification = createSpecification(criteria);
        return calendarEventRepository.count(specification);
    }

    /**
     * Function to convert {@link CalendarEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CalendarEvent> createSpecification(CalendarEventCriteria criteria) {
        Specification<CalendarEvent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CalendarEvent_.id));
            }
            if (criteria.getCalendarProvider() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCalendarProvider(), CalendarEvent_.calendarProvider));
            }
            if (criteria.getExternalEventId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalEventId(), CalendarEvent_.externalEventId));
            }
            if (criteria.getCalendarLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCalendarLink(), CalendarEvent_.calendarLink));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), CalendarEvent_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), CalendarEvent_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(CalendarEvent_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(CalendarEvent_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
