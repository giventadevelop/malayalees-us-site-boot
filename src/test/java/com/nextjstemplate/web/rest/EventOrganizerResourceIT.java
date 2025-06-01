package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
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
 * Integration tests for the {@link EventOrganizerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventOrganizerResourceIT {/*

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PHONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIMARY = false;
    private static final Boolean UPDATED_IS_PRIMARY = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/event-organizers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventOrganizerRepository eventOrganizerRepository;

    @Autowired
    private EventOrganizerMapper eventOrganizerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventOrganizerMockMvc;

    private EventOrganizer eventOrganizer;

    *//**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventOrganizer createEntity(EntityManager em) {
        EventOrganizer eventOrganizer = new EventOrganizer()
            .title(DEFAULT_TITLE)
            .designation(DEFAULT_DESIGNATION)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .contactPhone(DEFAULT_CONTACT_PHONE)
            .isPrimary(DEFAULT_IS_PRIMARY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventOrganizer;
    }

    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventOrganizer createUpdatedEntity(EntityManager em) {
        EventOrganizer eventOrganizer = new EventOrganizer()
            .title(UPDATED_TITLE)
            .designation(UPDATED_DESIGNATION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventOrganizer;
    }

    @BeforeEach
    public void initTest() {
        eventOrganizer = createEntity(em);
    }

    @Test
    @Transactional
    void createEventOrganizer() throws Exception {
        int databaseSizeBeforeCreate = eventOrganizerRepository.findAll().size();
        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);
        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeCreate + 1);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(DEFAULT_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(DEFAULT_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventOrganizerWithExistingId() throws Exception {
        // Create the EventOrganizer with an existing ID
        eventOrganizer.setId(1L);
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        int databaseSizeBeforeCreate = eventOrganizerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOrganizerRepository.findAll().size();
        // set the field null
        eventOrganizer.setTitle(null);

        // Create the EventOrganizer, which fails.
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOrganizerRepository.findAll().size();
        // set the field null
        eventOrganizer.setCreatedAt(null);

        // Create the EventOrganizer, which fails.
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOrganizerRepository.findAll().size();
        // set the field null
        eventOrganizer.setUpdatedAt(null);

        // Create the EventOrganizer, which fails.
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventOrganizers() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventOrganizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getEventOrganizer() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get the eventOrganizer
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL_ID, eventOrganizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventOrganizer.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.contactPhone").value(DEFAULT_CONTACT_PHONE))
            .andExpect(jsonPath("$.isPrimary").value(DEFAULT_IS_PRIMARY.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEventOrganizer() throws Exception {
        // Get the eventOrganizer
        restEventOrganizerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventOrganizer() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();

        // Update the eventOrganizer
        EventOrganizer updatedEventOrganizer = eventOrganizerRepository.findById(eventOrganizer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventOrganizer are not directly saved in db
        em.detach(updatedEventOrganizer);
        updatedEventOrganizer
            .title(UPDATED_TITLE)
            .designation(UPDATED_DESIGNATION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(updatedEventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventOrganizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventOrganizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventOrganizerWithPatch() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();

        // Update the eventOrganizer using partial update
        EventOrganizer partialUpdatedEventOrganizer = new EventOrganizer();
        partialUpdatedEventOrganizer.setId(eventOrganizer.getId());

        partialUpdatedEventOrganizer
            .title(UPDATED_TITLE)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .createdAt(UPDATED_CREATED_AT);

        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventOrganizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventOrganizer))
            )
            .andExpect(status().isOk());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(DEFAULT_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventOrganizerWithPatch() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();

        // Update the eventOrganizer using partial update
        EventOrganizer partialUpdatedEventOrganizer = new EventOrganizer();
        partialUpdatedEventOrganizer.setId(eventOrganizer.getId());

        partialUpdatedEventOrganizer
            .title(UPDATED_TITLE)
            .designation(UPDATED_DESIGNATION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventOrganizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventOrganizer))
            )
            .andExpect(status().isOk());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventOrganizerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventOrganizer() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeDelete = eventOrganizerRepository.findAll().size();

        // Delete the eventOrganizer
        restEventOrganizerMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventOrganizer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
