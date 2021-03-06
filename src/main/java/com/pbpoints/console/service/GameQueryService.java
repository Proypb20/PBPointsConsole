package com.pbpoints.console.service;

import com.pbpoints.console.domain.*; // for static metamodels
import com.pbpoints.console.domain.Game;
import com.pbpoints.console.repository.GameRepository;
import com.pbpoints.console.service.criteria.GameCriteria;
import com.pbpoints.console.service.dto.GameDTO;
import com.pbpoints.console.service.mapper.GameMapper;
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
 * Service for executing complex queries for {@link Game} entities in the database.
 * The main input is a {@link GameCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GameDTO} or a {@link Page} of {@link GameDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GameQueryService extends QueryService<Game> {

    private final Logger log = LoggerFactory.getLogger(GameQueryService.class);

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    public GameQueryService(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    /**
     * Return a {@link List} of {@link GameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GameDTO> findByCriteria(GameCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Game> specification = createSpecification(criteria);
        return gameMapper.toDto(gameRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GameDTO> findByCriteria(GameCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Game> specification = createSpecification(criteria);
        return gameRepository.findAll(specification, page).map(gameMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GameCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Game> specification = createSpecification(criteria);
        return gameRepository.count(specification);
    }

    /**
     * Function to convert {@link GameCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Game> createSpecification(GameCriteria criteria) {
        Specification<Game> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Game_.id));
            }
            if (criteria.getGameNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGameNum(), Game_.gameNum));
            }
            if (criteria.getClasif() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClasif(), Game_.clasif));
            }
            if (criteria.getSplitdeckNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSplitdeckNum(), Game_.splitdeckNum));
            }
            if (criteria.getPointsA() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointsA(), Game_.pointsA));
            }
            if (criteria.getPointsB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointsB(), Game_.pointsB));
            }
            if (criteria.getOverA() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOverA(), Game_.overA));
            }
            if (criteria.getOverB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOverB(), Game_.overB));
            }
            if (criteria.getPvpA() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPvpA(), Game_.pvpA));
            }
            if (criteria.getPvpB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPvpB(), Game_.pvpB));
            }
            if (criteria.getTimeLeft() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeLeft(), Game_.timeLeft));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Game_.status));
            }
            if (criteria.getTeamAId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamAId(), root -> root.join(Game_.teamA, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getTeamBId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamBId(), root -> root.join(Game_.teamB, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoryId(), root -> root.join(Game_.category, JoinType.LEFT).get(Category_.id))
                    );
            }
        }
        return specification;
    }
}
