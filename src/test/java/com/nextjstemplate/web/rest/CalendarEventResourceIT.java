package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.CalendarEvent;
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

        partialUpdatedCalendarEvent.calendarProvider(UPDATED_CALENDAR_PROVIDER).externalEventId(UPDATED_EXTERNAL_EVENT_ID);

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
        assertThat(testCalendarEvent.getCalendarLink()).isEqualTo(DEFAULT_CALENDAR_LINK);
        assertThat(testCalendarEvent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCalendarEvent.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
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
