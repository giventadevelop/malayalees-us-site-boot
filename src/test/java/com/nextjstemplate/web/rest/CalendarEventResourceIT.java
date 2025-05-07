package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.CalendarEvent;
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.CalendarEventRepository;
import com.nextjstemplate.service.dto.CalendarEventDTO;
import com.nextjstemplate.service.mapper.CalendarEventMapper;
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
 * Integration tests for the {@link CalendarEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalendarEventResourceIT {

    private static final String DEFAULT_CALENDAR_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_CALENDAR_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CALENDAR_LINK = "AAAAAAAAAA";
    private static final String UPDATED_CALENDAR_LINK = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/calendar-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Autowired
    private CalendarEventMapper calendarEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalendarEventMockMvc;

    private CalendarEvent calendarEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarEvent createEntity(EntityManager em) {
        CalendarEvent calendarEvent = new CalendarEvent()
            .calendarProvider(DEFAULT_CALENDAR_PROVIDER)
            .externalEventId(DEFAULT_EXTERNAL_EVENT_ID)
            .calendarLink(DEFAULT_CALENDAR_LINK)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return calendarEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarEvent createUpdatedEntity(EntityManager em) {
        CalendarEvent calendarEvent = new CalendarEvent()
            .calendarProvider(UPDATED_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID)
            .calendarLink(UPDATED_CALENDAR_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return calendarEvent;
    }

    @BeforeEach
    public void initTest() {
        calendarEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createCalendarEvent() throws Exception {
        int databaseSizeBeforeCreate = calendarEventRepository.findAll().size();
        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);
        restCalendarEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeCreate + 1);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getCalendarProvider()).isEqualTo(DEFAULT_CALENDAR_PROVIDER);
        assertThat(testCalendarEvent.getExternalEventId()).isEqualTo(DEFAULT_EXTERNAL_EVENT_ID);
        assertThat(testCalendarEvent.getCalendarLink()).isEqualTo(DEFAULT_CALENDAR_LINK);
        assertThat(testCalendarEvent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCalendarEvent.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createCalendarEventWithExistingId() throws Exception {
        // Create the CalendarEvent with an existing ID
        calendarEvent.setId(1L);
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        int databaseSizeBeforeCreate = calendarEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCalendarProviderIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setCalendarProvider(null);

        // Create the CalendarEvent, which fails.
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        restCalendarEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCalendarLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setCalendarLink(null);

        // Create the CalendarEvent, which fails.
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        restCalendarEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setCreatedAt(null);

        // Create the CalendarEvent, which fails.
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        restCalendarEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setUpdatedAt(null);

        // Create the CalendarEvent, which fails.
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        restCalendarEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalendarEvents() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].calendarProvider").value(hasItem(DEFAULT_CALENDAR_PROVIDER)))
            .andExpect(jsonPath("$.[*].externalEventId").value(hasItem(DEFAULT_EXTERNAL_EVENT_ID)))
            .andExpect(jsonPath("$.[*].calendarLink").value(hasItem(DEFAULT_CALENDAR_LINK)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get the calendarEvent
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL_ID, calendarEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendarEvent.getId().intValue()))
            .andExpect(jsonPath("$.calendarProvider").value(DEFAULT_CALENDAR_PROVIDER))
            .andExpect(jsonPath("$.externalEventId").value(DEFAULT_EXTERNAL_EVENT_ID))
            .andExpect(jsonPath("$.calendarLink").value(DEFAULT_CALENDAR_LINK))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCalendarEventsByIdFiltering() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        Long id = calendarEvent.getId();

        defaultCalendarEventShouldBeFound("id.equals=" + id);
        defaultCalendarEventShouldNotBeFound("id.notEquals=" + id);

        defaultCalendarEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCalendarEventShouldNotBeFound("id.greaterThan=" + id);

        defaultCalendarEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCalendarEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarProvider equals to DEFAULT_CALENDAR_PROVIDER
        defaultCalendarEventShouldBeFound("calendarProvider.equals=" + DEFAULT_CALENDAR_PROVIDER);

        // Get all the calendarEventList where calendarProvider equals to UPDATED_CALENDAR_PROVIDER
        defaultCalendarEventShouldNotBeFound("calendarProvider.equals=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarProviderIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarProvider in DEFAULT_CALENDAR_PROVIDER or UPDATED_CALENDAR_PROVIDER
        defaultCalendarEventShouldBeFound("calendarProvider.in=" + DEFAULT_CALENDAR_PROVIDER + "," + UPDATED_CALENDAR_PROVIDER);

        // Get all the calendarEventList where calendarProvider equals to UPDATED_CALENDAR_PROVIDER
        defaultCalendarEventShouldNotBeFound("calendarProvider.in=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarProvider is not null
        defaultCalendarEventShouldBeFound("calendarProvider.specified=true");

        // Get all the calendarEventList where calendarProvider is null
        defaultCalendarEventShouldNotBeFound("calendarProvider.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarProviderContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarProvider contains DEFAULT_CALENDAR_PROVIDER
        defaultCalendarEventShouldBeFound("calendarProvider.contains=" + DEFAULT_CALENDAR_PROVIDER);

        // Get all the calendarEventList where calendarProvider contains UPDATED_CALENDAR_PROVIDER
        defaultCalendarEventShouldNotBeFound("calendarProvider.contains=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarProviderNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarProvider does not contain DEFAULT_CALENDAR_PROVIDER
        defaultCalendarEventShouldNotBeFound("calendarProvider.doesNotContain=" + DEFAULT_CALENDAR_PROVIDER);

        // Get all the calendarEventList where calendarProvider does not contain UPDATED_CALENDAR_PROVIDER
        defaultCalendarEventShouldBeFound("calendarProvider.doesNotContain=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByExternalEventIdIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where externalEventId equals to DEFAULT_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldBeFound("externalEventId.equals=" + DEFAULT_EXTERNAL_EVENT_ID);

        // Get all the calendarEventList where externalEventId equals to UPDATED_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldNotBeFound("externalEventId.equals=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByExternalEventIdIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where externalEventId in DEFAULT_EXTERNAL_EVENT_ID or UPDATED_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldBeFound("externalEventId.in=" + DEFAULT_EXTERNAL_EVENT_ID + "," + UPDATED_EXTERNAL_EVENT_ID);

        // Get all the calendarEventList where externalEventId equals to UPDATED_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldNotBeFound("externalEventId.in=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByExternalEventIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where externalEventId is not null
        defaultCalendarEventShouldBeFound("externalEventId.specified=true");

        // Get all the calendarEventList where externalEventId is null
        defaultCalendarEventShouldNotBeFound("externalEventId.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarEventsByExternalEventIdContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where externalEventId contains DEFAULT_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldBeFound("externalEventId.contains=" + DEFAULT_EXTERNAL_EVENT_ID);

        // Get all the calendarEventList where externalEventId contains UPDATED_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldNotBeFound("externalEventId.contains=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByExternalEventIdNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where externalEventId does not contain DEFAULT_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldNotBeFound("externalEventId.doesNotContain=" + DEFAULT_EXTERNAL_EVENT_ID);

        // Get all the calendarEventList where externalEventId does not contain UPDATED_EXTERNAL_EVENT_ID
        defaultCalendarEventShouldBeFound("externalEventId.doesNotContain=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarLink equals to DEFAULT_CALENDAR_LINK
        defaultCalendarEventShouldBeFound("calendarLink.equals=" + DEFAULT_CALENDAR_LINK);

        // Get all the calendarEventList where calendarLink equals to UPDATED_CALENDAR_LINK
        defaultCalendarEventShouldNotBeFound("calendarLink.equals=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarLinkIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarLink in DEFAULT_CALENDAR_LINK or UPDATED_CALENDAR_LINK
        defaultCalendarEventShouldBeFound("calendarLink.in=" + DEFAULT_CALENDAR_LINK + "," + UPDATED_CALENDAR_LINK);

        // Get all the calendarEventList where calendarLink equals to UPDATED_CALENDAR_LINK
        defaultCalendarEventShouldNotBeFound("calendarLink.in=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarLink is not null
        defaultCalendarEventShouldBeFound("calendarLink.specified=true");

        // Get all the calendarEventList where calendarLink is null
        defaultCalendarEventShouldNotBeFound("calendarLink.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarLinkContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarLink contains DEFAULT_CALENDAR_LINK
        defaultCalendarEventShouldBeFound("calendarLink.contains=" + DEFAULT_CALENDAR_LINK);

        // Get all the calendarEventList where calendarLink contains UPDATED_CALENDAR_LINK
        defaultCalendarEventShouldNotBeFound("calendarLink.contains=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCalendarLinkNotContainsSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where calendarLink does not contain DEFAULT_CALENDAR_LINK
        defaultCalendarEventShouldNotBeFound("calendarLink.doesNotContain=" + DEFAULT_CALENDAR_LINK);

        // Get all the calendarEventList where calendarLink does not contain UPDATED_CALENDAR_LINK
        defaultCalendarEventShouldBeFound("calendarLink.doesNotContain=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where createdAt equals to DEFAULT_CREATED_AT
        defaultCalendarEventShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the calendarEventList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarEventShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCalendarEventShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the calendarEventList where createdAt equals to UPDATED_CREATED_AT
        defaultCalendarEventShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where createdAt is not null
        defaultCalendarEventShouldBeFound("createdAt.specified=true");

        // Get all the calendarEventList where createdAt is null
        defaultCalendarEventShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarEventsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCalendarEventShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the calendarEventList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarEventShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCalendarEventShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the calendarEventList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCalendarEventShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCalendarEventsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList where updatedAt is not null
        defaultCalendarEventShouldBeFound("updatedAt.specified=true");

        // Get all the calendarEventList where updatedAt is null
        defaultCalendarEventShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarEventsByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            calendarEventRepository.saveAndFlush(calendarEvent);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        calendarEvent.setEvent(event);
        calendarEventRepository.saveAndFlush(calendarEvent);
        Long eventId = event.getId();
        // Get all the calendarEventList where event equals to eventId
        defaultCalendarEventShouldBeFound("eventId.equals=" + eventId);

        // Get all the calendarEventList where event equals to (eventId + 1)
        defaultCalendarEventShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllCalendarEventsByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            calendarEventRepository.saveAndFlush(calendarEvent);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        calendarEvent.setCreatedBy(createdBy);
        calendarEventRepository.saveAndFlush(calendarEvent);
        Long createdById = createdBy.getId();
        // Get all the calendarEventList where createdBy equals to createdById
        defaultCalendarEventShouldBeFound("createdById.equals=" + createdById);

        // Get all the calendarEventList where createdBy equals to (createdById + 1)
        defaultCalendarEventShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCalendarEventShouldBeFound(String filter) throws Exception {
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].calendarProvider").value(hasItem(DEFAULT_CALENDAR_PROVIDER)))
            .andExpect(jsonPath("$.[*].externalEventId").value(hasItem(DEFAULT_EXTERNAL_EVENT_ID)))
            .andExpect(jsonPath("$.[*].calendarLink").value(hasItem(DEFAULT_CALENDAR_LINK)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCalendarEventShouldNotBeFound(String filter) throws Exception {
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCalendarEvent() throws Exception {
        // Get the calendarEvent
        restCalendarEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent
        CalendarEvent updatedCalendarEvent = calendarEventRepository.findById(calendarEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCalendarEvent are not directly saved in db
        em.detach(updatedCalendarEvent);
        updatedCalendarEvent
            .calendarProvider(UPDATED_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID)
            .calendarLink(UPDATED_CALENDAR_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(updatedCalendarEvent);

        restCalendarEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendarEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getCalendarProvider()).isEqualTo(UPDATED_CALENDAR_PROVIDER);
        assertThat(testCalendarEvent.getExternalEventId()).isEqualTo(UPDATED_EXTERNAL_EVENT_ID);
        assertThat(testCalendarEvent.getCalendarLink()).isEqualTo(UPDATED_CALENDAR_LINK);
        assertThat(testCalendarEvent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCalendarEvent.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(longCount.incrementAndGet());

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendarEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(longCount.incrementAndGet());

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(longCount.incrementAndGet());

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalendarEventWithPatch() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent using partial update
        CalendarEvent partialUpdatedCalendarEvent = new CalendarEvent();
        partialUpdatedCalendarEvent.setId(calendarEvent.getId());

        partialUpdatedCalendarEvent.updatedAt(UPDATED_UPDATED_AT);

        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendarEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendarEvent))
            )
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getCalendarProvider()).isEqualTo(DEFAULT_CALENDAR_PROVIDER);
        assertThat(testCalendarEvent.getExternalEventId()).isEqualTo(DEFAULT_EXTERNAL_EVENT_ID);
        assertThat(testCalendarEvent.getCalendarLink()).isEqualTo(DEFAULT_CALENDAR_LINK);
        assertThat(testCalendarEvent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCalendarEvent.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateCalendarEventWithPatch() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent using partial update
        CalendarEvent partialUpdatedCalendarEvent = new CalendarEvent();
        partialUpdatedCalendarEvent.setId(calendarEvent.getId());

        partialUpdatedCalendarEvent
            .calendarProvider(UPDATED_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID)
            .calendarLink(UPDATED_CALENDAR_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendarEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendarEvent))
            )
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getCalendarProvider()).isEqualTo(UPDATED_CALENDAR_PROVIDER);
        assertThat(testCalendarEvent.getExternalEventId()).isEqualTo(UPDATED_EXTERNAL_EVENT_ID);
        assertThat(testCalendarEvent.getCalendarLink()).isEqualTo(UPDATED_CALENDAR_LINK);
        assertThat(testCalendarEvent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCalendarEvent.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(longCount.incrementAndGet());

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calendarEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(longCount.incrementAndGet());

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(longCount.incrementAndGet());

        // Create the CalendarEvent
        CalendarEventDTO calendarEventDTO = calendarEventMapper.toDto(calendarEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendarEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeDelete = calendarEventRepository.findAll().size();

        // Delete the calendarEvent
        restCalendarEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, calendarEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
