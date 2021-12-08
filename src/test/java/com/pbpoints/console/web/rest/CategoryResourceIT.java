package com.pbpoints.console.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.console.IntegrationTest;
import com.pbpoints.console.domain.Category;
import com.pbpoints.console.domain.Event;
import com.pbpoints.console.repository.CategoryRepository;
import com.pbpoints.console.service.criteria.CategoryCriteria;
import com.pbpoints.console.service.dto.CategoryDTO;
import com.pbpoints.console.service.mapper.CategoryMapper;
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
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GAME_TIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_GAME_TIME_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_GAME_TIME = 1;
    private static final Integer UPDATED_GAME_TIME = 2;
    private static final Integer SMALLER_GAME_TIME = 1 - 1;

    private static final String DEFAULT_STOP_TIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STOP_TIME_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOP_TIME = 1;
    private static final Integer UPDATED_STOP_TIME = 2;
    private static final Integer SMALLER_STOP_TIME = 1 - 1;

    private static final String DEFAULT_STOP_SD_TIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STOP_SD_TIME_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOP_SD_TIME = 1;
    private static final Integer UPDATED_STOP_SD_TIME = 2;
    private static final Integer SMALLER_STOP_SD_TIME = 1 - 1;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final Integer DEFAULT_DIF_POINTS = 1;
    private static final Integer UPDATED_DIF_POINTS = 2;
    private static final Integer SMALLER_DIF_POINTS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private Category category;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity(EntityManager em) {
        Category category = new Category()
            .name(DEFAULT_NAME)
            .gameTimeType(DEFAULT_GAME_TIME_TYPE)
            .gameTime(DEFAULT_GAME_TIME)
            .stopTimeType(DEFAULT_STOP_TIME_TYPE)
            .stopTime(DEFAULT_STOP_TIME)
            .stopSdTimeType(DEFAULT_STOP_SD_TIME_TYPE)
            .stopSdTime(DEFAULT_STOP_SD_TIME)
            .points(DEFAULT_POINTS)
            .difPoints(DEFAULT_DIF_POINTS);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        category.setEvent(event);
        return category;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity(EntityManager em) {
        Category category = new Category()
            .name(UPDATED_NAME)
            .gameTimeType(UPDATED_GAME_TIME_TYPE)
            .gameTime(UPDATED_GAME_TIME)
            .stopTimeType(UPDATED_STOP_TIME_TYPE)
            .stopTime(UPDATED_STOP_TIME)
            .stopSdTimeType(UPDATED_STOP_SD_TIME_TYPE)
            .stopSdTime(UPDATED_STOP_SD_TIME)
            .points(UPDATED_POINTS)
            .difPoints(UPDATED_DIF_POINTS);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        category.setEvent(event);
        return category;
    }

    @BeforeEach
    public void initTest() {
        category = createEntity(em);
    }

    @Test
    @Transactional
    void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();
        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getGameTimeType()).isEqualTo(DEFAULT_GAME_TIME_TYPE);
        assertThat(testCategory.getGameTime()).isEqualTo(DEFAULT_GAME_TIME);
        assertThat(testCategory.getStopTimeType()).isEqualTo(DEFAULT_STOP_TIME_TYPE);
        assertThat(testCategory.getStopTime()).isEqualTo(DEFAULT_STOP_TIME);
        assertThat(testCategory.getStopSdTimeType()).isEqualTo(DEFAULT_STOP_SD_TIME_TYPE);
        assertThat(testCategory.getStopSdTime()).isEqualTo(DEFAULT_STOP_SD_TIME);
        assertThat(testCategory.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCategory.getDifPoints()).isEqualTo(DEFAULT_DIF_POINTS);
    }

    @Test
    @Transactional
    void createCategoryWithExistingId() throws Exception {
        // Create the Category with an existing ID
        category.setId(1L);
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setName(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGameTimeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setGameTimeType(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGameTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setGameTime(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStopTimeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setStopTimeType(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStopSdTimeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setStopSdTimeType(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStopSdTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setStopSdTime(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDifPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setDifPoints(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gameTimeType").value(hasItem(DEFAULT_GAME_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].gameTime").value(hasItem(DEFAULT_GAME_TIME)))
            .andExpect(jsonPath("$.[*].stopTimeType").value(hasItem(DEFAULT_STOP_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(DEFAULT_STOP_TIME)))
            .andExpect(jsonPath("$.[*].stopSdTimeType").value(hasItem(DEFAULT_STOP_SD_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].stopSdTime").value(hasItem(DEFAULT_STOP_SD_TIME)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].difPoints").value(hasItem(DEFAULT_DIF_POINTS)));
    }

    @Test
    @Transactional
    void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gameTimeType").value(DEFAULT_GAME_TIME_TYPE))
            .andExpect(jsonPath("$.gameTime").value(DEFAULT_GAME_TIME))
            .andExpect(jsonPath("$.stopTimeType").value(DEFAULT_STOP_TIME_TYPE))
            .andExpect(jsonPath("$.stopTime").value(DEFAULT_STOP_TIME))
            .andExpect(jsonPath("$.stopSdTimeType").value(DEFAULT_STOP_SD_TIME_TYPE))
            .andExpect(jsonPath("$.stopSdTime").value(DEFAULT_STOP_SD_TIME))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.difPoints").value(DEFAULT_DIF_POINTS));
    }

    @Test
    @Transactional
    void getCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        Long id = category.getId();

        defaultCategoryShouldBeFound("id.equals=" + id);
        defaultCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name equals to DEFAULT_NAME
        defaultCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name not equals to DEFAULT_NAME
        defaultCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the categoryList where name not equals to UPDATED_NAME
        defaultCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name is not null
        defaultCategoryShouldBeFound("name.specified=true");

        // Get all the categoryList where name is null
        defaultCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name contains DEFAULT_NAME
        defaultCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the categoryList where name contains UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name does not contain DEFAULT_NAME
        defaultCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the categoryList where name does not contain UPDATED_NAME
        defaultCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType equals to DEFAULT_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.equals=" + DEFAULT_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType equals to UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.equals=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType not equals to DEFAULT_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.notEquals=" + DEFAULT_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType not equals to UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.notEquals=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType in DEFAULT_GAME_TIME_TYPE or UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.in=" + DEFAULT_GAME_TIME_TYPE + "," + UPDATED_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType equals to UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.in=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType is not null
        defaultCategoryShouldBeFound("gameTimeType.specified=true");

        // Get all the categoryList where gameTimeType is null
        defaultCategoryShouldNotBeFound("gameTimeType.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeTypeContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType contains DEFAULT_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.contains=" + DEFAULT_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType contains UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.contains=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType does not contain DEFAULT_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.doesNotContain=" + DEFAULT_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType does not contain UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.doesNotContain=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime equals to DEFAULT_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.equals=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime equals to UPDATED_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.equals=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime not equals to DEFAULT_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.notEquals=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime not equals to UPDATED_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.notEquals=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime in DEFAULT_GAME_TIME or UPDATED_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.in=" + DEFAULT_GAME_TIME + "," + UPDATED_GAME_TIME);

        // Get all the categoryList where gameTime equals to UPDATED_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.in=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is not null
        defaultCategoryShouldBeFound("gameTime.specified=true");

        // Get all the categoryList where gameTime is null
        defaultCategoryShouldNotBeFound("gameTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is greater than or equal to DEFAULT_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.greaterThanOrEqual=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is greater than or equal to UPDATED_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.greaterThanOrEqual=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is less than or equal to DEFAULT_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.lessThanOrEqual=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is less than or equal to SMALLER_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.lessThanOrEqual=" + SMALLER_GAME_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is less than DEFAULT_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.lessThan=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is less than UPDATED_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.lessThan=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByGameTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is greater than DEFAULT_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.greaterThan=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is greater than SMALLER_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.greaterThan=" + SMALLER_GAME_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType equals to DEFAULT_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.equals=" + DEFAULT_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType equals to UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.equals=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType not equals to DEFAULT_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.notEquals=" + DEFAULT_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType not equals to UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.notEquals=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType in DEFAULT_STOP_TIME_TYPE or UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.in=" + DEFAULT_STOP_TIME_TYPE + "," + UPDATED_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType equals to UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.in=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType is not null
        defaultCategoryShouldBeFound("stopTimeType.specified=true");

        // Get all the categoryList where stopTimeType is null
        defaultCategoryShouldNotBeFound("stopTimeType.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeTypeContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType contains DEFAULT_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.contains=" + DEFAULT_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType contains UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.contains=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType does not contain DEFAULT_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.doesNotContain=" + DEFAULT_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType does not contain UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.doesNotContain=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime equals to DEFAULT_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.equals=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime equals to UPDATED_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.equals=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime not equals to DEFAULT_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.notEquals=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime not equals to UPDATED_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.notEquals=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime in DEFAULT_STOP_TIME or UPDATED_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.in=" + DEFAULT_STOP_TIME + "," + UPDATED_STOP_TIME);

        // Get all the categoryList where stopTime equals to UPDATED_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.in=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is not null
        defaultCategoryShouldBeFound("stopTime.specified=true");

        // Get all the categoryList where stopTime is null
        defaultCategoryShouldNotBeFound("stopTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is greater than or equal to DEFAULT_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.greaterThanOrEqual=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is greater than or equal to UPDATED_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.greaterThanOrEqual=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is less than or equal to DEFAULT_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.lessThanOrEqual=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is less than or equal to SMALLER_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.lessThanOrEqual=" + SMALLER_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is less than DEFAULT_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.lessThan=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is less than UPDATED_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.lessThan=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is greater than DEFAULT_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.greaterThan=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is greater than SMALLER_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.greaterThan=" + SMALLER_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTimeType equals to DEFAULT_STOP_SD_TIME_TYPE
        defaultCategoryShouldBeFound("stopSdTimeType.equals=" + DEFAULT_STOP_SD_TIME_TYPE);

        // Get all the categoryList where stopSdTimeType equals to UPDATED_STOP_SD_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopSdTimeType.equals=" + UPDATED_STOP_SD_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTimeType not equals to DEFAULT_STOP_SD_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopSdTimeType.notEquals=" + DEFAULT_STOP_SD_TIME_TYPE);

        // Get all the categoryList where stopSdTimeType not equals to UPDATED_STOP_SD_TIME_TYPE
        defaultCategoryShouldBeFound("stopSdTimeType.notEquals=" + UPDATED_STOP_SD_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTimeType in DEFAULT_STOP_SD_TIME_TYPE or UPDATED_STOP_SD_TIME_TYPE
        defaultCategoryShouldBeFound("stopSdTimeType.in=" + DEFAULT_STOP_SD_TIME_TYPE + "," + UPDATED_STOP_SD_TIME_TYPE);

        // Get all the categoryList where stopSdTimeType equals to UPDATED_STOP_SD_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopSdTimeType.in=" + UPDATED_STOP_SD_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTimeType is not null
        defaultCategoryShouldBeFound("stopSdTimeType.specified=true");

        // Get all the categoryList where stopSdTimeType is null
        defaultCategoryShouldNotBeFound("stopSdTimeType.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeTypeContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTimeType contains DEFAULT_STOP_SD_TIME_TYPE
        defaultCategoryShouldBeFound("stopSdTimeType.contains=" + DEFAULT_STOP_SD_TIME_TYPE);

        // Get all the categoryList where stopSdTimeType contains UPDATED_STOP_SD_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopSdTimeType.contains=" + UPDATED_STOP_SD_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTimeType does not contain DEFAULT_STOP_SD_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopSdTimeType.doesNotContain=" + DEFAULT_STOP_SD_TIME_TYPE);

        // Get all the categoryList where stopSdTimeType does not contain UPDATED_STOP_SD_TIME_TYPE
        defaultCategoryShouldBeFound("stopSdTimeType.doesNotContain=" + UPDATED_STOP_SD_TIME_TYPE);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime equals to DEFAULT_STOP_SD_TIME
        defaultCategoryShouldBeFound("stopSdTime.equals=" + DEFAULT_STOP_SD_TIME);

        // Get all the categoryList where stopSdTime equals to UPDATED_STOP_SD_TIME
        defaultCategoryShouldNotBeFound("stopSdTime.equals=" + UPDATED_STOP_SD_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime not equals to DEFAULT_STOP_SD_TIME
        defaultCategoryShouldNotBeFound("stopSdTime.notEquals=" + DEFAULT_STOP_SD_TIME);

        // Get all the categoryList where stopSdTime not equals to UPDATED_STOP_SD_TIME
        defaultCategoryShouldBeFound("stopSdTime.notEquals=" + UPDATED_STOP_SD_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime in DEFAULT_STOP_SD_TIME or UPDATED_STOP_SD_TIME
        defaultCategoryShouldBeFound("stopSdTime.in=" + DEFAULT_STOP_SD_TIME + "," + UPDATED_STOP_SD_TIME);

        // Get all the categoryList where stopSdTime equals to UPDATED_STOP_SD_TIME
        defaultCategoryShouldNotBeFound("stopSdTime.in=" + UPDATED_STOP_SD_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime is not null
        defaultCategoryShouldBeFound("stopSdTime.specified=true");

        // Get all the categoryList where stopSdTime is null
        defaultCategoryShouldNotBeFound("stopSdTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime is greater than or equal to DEFAULT_STOP_SD_TIME
        defaultCategoryShouldBeFound("stopSdTime.greaterThanOrEqual=" + DEFAULT_STOP_SD_TIME);

        // Get all the categoryList where stopSdTime is greater than or equal to UPDATED_STOP_SD_TIME
        defaultCategoryShouldNotBeFound("stopSdTime.greaterThanOrEqual=" + UPDATED_STOP_SD_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime is less than or equal to DEFAULT_STOP_SD_TIME
        defaultCategoryShouldBeFound("stopSdTime.lessThanOrEqual=" + DEFAULT_STOP_SD_TIME);

        // Get all the categoryList where stopSdTime is less than or equal to SMALLER_STOP_SD_TIME
        defaultCategoryShouldNotBeFound("stopSdTime.lessThanOrEqual=" + SMALLER_STOP_SD_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime is less than DEFAULT_STOP_SD_TIME
        defaultCategoryShouldNotBeFound("stopSdTime.lessThan=" + DEFAULT_STOP_SD_TIME);

        // Get all the categoryList where stopSdTime is less than UPDATED_STOP_SD_TIME
        defaultCategoryShouldBeFound("stopSdTime.lessThan=" + UPDATED_STOP_SD_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStopSdTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopSdTime is greater than DEFAULT_STOP_SD_TIME
        defaultCategoryShouldNotBeFound("stopSdTime.greaterThan=" + DEFAULT_STOP_SD_TIME);

        // Get all the categoryList where stopSdTime is greater than SMALLER_STOP_SD_TIME
        defaultCategoryShouldBeFound("stopSdTime.greaterThan=" + SMALLER_STOP_SD_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points equals to DEFAULT_POINTS
        defaultCategoryShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the categoryList where points equals to UPDATED_POINTS
        defaultCategoryShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points not equals to DEFAULT_POINTS
        defaultCategoryShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the categoryList where points not equals to UPDATED_POINTS
        defaultCategoryShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultCategoryShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the categoryList where points equals to UPDATED_POINTS
        defaultCategoryShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points is not null
        defaultCategoryShouldBeFound("points.specified=true");

        // Get all the categoryList where points is null
        defaultCategoryShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points is greater than or equal to DEFAULT_POINTS
        defaultCategoryShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the categoryList where points is greater than or equal to UPDATED_POINTS
        defaultCategoryShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points is less than or equal to DEFAULT_POINTS
        defaultCategoryShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the categoryList where points is less than or equal to SMALLER_POINTS
        defaultCategoryShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points is less than DEFAULT_POINTS
        defaultCategoryShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the categoryList where points is less than UPDATED_POINTS
        defaultCategoryShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where points is greater than DEFAULT_POINTS
        defaultCategoryShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the categoryList where points is greater than SMALLER_POINTS
        defaultCategoryShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints equals to DEFAULT_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.equals=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints equals to UPDATED_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.equals=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints not equals to DEFAULT_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.notEquals=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints not equals to UPDATED_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.notEquals=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints in DEFAULT_DIF_POINTS or UPDATED_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.in=" + DEFAULT_DIF_POINTS + "," + UPDATED_DIF_POINTS);

        // Get all the categoryList where difPoints equals to UPDATED_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.in=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is not null
        defaultCategoryShouldBeFound("difPoints.specified=true");

        // Get all the categoryList where difPoints is null
        defaultCategoryShouldNotBeFound("difPoints.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is greater than or equal to DEFAULT_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.greaterThanOrEqual=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is greater than or equal to UPDATED_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.greaterThanOrEqual=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is less than or equal to DEFAULT_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.lessThanOrEqual=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is less than or equal to SMALLER_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.lessThanOrEqual=" + SMALLER_DIF_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is less than DEFAULT_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.lessThan=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is less than UPDATED_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.lessThan=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByDifPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is greater than DEFAULT_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.greaterThan=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is greater than SMALLER_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.greaterThan=" + SMALLER_DIF_POINTS);
    }

    @Test
    @Transactional
    void getAllCategoriesByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        category.setEvent(event);
        categoryRepository.saveAndFlush(category);
        Long eventId = event.getId();

        // Get all the categoryList where event equals to eventId
        defaultCategoryShouldBeFound("eventId.equals=" + eventId);

        // Get all the categoryList where event equals to (eventId + 1)
        defaultCategoryShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryShouldBeFound(String filter) throws Exception {
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gameTimeType").value(hasItem(DEFAULT_GAME_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].gameTime").value(hasItem(DEFAULT_GAME_TIME)))
            .andExpect(jsonPath("$.[*].stopTimeType").value(hasItem(DEFAULT_STOP_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(DEFAULT_STOP_TIME)))
            .andExpect(jsonPath("$.[*].stopSdTimeType").value(hasItem(DEFAULT_STOP_SD_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].stopSdTime").value(hasItem(DEFAULT_STOP_SD_TIME)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].difPoints").value(hasItem(DEFAULT_DIF_POINTS)));

        // Check, that the count call also returns 1
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryShouldNotBeFound(String filter) throws Exception {
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory
            .name(UPDATED_NAME)
            .gameTimeType(UPDATED_GAME_TIME_TYPE)
            .gameTime(UPDATED_GAME_TIME)
            .stopTimeType(UPDATED_STOP_TIME_TYPE)
            .stopTime(UPDATED_STOP_TIME)
            .stopSdTimeType(UPDATED_STOP_SD_TIME_TYPE)
            .stopSdTime(UPDATED_STOP_SD_TIME)
            .points(UPDATED_POINTS)
            .difPoints(UPDATED_DIF_POINTS);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategory);

        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getGameTimeType()).isEqualTo(UPDATED_GAME_TIME_TYPE);
        assertThat(testCategory.getGameTime()).isEqualTo(UPDATED_GAME_TIME);
        assertThat(testCategory.getStopTimeType()).isEqualTo(UPDATED_STOP_TIME_TYPE);
        assertThat(testCategory.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testCategory.getStopSdTimeType()).isEqualTo(UPDATED_STOP_SD_TIME_TYPE);
        assertThat(testCategory.getStopSdTime()).isEqualTo(UPDATED_STOP_SD_TIME);
        assertThat(testCategory.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCategory.getDifPoints()).isEqualTo(UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.stopSdTimeType(UPDATED_STOP_SD_TIME_TYPE).points(UPDATED_POINTS);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getGameTimeType()).isEqualTo(DEFAULT_GAME_TIME_TYPE);
        assertThat(testCategory.getGameTime()).isEqualTo(DEFAULT_GAME_TIME);
        assertThat(testCategory.getStopTimeType()).isEqualTo(DEFAULT_STOP_TIME_TYPE);
        assertThat(testCategory.getStopTime()).isEqualTo(DEFAULT_STOP_TIME);
        assertThat(testCategory.getStopSdTimeType()).isEqualTo(UPDATED_STOP_SD_TIME_TYPE);
        assertThat(testCategory.getStopSdTime()).isEqualTo(DEFAULT_STOP_SD_TIME);
        assertThat(testCategory.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCategory.getDifPoints()).isEqualTo(DEFAULT_DIF_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory
            .name(UPDATED_NAME)
            .gameTimeType(UPDATED_GAME_TIME_TYPE)
            .gameTime(UPDATED_GAME_TIME)
            .stopTimeType(UPDATED_STOP_TIME_TYPE)
            .stopTime(UPDATED_STOP_TIME)
            .stopSdTimeType(UPDATED_STOP_SD_TIME_TYPE)
            .stopSdTime(UPDATED_STOP_SD_TIME)
            .points(UPDATED_POINTS)
            .difPoints(UPDATED_DIF_POINTS);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getGameTimeType()).isEqualTo(UPDATED_GAME_TIME_TYPE);
        assertThat(testCategory.getGameTime()).isEqualTo(UPDATED_GAME_TIME);
        assertThat(testCategory.getStopTimeType()).isEqualTo(UPDATED_STOP_TIME_TYPE);
        assertThat(testCategory.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testCategory.getStopSdTimeType()).isEqualTo(UPDATED_STOP_SD_TIME_TYPE);
        assertThat(testCategory.getStopSdTime()).isEqualTo(UPDATED_STOP_SD_TIME);
        assertThat(testCategory.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCategory.getDifPoints()).isEqualTo(UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, category.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
