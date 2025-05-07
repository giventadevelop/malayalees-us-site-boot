package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.TicketType;
import com.nextjstemplate.repository.TicketTypeRepository;
import com.nextjstemplate.service.criteria.TicketTypeCriteria;
import com.nextjstemplate.service.dto.TicketTypeDTO;
import com.nextjstemplate.service.mapper.TicketTypeMapper;
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
 * Service for executing complex queries for {@link TicketType} entities in the database.
 * The main input is a {@link TicketTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketTypeDTO} or a {@link Page} of {@link TicketTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketTypeQueryService extends QueryService<TicketType> {

    private final Logger log = LoggerFactory.getLogger(TicketTypeQueryService.class);

    private final TicketTypeRepository ticketTypeRepository;

    private final TicketTypeMapper ticketTypeMapper;

    public TicketTypeQueryService(TicketTypeRepository ticketTypeRepository, TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    /**
     * Return a {@link List} of {@link TicketTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketTypeDTO> findByCriteria(TicketTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TicketType> specification = createSpecification(criteria);
        return ticketTypeMapper.toDto(ticketTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketTypeDTO> findByCriteria(TicketTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TicketType> specification = createSpecification(criteria);
        return ticketTypeRepository.findAll(specification, page).map(ticketTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TicketType> specification = createSpecification(criteria);
        return ticketTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TicketType> createSpecification(TicketTypeCriteria criteria) {
        Specification<TicketType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TicketType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TicketType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TicketType_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), TicketType_.price));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), TicketType_.code));
            }
            if (criteria.getAvailableQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvailableQuantity(), TicketType_.availableQuantity));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), TicketType_.isActive));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TicketType_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), TicketType_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(TicketType_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
        }
        return specification;
    }
}
