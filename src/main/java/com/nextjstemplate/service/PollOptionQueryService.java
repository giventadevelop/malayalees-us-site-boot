package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.PollOption;
import com.nextjstemplate.repository.PollOptionRepository;
import com.nextjstemplate.service.criteria.PollOptionCriteria;
import com.nextjstemplate.service.dto.PollOptionDTO;
import com.nextjstemplate.service.mapper.PollOptionMapper;
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
 * Service for executing complex queries for {@link PollOption} entities in the database.
 * The main input is a {@link PollOptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PollOptionDTO} or a {@link Page} of {@link PollOptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PollOptionQueryService extends QueryService<PollOption> {

    private final Logger log = LoggerFactory.getLogger(PollOptionQueryService.class);

    private final PollOptionRepository pollOptionRepository;

    private final PollOptionMapper pollOptionMapper;

    public PollOptionQueryService(PollOptionRepository pollOptionRepository, PollOptionMapper pollOptionMapper) {
        this.pollOptionRepository = pollOptionRepository;
        this.pollOptionMapper = pollOptionMapper;
    }

    /**
     * Return a {@link List} of {@link PollOptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PollOptionDTO> findByCriteria(PollOptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PollOption> specification = createSpecification(criteria);
        return pollOptionMapper.toDto(pollOptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PollOptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PollOptionDTO> findByCriteria(PollOptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PollOption> specification = createSpecification(criteria);
        return pollOptionRepository.findAll(specification, page).map(pollOptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PollOptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PollOption> specification = createSpecification(criteria);
        return pollOptionRepository.count(specification);
    }

    /**
     * Function to convert {@link PollOptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PollOption> createSpecification(PollOptionCriteria criteria) {
        Specification<PollOption> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PollOption_.id));
            }
            if (criteria.getOptionText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOptionText(), PollOption_.optionText));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), PollOption_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), PollOption_.updatedAt));
            }
            if (criteria.getPollId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPollId(), root -> root.join(PollOption_.poll, JoinType.LEFT).get(Poll_.id))
                    );
            }
        }
        return specification;
    }
}
