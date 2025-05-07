package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventType;
import com.nextjstemplate.repository.EventTypeRepository;
import com.nextjstemplate.service.dto.EventTypeDTO;
import com.nextjstemplate.service.mapper.EventTypeMapper;
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
 * Integration tests for the {@link EventTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/event-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventTypeMapper eventTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventTypeMockMvc;

    private EventType eventType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createEntity(EntityManager em) {
        EventType eventType = new EventType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createUpdatedEntity(EntityManager em) {
        EventType eventType = new EventType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventType;
    }

    @BeforeEach
    public void initTest() {
        eventType = createEntity(em);
    }

    @Test
    @Transactional
    void createEventType() throws Exception {
        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();
        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);
        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventTypeWithExistingId() throws Exception {
        // Create the EventType with an existing ID
        eventType.setId(1L);
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeRepository.findAll().size();
        // set the field null
        eventType.setName(null);

        // Create the EventType, which fails.
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeRepository.findAll().size();
        // set the field null
        eventType.setCreatedAt(null);

        // Create the EventType, which fails.
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeRepository.findAll().size();
        // set the field null
        eventType.setUpdatedAt(null);

        // Create the EventType, which fails.
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        restEventTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventTypes() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get the eventType
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, eventType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getEventTypesByIdFiltering() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        Long id = eventType.getId();

        defaultEventTypeShouldBeFound("id.equals=" + id);
        defaultEventTypeShouldNotBeFound("id.notEquals=" + id);

        defaultEventTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultEventTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where name equals to DEFAULT_NAME
        defaultEventTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventTypeList where name equals to UPDATED_NAME
        defaultEventTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventTypeList where name equals to UPDATED_NAME
        defaultEventTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where name is not null
        defaultEventTypeShouldBeFound("name.specified=true");

        // Get all the eventTypeList where name is null
        defaultEventTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where name contains DEFAULT_NAME
        defaultEventTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventTypeList where name contains UPDATED_NAME
        defaultEventTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where name does not contain DEFAULT_NAME
        defaultEventTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventTypeList where name does not contain UPDATED_NAME
        defaultEventTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description equals to DEFAULT_DESCRIPTION
        defaultEventTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeList where description equals to UPDATED_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventTypeList where description equals to UPDATED_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description is not null
        defaultEventTypeShouldBeFound("description.specified=true");

        // Get all the eventTypeList where description is null
        defaultEventTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description contains DEFAULT_DESCRIPTION
        defaultEventTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeList where description contains UPDATED_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultEventTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeList where description does not contain UPDATED_DESCRIPTION
        defaultEventTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventTypeShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventTypeList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTypeShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventTypeShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventTypeList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTypeShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where createdAt is not null
        defaultEventTypeShouldBeFound("createdAt.specified=true");

        // Get all the eventTypeList where createdAt is null
        defaultEventTypeShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventTypeShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventTypeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTypeShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventTypeShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventTypeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTypeShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList where updatedAt is not null
        defaultEventTypeShouldBeFound("updatedAt.specified=true");

        // Get all the eventTypeList where updatedAt is null
        defaultEventTypeShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventTypeShouldBeFound(String filter) throws Exception {
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventTypeShouldNotBeFound(String filter) throws Exception {
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventType() throws Exception {
        // Get the eventType
        restEventTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType
        EventType updatedEventType = eventTypeRepository.findById(eventType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventType are not directly saved in db
        em.detach(updatedEventType);
        updatedEventType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(updatedEventType);

        restEventTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(longCount.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(longCount.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(longCount.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventTypeWithPatch() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType using partial update
        EventType partialUpdatedEventType = new EventType();
        partialUpdatedEventType.setId(eventType.getId());

        partialUpdatedEventType.description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT);

        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventType))
            )
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventTypeWithPatch() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType using partial update
        EventType partialUpdatedEventType = new EventType();
        partialUpdatedEventType.setId(eventType.getId());

        partialUpdatedEventType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventType))
            )
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(longCount.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(longCount.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();
        eventType.setId(longCount.incrementAndGet());

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeDelete = eventTypeRepository.findAll().size();

        // Delete the eventType
        restEventTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
