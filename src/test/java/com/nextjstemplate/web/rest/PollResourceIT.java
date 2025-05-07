package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.domain.UserProfile;
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
    void getPollsByIdFiltering() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        Long id = poll.getId();

        defaultPollShouldBeFound("id.equals=" + id);
        defaultPollShouldNotBeFound("id.notEquals=" + id);

        defaultPollShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPollShouldNotBeFound("id.greaterThan=" + id);

        defaultPollShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPollShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPollsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where title equals to DEFAULT_TITLE
        defaultPollShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the pollList where title equals to UPDATED_TITLE
        defaultPollShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPollsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPollShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the pollList where title equals to UPDATED_TITLE
        defaultPollShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPollsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where title is not null
        defaultPollShouldBeFound("title.specified=true");

        // Get all the pollList where title is null
        defaultPollShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllPollsByTitleContainsSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where title contains DEFAULT_TITLE
        defaultPollShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the pollList where title contains UPDATED_TITLE
        defaultPollShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPollsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where title does not contain DEFAULT_TITLE
        defaultPollShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the pollList where title does not contain UPDATED_TITLE
        defaultPollShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPollsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where description equals to DEFAULT_DESCRIPTION
        defaultPollShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the pollList where description equals to UPDATED_DESCRIPTION
        defaultPollShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPollsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPollShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the pollList where description equals to UPDATED_DESCRIPTION
        defaultPollShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPollsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where description is not null
        defaultPollShouldBeFound("description.specified=true");

        // Get all the pollList where description is null
        defaultPollShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPollsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where description contains DEFAULT_DESCRIPTION
        defaultPollShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the pollList where description contains UPDATED_DESCRIPTION
        defaultPollShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPollsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where description does not contain DEFAULT_DESCRIPTION
        defaultPollShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the pollList where description does not contain UPDATED_DESCRIPTION
        defaultPollShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPollsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPollShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the pollList where isActive equals to UPDATED_IS_ACTIVE
        defaultPollShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPollsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPollShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the pollList where isActive equals to UPDATED_IS_ACTIVE
        defaultPollShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPollsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where isActive is not null
        defaultPollShouldBeFound("isActive.specified=true");

        // Get all the pollList where isActive is null
        defaultPollShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllPollsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where startDate equals to DEFAULT_START_DATE
        defaultPollShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the pollList where startDate equals to UPDATED_START_DATE
        defaultPollShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPollsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultPollShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the pollList where startDate equals to UPDATED_START_DATE
        defaultPollShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPollsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where startDate is not null
        defaultPollShouldBeFound("startDate.specified=true");

        // Get all the pollList where startDate is null
        defaultPollShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPollsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where endDate equals to DEFAULT_END_DATE
        defaultPollShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the pollList where endDate equals to UPDATED_END_DATE
        defaultPollShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPollsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultPollShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the pollList where endDate equals to UPDATED_END_DATE
        defaultPollShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPollsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where endDate is not null
        defaultPollShouldBeFound("endDate.specified=true");

        // Get all the pollList where endDate is null
        defaultPollShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPollsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where createdAt equals to DEFAULT_CREATED_AT
        defaultPollShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the pollList where createdAt equals to UPDATED_CREATED_AT
        defaultPollShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPollsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPollShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the pollList where createdAt equals to UPDATED_CREATED_AT
        defaultPollShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPollsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where createdAt is not null
        defaultPollShouldBeFound("createdAt.specified=true");

        // Get all the pollList where createdAt is null
        defaultPollShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPollsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPollShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the pollList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPollShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPollsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPollShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the pollList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPollShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPollsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollRepository.saveAndFlush(poll);

        // Get all the pollList where updatedAt is not null
        defaultPollShouldBeFound("updatedAt.specified=true");

        // Get all the pollList where updatedAt is null
        defaultPollShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPollsByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            pollRepository.saveAndFlush(poll);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        poll.setEvent(event);
        pollRepository.saveAndFlush(poll);
        Long eventId = event.getId();
        // Get all the pollList where event equals to eventId
        defaultPollShouldBeFound("eventId.equals=" + eventId);

        // Get all the pollList where event equals to (eventId + 1)
        defaultPollShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllPollsByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            pollRepository.saveAndFlush(poll);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        poll.setCreatedBy(createdBy);
        pollRepository.saveAndFlush(poll);
        Long createdById = createdBy.getId();
        // Get all the pollList where createdBy equals to createdById
        defaultPollShouldBeFound("createdById.equals=" + createdById);

        // Get all the pollList where createdBy equals to (createdById + 1)
        defaultPollShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPollShouldBeFound(String filter) throws Exception {
        restPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restPollMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPollShouldNotBeFound(String filter) throws Exception {
        restPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPollMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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

        partialUpdatedPoll.createdAt(UPDATED_CREATED_AT);

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
        assertThat(testPoll.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPoll.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPoll.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPoll.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPoll.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPoll.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPoll.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
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
