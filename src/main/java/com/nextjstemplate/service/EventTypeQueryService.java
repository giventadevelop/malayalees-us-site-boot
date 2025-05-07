package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventType;
import com.nextjstemplate.repository.EventTypeRepository;
import com.nextjstemplate.service.criteria.EventTypeCriteria;
import com.nextjstemplate.service.dto.EventTypeDTO;
import com.nextjstemplate.service.mapper.EventTypeMapper;
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
 * Service for executing complex queries for {@link EventType} entities in the database.
 * The main input is a {@link EventTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventTypeDTO} or a {@link Page} of {@link EventTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTypeQueryService extends QueryService<EventType> {

    private final Logger log = LoggerFactory.getLogger(EventTypeQueryService.class);

    private final EventTypeRepository eventTypeRepository;

    private final EventTypeMapper eventTypeMapper;

    public EventTypeQueryService(EventTypeRepository eventTypeRepository, EventTypeMapper eventTypeMapper) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventTypeMapper = eventTypeMapper;
    }

    /**
     * Return a {@link List} of {@link EventTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventTypeDTO> findByCriteria(EventTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventType> specification = createSpecification(criteria);
        return eventTypeMapper.toDto(eventTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTypeDTO> findByCriteria(EventTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventType> specification = createSpecification(criteria);
        return eventTypeRepository.findAll(specification, page).map(eventTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventType> specification = createSpecification(criteria);
        return eventTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventType> createSpecification(EventTypeCriteria criteria) {
        Specification<EventType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventType_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventType_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventType_.updatedAt));
            }
        }
        return specification;
    }
}
