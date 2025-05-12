package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.repository.PollRepository;
import com.nextjstemplate.service.dto.PollDTO;
import com.nextjstemplate.service.mapper.PollMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PollResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PollResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/polls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollMapper pollMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPollMockMvc;

    private Poll poll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poll createEntity(EntityManager em) {
        Poll poll = new Poll()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return poll;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poll createUpdatedEntity(EntityManager em) {
        Poll poll = new Poll()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return poll;
    }

    @BeforeEach
    public void initTest() {
        poll = createEntity(em);
    }

    @Test
    @Transactional
    void createPoll() throws Exception {
        int databaseSizeBeforeCreate = pollRepository.findAll().size();
        // Create the Poll
        PollDTO pollDTO = pollMapper.toDto(poll);
        restPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isCreated());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeCreate + 1);
        Poll testPoll = pollList.get(pollList.size() - 1);
        assertThat(testPoll.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPoll.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPoll.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPoll.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPoll.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPoll.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPoll.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createPollWithExistingId() throws Exception {
        // Create the Poll with an existing ID
        poll.setId(1L);
        PollDTO pollDTO = pollMapper.toDto(poll);

        int databaseSizeBeforeCreate = pollRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setTitle(null);

        // Create the Poll, which fails.
        PollDTO pollDTO = pollMapper.toDto(poll);

        restPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isBadRequest());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setStartDate(null);

        // Create the Poll, which fails.
        PollDTO pollDTO = pollMapper.toDto(poll);

        restPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isBadRequest());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setCreatedAt(null);

        // Create the Poll, which fails.
        PollDTO pollDTO = pollMapper.toDto(poll);

        restPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isBadRequest());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollRepository.findAll().size();
        // set the field null
        poll.setUpdatedAt(null);

        // Create the Poll, which fails.
        PollDTO pollDTO = pollMapper.toDto(poll);

        restPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isBadRequest());

        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPolls() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList
        restPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poll.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getPoll() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get the poll
        restPollMockMvc
            .perform(get(ENTITY_API_URL_ID, poll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poll.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPoll() throws Exception {
        // Get the poll
        restPollMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPoll() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        int databaseSizeBeforeUpdate = pollRepository.findAll().size();

        // Update the poll
        Poll updatedPoll = pollRepository.findById(poll.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPoll are not directly saved in db
        em.detach(updatedPoll);
        updatedPoll
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PollDTO pollDTO = pollMapper.toDto(updatedPoll);

        restPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollDTO))
            )
            .andExpect(status().isOk());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
        Poll testPoll = pollList.get(pollList.size() - 1);
        assertThat(testPoll.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPoll.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPoll.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPoll.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPoll.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPoll.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPoll() throws Exception {
        int databaseSizeBeforeUpdate = pollRepository.findAll().size();
        poll.setId(longCount.incrementAndGet());

        // Create the Poll
        PollDTO pollDTO = pollMapper.toDto(poll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoll() throws Exception {
        int databaseSizeBeforeUpdate = pollRepository.findAll().size();
        poll.setId(longCount.incrementAndGet());

        // Create the Poll
        PollDTO pollDTO = pollMapper.toDto(poll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoll() throws Exception {
        int databaseSizeBeforeUpdate = pollRepository.findAll().size();
        poll.setId(longCount.incrementAndGet());

        // Create the Poll
        PollDTO pollDTO = pollMapper.toDto(poll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePollWithPatch() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        int databaseSizeBeforeUpdate = pollRepository.findAll().size();

        // Update the poll using partial update
        Poll partialUpdatedPoll = new Poll();
        partialUpdatedPoll.setId(poll.getId());

        partialUpdatedPoll
            .title(UPDATED_TITLE)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .updatedAt(UPDATED_UPDATED_AT);

        restPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoll))
            )
            .andExpect(status().isOk());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
        Poll testPoll = pollList.get(pollList.size() - 1);
        assertThat(testPoll.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPoll.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPoll.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPoll.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPoll.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPoll.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePollWithPatch() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        int databaseSizeBeforeUpdate = pollRepository.findAll().size();

        // Update the poll using partial update
        Poll partialUpdatedPoll = new Poll();
        partialUpdatedPoll.setId(poll.getId());

        partialUpdatedPoll
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoll))
            )
            .andExpect(status().isOk());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
        Poll testPoll = pollList.get(pollList.size() - 1);
        assertThat(testPoll.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPoll.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPoll.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPoll.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPoll.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPoll.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPoll() throws Exception {
        int databaseSizeBeforeUpdate = pollRepository.findAll().size();
        poll.setId(longCount.incrementAndGet());

        // Create the Poll
        PollDTO pollDTO = pollMapper.toDto(poll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pollDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoll() throws Exception {
        int databaseSizeBeforeUpdate = pollRepository.findAll().size();
        poll.setId(longCount.incrementAndGet());

        // Create the Poll
        PollDTO pollDTO = pollMapper.toDto(poll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoll() throws Exception {
        int databaseSizeBeforeUpdate = pollRepository.findAll().size();
        poll.setId(longCount.incrementAndGet());

        // Create the Poll
        PollDTO pollDTO = pollMapper.toDto(poll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pollDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poll in the database
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoll() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        int databaseSizeBeforeDelete = pollRepository.findAll().size();

        // Delete the poll
        restPollMockMvc
            .perform(delete(ENTITY_API_URL_ID, poll.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Poll> pollList = pollRepository.findAll();
        assertThat(pollList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
