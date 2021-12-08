package com.pbpoints.console.service;

import com.pbpoints.console.domain.*; // for static metamodels
import com.pbpoints.console.domain.Category;
import com.pbpoints.console.repository.CategoryRepository;
import com.pbpoints.console.service.criteria.CategoryCriteria;
import com.pbpoints.console.service.dto.CategoryDTO;
import com.pbpoints.console.service.mapper.CategoryMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Category} entities in the database.
 * The main input is a {@link CategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategoryDTO} or a {@link Page} of {@link CategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryQueryService extends QueryService<Category> {

    private final Logger log = LoggerFactory.getLogger(CategoryQueryService.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryQueryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Return a {@link List} of {@link CategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> findByCriteria(CategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryMapper.toDto(categoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findByCriteria(CategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryRepository.findAll(specification, page).map(categoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Category> createSpecification(CategoryCriteria criteria) {
        Specification<Category> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Category_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Category_.name));
            }
            if (criteria.getGameTimeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGameTimeType(), Category_.gameTimeType));
            }
            if (criteria.getGameTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGameTime(), Category_.gameTime));
            }
            if (criteria.getStopTimeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStopTimeType(), Category_.stopTimeType));
            }
            if (criteria.getStopTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStopTime(), Category_.stopTime));
            }
            if (criteria.getStopSdTimeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStopSdTimeType(), Category_.stopSdTimeType));
            }
            if (criteria.getStopSdTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStopSdTime(), Category_.stopSdTime));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), Category_.points));
            }
            if (criteria.getDifPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDifPoints(), Category_.difPoints));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Category_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
        }
        return specification;
    }
}
