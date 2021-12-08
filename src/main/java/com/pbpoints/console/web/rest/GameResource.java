package com.pbpoints.console.web.rest;

import com.pbpoints.console.repository.GameRepository;
import com.pbpoints.console.service.GameQueryService;
import com.pbpoints.console.service.GameService;
import com.pbpoints.console.service.criteria.GameCriteria;
import com.pbpoints.console.service.dto.GameDTO;
import com.pbpoints.console.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.console.domain.Game}.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameService gameService;

    private final GameRepository gameRepository;

    private final GameQueryService gameQueryService;

    public GameResource(GameService gameService, GameRepository gameRepository, GameQueryService gameQueryService) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.gameQueryService = gameQueryService;
    }

    /**
     * {@code POST  /games} : Create a new game.
     *
     * @param gameDTO the gameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameDTO, or with status {@code 400 (Bad Request)} if the game has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/games")
    public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
        log.debug("REST request to save Game : {}", gameDTO);
        if (gameDTO.getId() != null) {
            throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity
            .created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /games/:id} : Updates an existing game.
     *
     * @param id the id of the gameDTO to save.
     * @param gameDTO the gameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameDTO,
     * or with status {@code 400 (Bad Request)} if the gameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/games/{id}")
    public ResponseEntity<GameDTO> updateGame(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameDTO gameDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Game : {}, {}", id, gameDTO);
        if (gameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /games/:id} : Partial updates given fields of an existing game, field will ignore if it is null
     *
     * @param id the id of the gameDTO to save.
     * @param gameDTO the gameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameDTO,
     * or with status {@code 400 (Bad Request)} if the gameDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/games/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GameDTO> partialUpdateGame(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameDTO gameDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Game partially : {}, {}", id, gameDTO);
        if (gameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameDTO> result = gameService.partialUpdate(gameDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /games} : get all the games.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of games in body.
     */
    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getAllGames(GameCriteria criteria) {
        log.debug("REST request to get Games by criteria: {}", criteria);
        List<GameDTO> entityList = gameQueryService.findByCriteria(criteria);
        log.debug("EntityList: {}", entityList);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /games/count} : count all the games.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/games/count")
    public ResponseEntity<Long> countGames(GameCriteria criteria) {
        log.debug("REST request to count Games by criteria: {}", criteria);
        return ResponseEntity.ok().body(gameQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /games/:id} : get the "id" game.
     *
     * @param id the id of the gameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        log.debug("REST request to get Game : {}", id);
        Optional<GameDTO> gameDTO = gameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameDTO);
    }

    /**
     * {@code DELETE  /games/:id} : delete the "id" game.
     *
     * @param id the id of the gameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
