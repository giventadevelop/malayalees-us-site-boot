package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.EventType;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventRepository;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.mapper.EventMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CAPTION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTIONS_TO_VENUE = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTIONS_TO_VENUE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;
    private static final Integer SMALLER_CAPACITY = 1 - 1;

    private static final String DEFAULT_ADMISSION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ADMISSION_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .title(DEFAULT_TITLE)
            .caption(DEFAULT_CAPTION)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .location(DEFAULT_LOCATION)
            .directionsToVenue(DEFAULT_DIRECTIONS_TO_VENUE)
            .capacity(DEFAULT_CAPACITY)
            .admissionType(DEFAULT_ADMISSION_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return event;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .location(UPDATED_LOCATION)
            .directionsToVenue(UPDATED_DIRECTIONS_TO_VENUE)
            .capacity(UPDATED_CAPACITY)
            .admissionType(UPDATED_ADMISSION_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();
        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvent.getCaption()).isEqualTo(DEFAULT_CAPTION);
        assertThat(testEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEvent.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testEvent.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testEvent.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEvent.getDirectionsToVenue()).isEqualTo(DEFAULT_DIRECTIONS_TO_VENUE);
        assertThat(testEvent.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testEvent.getAdmissionType()).isEqualTo(DEFAULT_ADMISSION_TYPE);
        assertThat(testEvent.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEvent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEvent.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setTitle(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setStartDate(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEndDate(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setStartTime(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEndTime(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdmissionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setAdmissionType(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setCreatedAt(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setUpdatedAt(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].directionsToVenue").value(hasItem(DEFAULT_DIRECTIONS_TO_VENUE)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].admissionType").value(hasItem(DEFAULT_ADMISSION_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.directionsToVenue").value(DEFAULT_DIRECTIONS_TO_VENUE))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.admissionType").value(DEFAULT_ADMISSION_TYPE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventShouldBeFound("id.equals=" + id);
        defaultEventShouldNotBeFound("id.notEquals=" + id);

        defaultEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.greaterThan=" + id);

        defaultEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title equals to DEFAULT_TITLE
        defaultEventShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the eventList where title equals to UPDATED_TITLE
        defaultEventShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEventShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the eventList where title equals to UPDATED_TITLE
        defaultEventShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title is not null
        defaultEventShouldBeFound("title.specified=true");

        // Get all the eventList where title is null
        defaultEventShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByTitleContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title contains DEFAULT_TITLE
        defaultEventShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the eventList where title contains UPDATED_TITLE
        defaultEventShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where title does not contain DEFAULT_TITLE
        defaultEventShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the eventList where title does not contain UPDATED_TITLE
        defaultEventShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventsByCaptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where caption equals to DEFAULT_CAPTION
        defaultEventShouldBeFound("caption.equals=" + DEFAULT_CAPTION);

        // Get all the eventList where caption equals to UPDATED_CAPTION
        defaultEventShouldNotBeFound("caption.equals=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventsByCaptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where caption in DEFAULT_CAPTION or UPDATED_CAPTION
        defaultEventShouldBeFound("caption.in=" + DEFAULT_CAPTION + "," + UPDATED_CAPTION);

        // Get all the eventList where caption equals to UPDATED_CAPTION
        defaultEventShouldNotBeFound("caption.in=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventsByCaptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where caption is not null
        defaultEventShouldBeFound("caption.specified=true");

        // Get all the eventList where caption is null
        defaultEventShouldNotBeFound("caption.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByCaptionContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where caption contains DEFAULT_CAPTION
        defaultEventShouldBeFound("caption.contains=" + DEFAULT_CAPTION);

        // Get all the eventList where caption contains UPDATED_CAPTION
        defaultEventShouldNotBeFound("caption.contains=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventsByCaptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where caption does not contain DEFAULT_CAPTION
        defaultEventShouldNotBeFound("caption.doesNotContain=" + DEFAULT_CAPTION);

        // Get all the eventList where caption does not contain UPDATED_CAPTION
        defaultEventShouldBeFound("caption.doesNotContain=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where description equals to DEFAULT_DESCRIPTION
        defaultEventShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventList where description equals to UPDATED_DESCRIPTION
        defaultEventShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventList where description equals to UPDATED_DESCRIPTION
        defaultEventShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where description is not null
        defaultEventShouldBeFound("description.specified=true");

        // Get all the eventList where description is null
        defaultEventShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where description contains DEFAULT_DESCRIPTION
        defaultEventShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventList where description contains UPDATED_DESCRIPTION
        defaultEventShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where description does not contain DEFAULT_DESCRIPTION
        defaultEventShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventList where description does not contain UPDATED_DESCRIPTION
        defaultEventShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startDate equals to DEFAULT_START_DATE
        defaultEventShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the eventList where startDate equals to UPDATED_START_DATE
        defaultEventShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultEventShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the eventList where startDate equals to UPDATED_START_DATE
        defaultEventShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startDate is not null
        defaultEventShouldBeFound("startDate.specified=true");

        // Get all the eventList where startDate is null
        defaultEventShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultEventShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the eventList where startDate is greater than or equal to UPDATED_START_DATE
        defaultEventShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startDate is less than or equal to DEFAULT_START_DATE
        defaultEventShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the eventList where startDate is less than or equal to SMALLER_START_DATE
        defaultEventShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startDate is less than DEFAULT_START_DATE
        defaultEventShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the eventList where startDate is less than UPDATED_START_DATE
        defaultEventShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startDate is greater than DEFAULT_START_DATE
        defaultEventShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the eventList where startDate is greater than SMALLER_START_DATE
        defaultEventShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate equals to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate equals to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the eventList where endDate equals to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is not null
        defaultEventShouldBeFound("endDate.specified=true");

        // Get all the eventList where endDate is null
        defaultEventShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is greater than or equal to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is less than or equal to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is less than or equal to SMALLER_END_DATE
        defaultEventShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is less than DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is less than UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is greater than DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is greater than SMALLER_END_DATE
        defaultEventShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startTime equals to DEFAULT_START_TIME
        defaultEventShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the eventList where startTime equals to UPDATED_START_TIME
        defaultEventShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultEventShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the eventList where startTime equals to UPDATED_START_TIME
        defaultEventShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startTime is not null
        defaultEventShouldBeFound("startTime.specified=true");

        // Get all the eventList where startTime is null
        defaultEventShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByStartTimeContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startTime contains DEFAULT_START_TIME
        defaultEventShouldBeFound("startTime.contains=" + DEFAULT_START_TIME);

        // Get all the eventList where startTime contains UPDATED_START_TIME
        defaultEventShouldNotBeFound("startTime.contains=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByStartTimeNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startTime does not contain DEFAULT_START_TIME
        defaultEventShouldNotBeFound("startTime.doesNotContain=" + DEFAULT_START_TIME);

        // Get all the eventList where startTime does not contain UPDATED_START_TIME
        defaultEventShouldBeFound("startTime.doesNotContain=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endTime equals to DEFAULT_END_TIME
        defaultEventShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the eventList where endTime equals to UPDATED_END_TIME
        defaultEventShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultEventShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the eventList where endTime equals to UPDATED_END_TIME
        defaultEventShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endTime is not null
        defaultEventShouldBeFound("endTime.specified=true");

        // Get all the eventList where endTime is null
        defaultEventShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEndTimeContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endTime contains DEFAULT_END_TIME
        defaultEventShouldBeFound("endTime.contains=" + DEFAULT_END_TIME);

        // Get all the eventList where endTime contains UPDATED_END_TIME
        defaultEventShouldNotBeFound("endTime.contains=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByEndTimeNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endTime does not contain DEFAULT_END_TIME
        defaultEventShouldNotBeFound("endTime.doesNotContain=" + DEFAULT_END_TIME);

        // Get all the eventList where endTime does not contain UPDATED_END_TIME
        defaultEventShouldBeFound("endTime.doesNotContain=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where location equals to DEFAULT_LOCATION
        defaultEventShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the eventList where location equals to UPDATED_LOCATION
        defaultEventShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultEventShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the eventList where location equals to UPDATED_LOCATION
        defaultEventShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where location is not null
        defaultEventShouldBeFound("location.specified=true");

        // Get all the eventList where location is null
        defaultEventShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByLocationContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where location contains DEFAULT_LOCATION
        defaultEventShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the eventList where location contains UPDATED_LOCATION
        defaultEventShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where location does not contain DEFAULT_LOCATION
        defaultEventShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the eventList where location does not contain UPDATED_LOCATION
        defaultEventShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionsToVenueIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where directionsToVenue equals to DEFAULT_DIRECTIONS_TO_VENUE
        defaultEventShouldBeFound("directionsToVenue.equals=" + DEFAULT_DIRECTIONS_TO_VENUE);

        // Get all the eventList where directionsToVenue equals to UPDATED_DIRECTIONS_TO_VENUE
        defaultEventShouldNotBeFound("directionsToVenue.equals=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionsToVenueIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where directionsToVenue in DEFAULT_DIRECTIONS_TO_VENUE or UPDATED_DIRECTIONS_TO_VENUE
        defaultEventShouldBeFound("directionsToVenue.in=" + DEFAULT_DIRECTIONS_TO_VENUE + "," + UPDATED_DIRECTIONS_TO_VENUE);

        // Get all the eventList where directionsToVenue equals to UPDATED_DIRECTIONS_TO_VENUE
        defaultEventShouldNotBeFound("directionsToVenue.in=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionsToVenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where directionsToVenue is not null
        defaultEventShouldBeFound("directionsToVenue.specified=true");

        // Get all the eventList where directionsToVenue is null
        defaultEventShouldNotBeFound("directionsToVenue.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDirectionsToVenueContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where directionsToVenue contains DEFAULT_DIRECTIONS_TO_VENUE
        defaultEventShouldBeFound("directionsToVenue.contains=" + DEFAULT_DIRECTIONS_TO_VENUE);

        // Get all the eventList where directionsToVenue contains UPDATED_DIRECTIONS_TO_VENUE
        defaultEventShouldNotBeFound("directionsToVenue.contains=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionsToVenueNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where directionsToVenue does not contain DEFAULT_DIRECTIONS_TO_VENUE
        defaultEventShouldNotBeFound("directionsToVenue.doesNotContain=" + DEFAULT_DIRECTIONS_TO_VENUE);

        // Get all the eventList where directionsToVenue does not contain UPDATED_DIRECTIONS_TO_VENUE
        defaultEventShouldBeFound("directionsToVenue.doesNotContain=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventsByCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where capacity equals to DEFAULT_CAPACITY
        defaultEventShouldBeFound("capacity.equals=" + DEFAULT_CAPACITY);

        // Get all the eventList where capacity equals to UPDATED_CAPACITY
        defaultEventShouldNotBeFound("capacity.equals=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventsByCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where capacity in DEFAULT_CAPACITY or UPDATED_CAPACITY
        defaultEventShouldBeFound("capacity.in=" + DEFAULT_CAPACITY + "," + UPDATED_CAPACITY);

        // Get all the eventList where capacity equals to UPDATED_CAPACITY
        defaultEventShouldNotBeFound("capacity.in=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventsByCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where capacity is not null
        defaultEventShouldBeFound("capacity.specified=true");

        // Get all the eventList where capacity is null
        defaultEventShouldNotBeFound("capacity.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where capacity is greater than or equal to DEFAULT_CAPACITY
        defaultEventShouldBeFound("capacity.greaterThanOrEqual=" + DEFAULT_CAPACITY);

        // Get all the eventList where capacity is greater than or equal to UPDATED_CAPACITY
        defaultEventShouldNotBeFound("capacity.greaterThanOrEqual=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventsByCapacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where capacity is less than or equal to DEFAULT_CAPACITY
        defaultEventShouldBeFound("capacity.lessThanOrEqual=" + DEFAULT_CAPACITY);

        // Get all the eventList where capacity is less than or equal to SMALLER_CAPACITY
        defaultEventShouldNotBeFound("capacity.lessThanOrEqual=" + SMALLER_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventsByCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where capacity is less than DEFAULT_CAPACITY
        defaultEventShouldNotBeFound("capacity.lessThan=" + DEFAULT_CAPACITY);

        // Get all the eventList where capacity is less than UPDATED_CAPACITY
        defaultEventShouldBeFound("capacity.lessThan=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventsByCapacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where capacity is greater than DEFAULT_CAPACITY
        defaultEventShouldNotBeFound("capacity.greaterThan=" + DEFAULT_CAPACITY);

        // Get all the eventList where capacity is greater than SMALLER_CAPACITY
        defaultEventShouldBeFound("capacity.greaterThan=" + SMALLER_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventsByAdmissionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where admissionType equals to DEFAULT_ADMISSION_TYPE
        defaultEventShouldBeFound("admissionType.equals=" + DEFAULT_ADMISSION_TYPE);

        // Get all the eventList where admissionType equals to UPDATED_ADMISSION_TYPE
        defaultEventShouldNotBeFound("admissionType.equals=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByAdmissionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where admissionType in DEFAULT_ADMISSION_TYPE or UPDATED_ADMISSION_TYPE
        defaultEventShouldBeFound("admissionType.in=" + DEFAULT_ADMISSION_TYPE + "," + UPDATED_ADMISSION_TYPE);

        // Get all the eventList where admissionType equals to UPDATED_ADMISSION_TYPE
        defaultEventShouldNotBeFound("admissionType.in=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByAdmissionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where admissionType is not null
        defaultEventShouldBeFound("admissionType.specified=true");

        // Get all the eventList where admissionType is null
        defaultEventShouldNotBeFound("admissionType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByAdmissionTypeContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where admissionType contains DEFAULT_ADMISSION_TYPE
        defaultEventShouldBeFound("admissionType.contains=" + DEFAULT_ADMISSION_TYPE);

        // Get all the eventList where admissionType contains UPDATED_ADMISSION_TYPE
        defaultEventShouldNotBeFound("admissionType.contains=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByAdmissionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where admissionType does not contain DEFAULT_ADMISSION_TYPE
        defaultEventShouldNotBeFound("admissionType.doesNotContain=" + DEFAULT_ADMISSION_TYPE);

        // Get all the eventList where admissionType does not contain UPDATED_ADMISSION_TYPE
        defaultEventShouldBeFound("admissionType.doesNotContain=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEventShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the eventList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEventShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the eventList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where isActive is not null
        defaultEventShouldBeFound("isActive.specified=true");

        // Get all the eventList where isActive is null
        defaultEventShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventList where createdAt equals to UPDATED_CREATED_AT
        defaultEventShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventList where createdAt equals to UPDATED_CREATED_AT
        defaultEventShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createdAt is not null
        defaultEventShouldBeFound("createdAt.specified=true");

        // Get all the eventList where createdAt is null
        defaultEventShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedAt is not null
        defaultEventShouldBeFound("updatedAt.specified=true");

        // Get all the eventList where updatedAt is null
        defaultEventShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventRepository.saveAndFlush(event);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        event.setCreatedBy(createdBy);
        eventRepository.saveAndFlush(event);
        Long createdById = createdBy.getId();
        // Get all the eventList where createdBy equals to createdById
        defaultEventShouldBeFound("createdById.equals=" + createdById);

        // Get all the eventList where createdBy equals to (createdById + 1)
        defaultEventShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsEqualToSomething() throws Exception {
        EventType eventType;
        if (TestUtil.findAll(em, EventType.class).isEmpty()) {
            eventRepository.saveAndFlush(event);
            eventType = EventTypeResourceIT.createEntity(em);
        } else {
            eventType = TestUtil.findAll(em, EventType.class).get(0);
        }
        em.persist(eventType);
        em.flush();
        event.setEventType(eventType);
        eventRepository.saveAndFlush(event);
        Long eventTypeId = eventType.getId();
        // Get all the eventList where eventType equals to eventTypeId
        defaultEventShouldBeFound("eventTypeId.equals=" + eventTypeId);

        // Get all the eventList where eventType equals to (eventTypeId + 1)
        defaultEventShouldNotBeFound("eventTypeId.equals=" + (eventTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].directionsToVenue").value(hasItem(DEFAULT_DIRECTIONS_TO_VENUE)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].admissionType").value(hasItem(DEFAULT_ADMISSION_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .location(UPDATED_LOCATION)
            .directionsToVenue(UPDATED_DIRECTIONS_TO_VENUE)
            .capacity(UPDATED_CAPACITY)
            .admissionType(UPDATED_ADMISSION_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEvent.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEvent.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEvent.getDirectionsToVenue()).isEqualTo(UPDATED_DIRECTIONS_TO_VENUE);
        assertThat(testEvent.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testEvent.getAdmissionType()).isEqualTo(UPDATED_ADMISSION_TYPE);
        assertThat(testEvent.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEvent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEvent.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .directionsToVenue(UPDATED_DIRECTIONS_TO_VENUE)
            .capacity(UPDATED_CAPACITY)
            .admissionType(UPDATED_ADMISSION_TYPE)
            .createdAt(UPDATED_CREATED_AT);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEvent.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEvent.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEvent.getDirectionsToVenue()).isEqualTo(UPDATED_DIRECTIONS_TO_VENUE);
        assertThat(testEvent.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testEvent.getAdmissionType()).isEqualTo(UPDATED_ADMISSION_TYPE);
        assertThat(testEvent.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEvent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEvent.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .location(UPDATED_LOCATION)
            .directionsToVenue(UPDATED_DIRECTIONS_TO_VENUE)
            .capacity(UPDATED_CAPACITY)
            .admissionType(UPDATED_ADMISSION_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEvent.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEvent.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEvent.getDirectionsToVenue()).isEqualTo(UPDATED_DIRECTIONS_TO_VENUE);
        assertThat(testEvent.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testEvent.getAdmissionType()).isEqualTo(UPDATED_ADMISSION_TYPE);
        assertThat(testEvent.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEvent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEvent.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(longCount.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
