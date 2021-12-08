package com.pbpoints.console.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.console.IntegrationTest;
import com.pbpoints.console.domain.Category;
import com.pbpoints.console.domain.Game;
import com.pbpoints.console.domain.Team;
import com.pbpoints.console.domain.enumeration.Status;
import com.pbpoints.console.repository.GameRepository;
import com.pbpoints.console.service.criteria.GameCriteria;
import com.pbpoints.console.service.dto.GameDTO;
import com.pbpoints.console.service.mapper.GameMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameResourceIT {

    private static final Integer DEFAULT_GAME_NUM = 1;
    private static final Integer UPDATED_GAME_NUM = 2;
    private static final Integer SMALLER_GAME_NUM = 1 - 1;

    private static final String DEFAULT_CLASIF = "AAAAAAAAAA";
    private static final String UPDATED_CLASIF = "BBBBBBBBBB";

    private static final Integer DEFAULT_SPLITDECK_NUM = 1;
    private static final Integer UPDATED_SPLITDECK_NUM = 2;
    private static final Integer SMALLER_SPLITDECK_NUM = 1 - 1;

    private static final Integer DEFAULT_POINTS_A = 1;
    private static final Integer UPDATED_POINTS_A = 2;
    private static final Integer SMALLER_POINTS_A = 1 - 1;

    private static final Integer DEFAULT_POINTS_B = 1;
    private static final Integer UPDATED_POINTS_B = 2;
    private static final Integer SMALLER_POINTS_B = 1 - 1;

    private static final Integer DEFAULT_OVER_A = 1;
    private static final Integer UPDATED_OVER_A = 2;
    private static final Integer SMALLER_OVER_A = 1 - 1;

    private static final Integer DEFAULT_OVER_B = 1;
    private static final Integer UPDATED_OVER_B = 2;
    private static final Integer SMALLER_OVER_B = 1 - 1;

    private static final Integer DEFAULT_PVP_A = 1;
    private static final Integer UPDATED_PVP_A = 2;
    private static final Integer SMALLER_PVP_A = 1 - 1;

    private static final Integer DEFAULT_PVP_B = 1;
    private static final Integer UPDATED_PVP_B = 2;
    private static final Integer SMALLER_PVP_B = 1 - 1;

    private static final Integer DEFAULT_TIME_LEFT = 1;
    private static final Integer UPDATED_TIME_LEFT = 2;
    private static final Integer SMALLER_TIME_LEFT = 1 - 1;

    private static final Status DEFAULT_STATUS = Status.CREATED;
    private static final Status UPDATED_STATUS = Status.PENDING;

    private static final String ENTITY_API_URL = "/api/games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameMockMvc;

    private Game game;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = new Game()
            .gameNum(DEFAULT_GAME_NUM)
            .clasif(DEFAULT_CLASIF)
            .splitdeckNum(DEFAULT_SPLITDECK_NUM)
            .pointsA(DEFAULT_POINTS_A)
            .pointsB(DEFAULT_POINTS_B)
            .overA(DEFAULT_OVER_A)
            .overB(DEFAULT_OVER_B)
            .pvpA(DEFAULT_PVP_A)
            .pvpB(DEFAULT_PVP_B)
            .timeLeft(DEFAULT_TIME_LEFT)
            .status(DEFAULT_STATUS);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        game.setTeamA(team);
        // Add required entity
        game.setTeamB(team);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        game.setCategory(category);
        return game;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createUpdatedEntity(EntityManager em) {
        Game game = new Game()
            .gameNum(UPDATED_GAME_NUM)
            .clasif(UPDATED_CLASIF)
            .splitdeckNum(UPDATED_SPLITDECK_NUM)
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .overA(UPDATED_OVER_A)
            .overB(UPDATED_OVER_B)
            .pvpA(UPDATED_PVP_A)
            .pvpB(UPDATED_PVP_B)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        game.setTeamA(team);
        // Add required entity
        game.setTeamB(team);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        game.setCategory(category);
        return game;
    }

    @BeforeEach
    public void initTest() {
        game = createEntity(em);
    }

    @Test
    @Transactional
    void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();
        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);
        restGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getGameNum()).isEqualTo(DEFAULT_GAME_NUM);
        assertThat(testGame.getClasif()).isEqualTo(DEFAULT_CLASIF);
        assertThat(testGame.getSplitdeckNum()).isEqualTo(DEFAULT_SPLITDECK_NUM);
        assertThat(testGame.getPointsA()).isEqualTo(DEFAULT_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(DEFAULT_POINTS_B);
        assertThat(testGame.getOverA()).isEqualTo(DEFAULT_OVER_A);
        assertThat(testGame.getOverB()).isEqualTo(DEFAULT_OVER_B);
        assertThat(testGame.getPvpA()).isEqualTo(DEFAULT_PVP_A);
        assertThat(testGame.getPvpB()).isEqualTo(DEFAULT_PVP_B);
        assertThat(testGame.getTimeLeft()).isEqualTo(DEFAULT_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createGameWithExistingId() throws Exception {
        // Create the Game with an existing ID
        game.setId(1L);
        GameDTO gameDTO = gameMapper.toDto(game);

        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].gameNum").value(hasItem(DEFAULT_GAME_NUM)))
            .andExpect(jsonPath("$.[*].clasif").value(hasItem(DEFAULT_CLASIF)))
            .andExpect(jsonPath("$.[*].splitdeckNum").value(hasItem(DEFAULT_SPLITDECK_NUM)))
            .andExpect(jsonPath("$.[*].pointsA").value(hasItem(DEFAULT_POINTS_A)))
            .andExpect(jsonPath("$.[*].pointsB").value(hasItem(DEFAULT_POINTS_B)))
            .andExpect(jsonPath("$.[*].overA").value(hasItem(DEFAULT_OVER_A)))
            .andExpect(jsonPath("$.[*].overB").value(hasItem(DEFAULT_OVER_B)))
            .andExpect(jsonPath("$.[*].pvpA").value(hasItem(DEFAULT_PVP_A)))
            .andExpect(jsonPath("$.[*].pvpB").value(hasItem(DEFAULT_PVP_B)))
            .andExpect(jsonPath("$.[*].timeLeft").value(hasItem(DEFAULT_TIME_LEFT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc
            .perform(get(ENTITY_API_URL_ID, game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.gameNum").value(DEFAULT_GAME_NUM))
            .andExpect(jsonPath("$.clasif").value(DEFAULT_CLASIF))
            .andExpect(jsonPath("$.splitdeckNum").value(DEFAULT_SPLITDECK_NUM))
            .andExpect(jsonPath("$.pointsA").value(DEFAULT_POINTS_A))
            .andExpect(jsonPath("$.pointsB").value(DEFAULT_POINTS_B))
            .andExpect(jsonPath("$.overA").value(DEFAULT_OVER_A))
            .andExpect(jsonPath("$.overB").value(DEFAULT_OVER_B))
            .andExpect(jsonPath("$.pvpA").value(DEFAULT_PVP_A))
            .andExpect(jsonPath("$.pvpB").value(DEFAULT_PVP_B))
            .andExpect(jsonPath("$.timeLeft").value(DEFAULT_TIME_LEFT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getGamesByIdFiltering() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        Long id = game.getId();

        defaultGameShouldBeFound("id.equals=" + id);
        defaultGameShouldNotBeFound("id.notEquals=" + id);

        defaultGameShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGameShouldNotBeFound("id.greaterThan=" + id);

        defaultGameShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGameShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum equals to DEFAULT_GAME_NUM
        defaultGameShouldBeFound("gameNum.equals=" + DEFAULT_GAME_NUM);

        // Get all the gameList where gameNum equals to UPDATED_GAME_NUM
        defaultGameShouldNotBeFound("gameNum.equals=" + UPDATED_GAME_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum not equals to DEFAULT_GAME_NUM
        defaultGameShouldNotBeFound("gameNum.notEquals=" + DEFAULT_GAME_NUM);

        // Get all the gameList where gameNum not equals to UPDATED_GAME_NUM
        defaultGameShouldBeFound("gameNum.notEquals=" + UPDATED_GAME_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum in DEFAULT_GAME_NUM or UPDATED_GAME_NUM
        defaultGameShouldBeFound("gameNum.in=" + DEFAULT_GAME_NUM + "," + UPDATED_GAME_NUM);

        // Get all the gameList where gameNum equals to UPDATED_GAME_NUM
        defaultGameShouldNotBeFound("gameNum.in=" + UPDATED_GAME_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum is not null
        defaultGameShouldBeFound("gameNum.specified=true");

        // Get all the gameList where gameNum is null
        defaultGameShouldNotBeFound("gameNum.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum is greater than or equal to DEFAULT_GAME_NUM
        defaultGameShouldBeFound("gameNum.greaterThanOrEqual=" + DEFAULT_GAME_NUM);

        // Get all the gameList where gameNum is greater than or equal to UPDATED_GAME_NUM
        defaultGameShouldNotBeFound("gameNum.greaterThanOrEqual=" + UPDATED_GAME_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum is less than or equal to DEFAULT_GAME_NUM
        defaultGameShouldBeFound("gameNum.lessThanOrEqual=" + DEFAULT_GAME_NUM);

        // Get all the gameList where gameNum is less than or equal to SMALLER_GAME_NUM
        defaultGameShouldNotBeFound("gameNum.lessThanOrEqual=" + SMALLER_GAME_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum is less than DEFAULT_GAME_NUM
        defaultGameShouldNotBeFound("gameNum.lessThan=" + DEFAULT_GAME_NUM);

        // Get all the gameList where gameNum is less than UPDATED_GAME_NUM
        defaultGameShouldBeFound("gameNum.lessThan=" + UPDATED_GAME_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByGameNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where gameNum is greater than DEFAULT_GAME_NUM
        defaultGameShouldNotBeFound("gameNum.greaterThan=" + DEFAULT_GAME_NUM);

        // Get all the gameList where gameNum is greater than SMALLER_GAME_NUM
        defaultGameShouldBeFound("gameNum.greaterThan=" + SMALLER_GAME_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif equals to DEFAULT_CLASIF
        defaultGameShouldBeFound("clasif.equals=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif equals to UPDATED_CLASIF
        defaultGameShouldNotBeFound("clasif.equals=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif not equals to DEFAULT_CLASIF
        defaultGameShouldNotBeFound("clasif.notEquals=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif not equals to UPDATED_CLASIF
        defaultGameShouldBeFound("clasif.notEquals=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif in DEFAULT_CLASIF or UPDATED_CLASIF
        defaultGameShouldBeFound("clasif.in=" + DEFAULT_CLASIF + "," + UPDATED_CLASIF);

        // Get all the gameList where clasif equals to UPDATED_CLASIF
        defaultGameShouldNotBeFound("clasif.in=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif is not null
        defaultGameShouldBeFound("clasif.specified=true");

        // Get all the gameList where clasif is null
        defaultGameShouldNotBeFound("clasif.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByClasifContainsSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif contains DEFAULT_CLASIF
        defaultGameShouldBeFound("clasif.contains=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif contains UPDATED_CLASIF
        defaultGameShouldNotBeFound("clasif.contains=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifNotContainsSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif does not contain DEFAULT_CLASIF
        defaultGameShouldNotBeFound("clasif.doesNotContain=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif does not contain UPDATED_CLASIF
        defaultGameShouldBeFound("clasif.doesNotContain=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum equals to DEFAULT_SPLITDECK_NUM
        defaultGameShouldBeFound("splitdeckNum.equals=" + DEFAULT_SPLITDECK_NUM);

        // Get all the gameList where splitdeckNum equals to UPDATED_SPLITDECK_NUM
        defaultGameShouldNotBeFound("splitdeckNum.equals=" + UPDATED_SPLITDECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum not equals to DEFAULT_SPLITDECK_NUM
        defaultGameShouldNotBeFound("splitdeckNum.notEquals=" + DEFAULT_SPLITDECK_NUM);

        // Get all the gameList where splitdeckNum not equals to UPDATED_SPLITDECK_NUM
        defaultGameShouldBeFound("splitdeckNum.notEquals=" + UPDATED_SPLITDECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum in DEFAULT_SPLITDECK_NUM or UPDATED_SPLITDECK_NUM
        defaultGameShouldBeFound("splitdeckNum.in=" + DEFAULT_SPLITDECK_NUM + "," + UPDATED_SPLITDECK_NUM);

        // Get all the gameList where splitdeckNum equals to UPDATED_SPLITDECK_NUM
        defaultGameShouldNotBeFound("splitdeckNum.in=" + UPDATED_SPLITDECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum is not null
        defaultGameShouldBeFound("splitdeckNum.specified=true");

        // Get all the gameList where splitdeckNum is null
        defaultGameShouldNotBeFound("splitdeckNum.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum is greater than or equal to DEFAULT_SPLITDECK_NUM
        defaultGameShouldBeFound("splitdeckNum.greaterThanOrEqual=" + DEFAULT_SPLITDECK_NUM);

        // Get all the gameList where splitdeckNum is greater than or equal to UPDATED_SPLITDECK_NUM
        defaultGameShouldNotBeFound("splitdeckNum.greaterThanOrEqual=" + UPDATED_SPLITDECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum is less than or equal to DEFAULT_SPLITDECK_NUM
        defaultGameShouldBeFound("splitdeckNum.lessThanOrEqual=" + DEFAULT_SPLITDECK_NUM);

        // Get all the gameList where splitdeckNum is less than or equal to SMALLER_SPLITDECK_NUM
        defaultGameShouldNotBeFound("splitdeckNum.lessThanOrEqual=" + SMALLER_SPLITDECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum is less than DEFAULT_SPLITDECK_NUM
        defaultGameShouldNotBeFound("splitdeckNum.lessThan=" + DEFAULT_SPLITDECK_NUM);

        // Get all the gameList where splitdeckNum is less than UPDATED_SPLITDECK_NUM
        defaultGameShouldBeFound("splitdeckNum.lessThan=" + UPDATED_SPLITDECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitdeckNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitdeckNum is greater than DEFAULT_SPLITDECK_NUM
        defaultGameShouldNotBeFound("splitdeckNum.greaterThan=" + DEFAULT_SPLITDECK_NUM);

        // Get all the gameList where splitdeckNum is greater than SMALLER_SPLITDECK_NUM
        defaultGameShouldBeFound("splitdeckNum.greaterThan=" + SMALLER_SPLITDECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA equals to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.equals=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA equals to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.equals=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA not equals to DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.notEquals=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA not equals to UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.notEquals=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA in DEFAULT_POINTS_A or UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.in=" + DEFAULT_POINTS_A + "," + UPDATED_POINTS_A);

        // Get all the gameList where pointsA equals to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.in=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is not null
        defaultGameShouldBeFound("pointsA.specified=true");

        // Get all the gameList where pointsA is null
        defaultGameShouldNotBeFound("pointsA.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is greater than or equal to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.greaterThanOrEqual=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is greater than or equal to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.greaterThanOrEqual=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is less than or equal to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.lessThanOrEqual=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is less than or equal to SMALLER_POINTS_A
        defaultGameShouldNotBeFound("pointsA.lessThanOrEqual=" + SMALLER_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is less than DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.lessThan=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is less than UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.lessThan=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is greater than DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.greaterThan=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is greater than SMALLER_POINTS_A
        defaultGameShouldBeFound("pointsA.greaterThan=" + SMALLER_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB equals to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.equals=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB equals to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.equals=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB not equals to DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.notEquals=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB not equals to UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.notEquals=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB in DEFAULT_POINTS_B or UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.in=" + DEFAULT_POINTS_B + "," + UPDATED_POINTS_B);

        // Get all the gameList where pointsB equals to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.in=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is not null
        defaultGameShouldBeFound("pointsB.specified=true");

        // Get all the gameList where pointsB is null
        defaultGameShouldNotBeFound("pointsB.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is greater than or equal to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.greaterThanOrEqual=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is greater than or equal to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.greaterThanOrEqual=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is less than or equal to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.lessThanOrEqual=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is less than or equal to SMALLER_POINTS_B
        defaultGameShouldNotBeFound("pointsB.lessThanOrEqual=" + SMALLER_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is less than DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.lessThan=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is less than UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.lessThan=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is greater than DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.greaterThan=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is greater than SMALLER_POINTS_B
        defaultGameShouldBeFound("pointsB.greaterThan=" + SMALLER_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA equals to DEFAULT_OVER_A
        defaultGameShouldBeFound("overA.equals=" + DEFAULT_OVER_A);

        // Get all the gameList where overA equals to UPDATED_OVER_A
        defaultGameShouldNotBeFound("overA.equals=" + UPDATED_OVER_A);
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA not equals to DEFAULT_OVER_A
        defaultGameShouldNotBeFound("overA.notEquals=" + DEFAULT_OVER_A);

        // Get all the gameList where overA not equals to UPDATED_OVER_A
        defaultGameShouldBeFound("overA.notEquals=" + UPDATED_OVER_A);
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA in DEFAULT_OVER_A or UPDATED_OVER_A
        defaultGameShouldBeFound("overA.in=" + DEFAULT_OVER_A + "," + UPDATED_OVER_A);

        // Get all the gameList where overA equals to UPDATED_OVER_A
        defaultGameShouldNotBeFound("overA.in=" + UPDATED_OVER_A);
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA is not null
        defaultGameShouldBeFound("overA.specified=true");

        // Get all the gameList where overA is null
        defaultGameShouldNotBeFound("overA.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA is greater than or equal to DEFAULT_OVER_A
        defaultGameShouldBeFound("overA.greaterThanOrEqual=" + DEFAULT_OVER_A);

        // Get all the gameList where overA is greater than or equal to UPDATED_OVER_A
        defaultGameShouldNotBeFound("overA.greaterThanOrEqual=" + UPDATED_OVER_A);
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA is less than or equal to DEFAULT_OVER_A
        defaultGameShouldBeFound("overA.lessThanOrEqual=" + DEFAULT_OVER_A);

        // Get all the gameList where overA is less than or equal to SMALLER_OVER_A
        defaultGameShouldNotBeFound("overA.lessThanOrEqual=" + SMALLER_OVER_A);
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA is less than DEFAULT_OVER_A
        defaultGameShouldNotBeFound("overA.lessThan=" + DEFAULT_OVER_A);

        // Get all the gameList where overA is less than UPDATED_OVER_A
        defaultGameShouldBeFound("overA.lessThan=" + UPDATED_OVER_A);
    }

    @Test
    @Transactional
    void getAllGamesByOverAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overA is greater than DEFAULT_OVER_A
        defaultGameShouldNotBeFound("overA.greaterThan=" + DEFAULT_OVER_A);

        // Get all the gameList where overA is greater than SMALLER_OVER_A
        defaultGameShouldBeFound("overA.greaterThan=" + SMALLER_OVER_A);
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB equals to DEFAULT_OVER_B
        defaultGameShouldBeFound("overB.equals=" + DEFAULT_OVER_B);

        // Get all the gameList where overB equals to UPDATED_OVER_B
        defaultGameShouldNotBeFound("overB.equals=" + UPDATED_OVER_B);
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB not equals to DEFAULT_OVER_B
        defaultGameShouldNotBeFound("overB.notEquals=" + DEFAULT_OVER_B);

        // Get all the gameList where overB not equals to UPDATED_OVER_B
        defaultGameShouldBeFound("overB.notEquals=" + UPDATED_OVER_B);
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB in DEFAULT_OVER_B or UPDATED_OVER_B
        defaultGameShouldBeFound("overB.in=" + DEFAULT_OVER_B + "," + UPDATED_OVER_B);

        // Get all the gameList where overB equals to UPDATED_OVER_B
        defaultGameShouldNotBeFound("overB.in=" + UPDATED_OVER_B);
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB is not null
        defaultGameShouldBeFound("overB.specified=true");

        // Get all the gameList where overB is null
        defaultGameShouldNotBeFound("overB.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB is greater than or equal to DEFAULT_OVER_B
        defaultGameShouldBeFound("overB.greaterThanOrEqual=" + DEFAULT_OVER_B);

        // Get all the gameList where overB is greater than or equal to UPDATED_OVER_B
        defaultGameShouldNotBeFound("overB.greaterThanOrEqual=" + UPDATED_OVER_B);
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB is less than or equal to DEFAULT_OVER_B
        defaultGameShouldBeFound("overB.lessThanOrEqual=" + DEFAULT_OVER_B);

        // Get all the gameList where overB is less than or equal to SMALLER_OVER_B
        defaultGameShouldNotBeFound("overB.lessThanOrEqual=" + SMALLER_OVER_B);
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB is less than DEFAULT_OVER_B
        defaultGameShouldNotBeFound("overB.lessThan=" + DEFAULT_OVER_B);

        // Get all the gameList where overB is less than UPDATED_OVER_B
        defaultGameShouldBeFound("overB.lessThan=" + UPDATED_OVER_B);
    }

    @Test
    @Transactional
    void getAllGamesByOverBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overB is greater than DEFAULT_OVER_B
        defaultGameShouldNotBeFound("overB.greaterThan=" + DEFAULT_OVER_B);

        // Get all the gameList where overB is greater than SMALLER_OVER_B
        defaultGameShouldBeFound("overB.greaterThan=" + SMALLER_OVER_B);
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA equals to DEFAULT_PVP_A
        defaultGameShouldBeFound("pvpA.equals=" + DEFAULT_PVP_A);

        // Get all the gameList where pvpA equals to UPDATED_PVP_A
        defaultGameShouldNotBeFound("pvpA.equals=" + UPDATED_PVP_A);
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA not equals to DEFAULT_PVP_A
        defaultGameShouldNotBeFound("pvpA.notEquals=" + DEFAULT_PVP_A);

        // Get all the gameList where pvpA not equals to UPDATED_PVP_A
        defaultGameShouldBeFound("pvpA.notEquals=" + UPDATED_PVP_A);
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA in DEFAULT_PVP_A or UPDATED_PVP_A
        defaultGameShouldBeFound("pvpA.in=" + DEFAULT_PVP_A + "," + UPDATED_PVP_A);

        // Get all the gameList where pvpA equals to UPDATED_PVP_A
        defaultGameShouldNotBeFound("pvpA.in=" + UPDATED_PVP_A);
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA is not null
        defaultGameShouldBeFound("pvpA.specified=true");

        // Get all the gameList where pvpA is null
        defaultGameShouldNotBeFound("pvpA.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA is greater than or equal to DEFAULT_PVP_A
        defaultGameShouldBeFound("pvpA.greaterThanOrEqual=" + DEFAULT_PVP_A);

        // Get all the gameList where pvpA is greater than or equal to UPDATED_PVP_A
        defaultGameShouldNotBeFound("pvpA.greaterThanOrEqual=" + UPDATED_PVP_A);
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA is less than or equal to DEFAULT_PVP_A
        defaultGameShouldBeFound("pvpA.lessThanOrEqual=" + DEFAULT_PVP_A);

        // Get all the gameList where pvpA is less than or equal to SMALLER_PVP_A
        defaultGameShouldNotBeFound("pvpA.lessThanOrEqual=" + SMALLER_PVP_A);
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA is less than DEFAULT_PVP_A
        defaultGameShouldNotBeFound("pvpA.lessThan=" + DEFAULT_PVP_A);

        // Get all the gameList where pvpA is less than UPDATED_PVP_A
        defaultGameShouldBeFound("pvpA.lessThan=" + UPDATED_PVP_A);
    }

    @Test
    @Transactional
    void getAllGamesByPvpAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpA is greater than DEFAULT_PVP_A
        defaultGameShouldNotBeFound("pvpA.greaterThan=" + DEFAULT_PVP_A);

        // Get all the gameList where pvpA is greater than SMALLER_PVP_A
        defaultGameShouldBeFound("pvpA.greaterThan=" + SMALLER_PVP_A);
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB equals to DEFAULT_PVP_B
        defaultGameShouldBeFound("pvpB.equals=" + DEFAULT_PVP_B);

        // Get all the gameList where pvpB equals to UPDATED_PVP_B
        defaultGameShouldNotBeFound("pvpB.equals=" + UPDATED_PVP_B);
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB not equals to DEFAULT_PVP_B
        defaultGameShouldNotBeFound("pvpB.notEquals=" + DEFAULT_PVP_B);

        // Get all the gameList where pvpB not equals to UPDATED_PVP_B
        defaultGameShouldBeFound("pvpB.notEquals=" + UPDATED_PVP_B);
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB in DEFAULT_PVP_B or UPDATED_PVP_B
        defaultGameShouldBeFound("pvpB.in=" + DEFAULT_PVP_B + "," + UPDATED_PVP_B);

        // Get all the gameList where pvpB equals to UPDATED_PVP_B
        defaultGameShouldNotBeFound("pvpB.in=" + UPDATED_PVP_B);
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB is not null
        defaultGameShouldBeFound("pvpB.specified=true");

        // Get all the gameList where pvpB is null
        defaultGameShouldNotBeFound("pvpB.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB is greater than or equal to DEFAULT_PVP_B
        defaultGameShouldBeFound("pvpB.greaterThanOrEqual=" + DEFAULT_PVP_B);

        // Get all the gameList where pvpB is greater than or equal to UPDATED_PVP_B
        defaultGameShouldNotBeFound("pvpB.greaterThanOrEqual=" + UPDATED_PVP_B);
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB is less than or equal to DEFAULT_PVP_B
        defaultGameShouldBeFound("pvpB.lessThanOrEqual=" + DEFAULT_PVP_B);

        // Get all the gameList where pvpB is less than or equal to SMALLER_PVP_B
        defaultGameShouldNotBeFound("pvpB.lessThanOrEqual=" + SMALLER_PVP_B);
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB is less than DEFAULT_PVP_B
        defaultGameShouldNotBeFound("pvpB.lessThan=" + DEFAULT_PVP_B);

        // Get all the gameList where pvpB is less than UPDATED_PVP_B
        defaultGameShouldBeFound("pvpB.lessThan=" + UPDATED_PVP_B);
    }

    @Test
    @Transactional
    void getAllGamesByPvpBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pvpB is greater than DEFAULT_PVP_B
        defaultGameShouldNotBeFound("pvpB.greaterThan=" + DEFAULT_PVP_B);

        // Get all the gameList where pvpB is greater than SMALLER_PVP_B
        defaultGameShouldBeFound("pvpB.greaterThan=" + SMALLER_PVP_B);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft equals to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.equals=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft equals to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.equals=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft not equals to DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.notEquals=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft not equals to UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.notEquals=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft in DEFAULT_TIME_LEFT or UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.in=" + DEFAULT_TIME_LEFT + "," + UPDATED_TIME_LEFT);

        // Get all the gameList where timeLeft equals to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.in=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is not null
        defaultGameShouldBeFound("timeLeft.specified=true");

        // Get all the gameList where timeLeft is null
        defaultGameShouldNotBeFound("timeLeft.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is greater than or equal to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.greaterThanOrEqual=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is greater than or equal to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.greaterThanOrEqual=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is less than or equal to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.lessThanOrEqual=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is less than or equal to SMALLER_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.lessThanOrEqual=" + SMALLER_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is less than DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.lessThan=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is less than UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.lessThan=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is greater than DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.greaterThan=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is greater than SMALLER_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.greaterThan=" + SMALLER_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status equals to DEFAULT_STATUS
        defaultGameShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the gameList where status equals to UPDATED_STATUS
        defaultGameShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status not equals to DEFAULT_STATUS
        defaultGameShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the gameList where status not equals to UPDATED_STATUS
        defaultGameShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultGameShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the gameList where status equals to UPDATED_STATUS
        defaultGameShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status is not null
        defaultGameShouldBeFound("status.specified=true");

        // Get all the gameList where status is null
        defaultGameShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByTeamAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        Team teamA = TeamResourceIT.createEntity(em);
        em.persist(teamA);
        em.flush();
        game.setTeamA(teamA);
        gameRepository.saveAndFlush(game);
        Long teamAId = teamA.getId();

        // Get all the gameList where teamA equals to teamAId
        defaultGameShouldBeFound("teamAId.equals=" + teamAId);

        // Get all the gameList where teamA equals to (teamAId + 1)
        defaultGameShouldNotBeFound("teamAId.equals=" + (teamAId + 1));
    }

    @Test
    @Transactional
    void getAllGamesByTeamBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        Team teamB = TeamResourceIT.createEntity(em);
        em.persist(teamB);
        em.flush();
        game.setTeamB(teamB);
        gameRepository.saveAndFlush(game);
        Long teamBId = teamB.getId();

        // Get all the gameList where teamB equals to teamBId
        defaultGameShouldBeFound("teamBId.equals=" + teamBId);

        // Get all the gameList where teamB equals to (teamBId + 1)
        defaultGameShouldNotBeFound("teamBId.equals=" + (teamBId + 1));
    }

    @Test
    @Transactional
    void getAllGamesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        game.setCategory(category);
        gameRepository.saveAndFlush(game);
        Long categoryId = category.getId();

        // Get all the gameList where category equals to categoryId
        defaultGameShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the gameList where category equals to (categoryId + 1)
        defaultGameShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGameShouldBeFound(String filter) throws Exception {
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].gameNum").value(hasItem(DEFAULT_GAME_NUM)))
            .andExpect(jsonPath("$.[*].clasif").value(hasItem(DEFAULT_CLASIF)))
            .andExpect(jsonPath("$.[*].splitdeckNum").value(hasItem(DEFAULT_SPLITDECK_NUM)))
            .andExpect(jsonPath("$.[*].pointsA").value(hasItem(DEFAULT_POINTS_A)))
            .andExpect(jsonPath("$.[*].pointsB").value(hasItem(DEFAULT_POINTS_B)))
            .andExpect(jsonPath("$.[*].overA").value(hasItem(DEFAULT_OVER_A)))
            .andExpect(jsonPath("$.[*].overB").value(hasItem(DEFAULT_OVER_B)))
            .andExpect(jsonPath("$.[*].pvpA").value(hasItem(DEFAULT_PVP_A)))
            .andExpect(jsonPath("$.[*].pvpB").value(hasItem(DEFAULT_PVP_B)))
            .andExpect(jsonPath("$.[*].timeLeft").value(hasItem(DEFAULT_TIME_LEFT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGameShouldNotBeFound(String filter) throws Exception {
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findById(game.getId()).get();
        // Disconnect from session so that the updates on updatedGame are not directly saved in db
        em.detach(updatedGame);
        updatedGame
            .gameNum(UPDATED_GAME_NUM)
            .clasif(UPDATED_CLASIF)
            .splitdeckNum(UPDATED_SPLITDECK_NUM)
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .overA(UPDATED_OVER_A)
            .overB(UPDATED_OVER_B)
            .pvpA(UPDATED_PVP_A)
            .pvpB(UPDATED_PVP_B)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS);
        GameDTO gameDTO = gameMapper.toDto(updatedGame);

        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getGameNum()).isEqualTo(UPDATED_GAME_NUM);
        assertThat(testGame.getClasif()).isEqualTo(UPDATED_CLASIF);
        assertThat(testGame.getSplitdeckNum()).isEqualTo(UPDATED_SPLITDECK_NUM);
        assertThat(testGame.getPointsA()).isEqualTo(UPDATED_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(UPDATED_POINTS_B);
        assertThat(testGame.getOverA()).isEqualTo(UPDATED_OVER_A);
        assertThat(testGame.getOverB()).isEqualTo(UPDATED_OVER_B);
        assertThat(testGame.getPvpA()).isEqualTo(UPDATED_PVP_A);
        assertThat(testGame.getPvpB()).isEqualTo(UPDATED_PVP_B);
        assertThat(testGame.getTimeLeft()).isEqualTo(UPDATED_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameWithPatch() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        partialUpdatedGame
            .gameNum(UPDATED_GAME_NUM)
            .clasif(UPDATED_CLASIF)
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .overA(UPDATED_OVER_A)
            .pvpA(UPDATED_PVP_A)
            .pvpB(UPDATED_PVP_B)
            .status(UPDATED_STATUS);

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getGameNum()).isEqualTo(UPDATED_GAME_NUM);
        assertThat(testGame.getClasif()).isEqualTo(UPDATED_CLASIF);
        assertThat(testGame.getSplitdeckNum()).isEqualTo(DEFAULT_SPLITDECK_NUM);
        assertThat(testGame.getPointsA()).isEqualTo(UPDATED_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(UPDATED_POINTS_B);
        assertThat(testGame.getOverA()).isEqualTo(UPDATED_OVER_A);
        assertThat(testGame.getOverB()).isEqualTo(DEFAULT_OVER_B);
        assertThat(testGame.getPvpA()).isEqualTo(UPDATED_PVP_A);
        assertThat(testGame.getPvpB()).isEqualTo(UPDATED_PVP_B);
        assertThat(testGame.getTimeLeft()).isEqualTo(DEFAULT_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateGameWithPatch() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        partialUpdatedGame
            .gameNum(UPDATED_GAME_NUM)
            .clasif(UPDATED_CLASIF)
            .splitdeckNum(UPDATED_SPLITDECK_NUM)
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .overA(UPDATED_OVER_A)
            .overB(UPDATED_OVER_B)
            .pvpA(UPDATED_PVP_A)
            .pvpB(UPDATED_PVP_B)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS);

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getGameNum()).isEqualTo(UPDATED_GAME_NUM);
        assertThat(testGame.getClasif()).isEqualTo(UPDATED_CLASIF);
        assertThat(testGame.getSplitdeckNum()).isEqualTo(UPDATED_SPLITDECK_NUM);
        assertThat(testGame.getPointsA()).isEqualTo(UPDATED_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(UPDATED_POINTS_B);
        assertThat(testGame.getOverA()).isEqualTo(UPDATED_OVER_A);
        assertThat(testGame.getOverB()).isEqualTo(UPDATED_OVER_B);
        assertThat(testGame.getPvpA()).isEqualTo(UPDATED_PVP_A);
        assertThat(testGame.getPvpB()).isEqualTo(UPDATED_PVP_B);
        assertThat(testGame.getTimeLeft()).isEqualTo(UPDATED_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Delete the game
        restGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, game.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
