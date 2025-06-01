package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.criteria.EventTicketTransactionCriteria;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
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
 * Service for executing complex queries for {@link EventTicketTransaction} entities in the database.
 * The main input is a {@link EventTicketTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventTicketTransactionDTO} or a {@link Page} of {@link EventTicketTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTicketTransactionQueryService extends QueryService<EventTicketTransaction> {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionQueryService.class);

    private final EventTicketTransactionRepository eventTicketTransactionRepository;

    private final EventTicketTransactionMapper eventTicketTransactionMapper;

    public EventTicketTransactionQueryService(
        EventTicketTransactionRepository eventTicketTransactionRepository,
        EventTicketTransactionMapper eventTicketTransactionMapper
    ) {
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionMapper = eventTicketTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link EventTicketTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventTicketTransactionDTO> findByCriteria(EventTicketTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventTicketTransaction> specification = createSpecification(criteria);
        return eventTicketTransactionMapper.toDto(eventTicketTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventTicketTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionDTO> findByCriteria(EventTicketTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventTicketTransaction> specification = createSpecification(criteria);
        return eventTicketTransactionRepository.findAll(specification, page).map(eventTicketTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTicketTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventTicketTransaction> specification = createSpecification(criteria);
        return eventTicketTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTicketTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventTicketTransaction> createSpecification(EventTicketTransactionCriteria criteria) {
        Specification<EventTicketTransaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventTicketTransaction_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventTicketTransaction_.tenantId));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EventTicketTransaction_.email));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EventTicketTransaction_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EventTicketTransaction_.lastName));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), EventTicketTransaction_.quantity));
            }
            if (criteria.getPricePerUnit() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPricePerUnit(), EventTicketTransaction_.pricePerUnit));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), EventTicketTransaction_.totalAmount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), EventTicketTransaction_.status));
            }
            if (criteria.getPurchaseDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPurchaseDate(), EventTicketTransaction_.purchaseDate));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventTicketTransaction_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventTicketTransaction_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventTicketTransaction_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
            if (criteria.getTicketTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTicketTypeId(),
                            root -> root.join(EventTicketTransaction_.ticketType, JoinType.LEFT).get(EventTicketType_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(EventTicketTransaction_.user, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
