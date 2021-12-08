package com.pbpoints.console.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.console.IntegrationTest;
import com.pbpoints.console.domain.Tournament;
import com.pbpoints.console.repository.TournamentRepository;
import com.pbpoints.console.service.criteria.TournamentCriteria;
import com.pbpoints.console.service.dto.TournamentDTO;
import com.pbpoints.console.service.mapper.TournamentMapper;
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
 * Integration tests for the {@link TournamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TournamentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tournaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTournamentMockMvc;

    private Tournament tournament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournament createEntity(EntityManager em) {
        Tournament tournament = new Tournament().name(DEFAULT_NAME);
        return tournament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournament createUpdatedEntity(EntityManager em) {
        Tournament tournament = new Tournament().name(UPDATED_NAME);
        return tournament;
    }

    @BeforeEach
    public void initTest() {
        tournament = createEntity(em);
    }

    @Test
    @Transactional
    void createTournament() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();
        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);
        restTournamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isCreated());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate + 1);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTournamentWithExistingId() throws Exception {
        // Create the Tournament with an existing ID
        tournament.setId(1L);
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tournamentRepository.findAll().size();
        // set the field null
        tournament.setName(null);

        // Create the Tournament, which fails.
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        restTournamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isBadRequest());

        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTournaments() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get the tournament
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL_ID, tournament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tournament.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTournamentsByIdFiltering() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        Long id = tournament.getId();

        defaultTournamentShouldBeFound("id.equals=" + id);
        defaultTournamentShouldNotBeFound("id.notEquals=" + id);

        defaultTournamentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTournamentShouldNotBeFound("id.greaterThan=" + id);

        defaultTournamentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTournamentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name equals to DEFAULT_NAME
        defaultTournamentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tournamentList where name equals to UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name not equals to DEFAULT_NAME
        defaultTournamentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tournamentList where name not equals to UPDATED_NAME
        defaultTournamentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTournamentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tournamentList where name equals to UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name is not null
        defaultTournamentShouldBeFound("name.specified=true");

        // Get all the tournamentList where name is null
        defaultTournamentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentsByNameContainsSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name contains DEFAULT_NAME
        defaultTournamentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tournamentList where name contains UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name does not contain DEFAULT_NAME
        defaultTournamentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tournamentList where name does not contain UPDATED_NAME
        defaultTournamentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTournamentShouldBeFound(String filter) throws Exception {
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTournamentShouldNotBeFound(String filter) throws Exception {
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTournament() throws Exception {
        // Get the tournament
        restTournamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament
        Tournament updatedTournament = tournamentRepository.findById(tournament.getId()).get();
        // Disconnect from session so that the updates on updatedTournament are not directly saved in db
        em.detach(updatedTournament);
        updatedTournament.name(UPDATED_NAME);
        TournamentDTO tournamentDTO = tournamentMapper.toDto(updatedTournament);

        restTournamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tournamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tournamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTournamentWithPatch() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament using partial update
        Tournament partialUpdatedTournament = new Tournament();
        partialUpdatedTournament.setId(tournament.getId());

        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTournament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTournament))
            )
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTournamentWithPatch() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament using partial update
        Tournament partialUpdatedTournament = new Tournament();
        partialUpdatedTournament.setId(tournament.getId());

        partialUpdatedTournament.name(UPDATED_NAME);

        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTournament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTournament))
            )
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tournamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeDelete = tournamentRepository.findAll().size();

        // Delete the tournament
        restTournamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, tournament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
