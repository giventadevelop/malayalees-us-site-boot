package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.service.mapper.EventMediaMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link EventMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventMediaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_MEDIA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_MEDIA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STORAGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_DATA_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final Boolean DEFAULT_EVENT_FLYER = false;
    private static final Boolean UPDATED_EVENT_FLYER = true;

    private static final Boolean DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT = false;
    private static final Boolean UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/event-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventMediaRepository eventMediaRepository;

    @Autowired
    private EventMediaMapper eventMediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMediaMockMvc;

    private EventMedia eventMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventMedia createEntity(EntityManager em) {
        EventMedia eventMedia = new EventMedia()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .eventMediaType(DEFAULT_EVENT_MEDIA_TYPE)
            .storageType(DEFAULT_STORAGE_TYPE)
            .fileUrl(DEFAULT_FILE_URL)
            .fileData(DEFAULT_FILE_DATA)
            .fileDataContentType(DEFAULT_FILE_DATA_CONTENT_TYPE)
            .contentType(DEFAULT_CONTENT_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .isPublic(DEFAULT_IS_PUBLIC)
            .eventFlyer(DEFAULT_EVENT_FLYER)
            .isEventManagementOfficialDocument(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventMedia createUpdatedEntity(EntityManager em) {
        EventMedia eventMedia = new EventMedia()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .eventMediaType(UPDATED_EVENT_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .eventFlyer(UPDATED_EVENT_FLYER)
            .isEventManagementOfficialDocument(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventMedia;
    }

    @BeforeEach
    public void initTest() {
        eventMedia = createEntity(em);
    }

    @Test
    @Transactional
    void createEventMedia() throws Exception {
        int databaseSizeBeforeCreate = eventMediaRepository.findAll().size();
        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);
        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isCreated());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeCreate + 1);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(DEFAULT_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(DEFAULT_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(DEFAULT_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(DEFAULT_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(DEFAULT_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventMediaWithExistingId() throws Exception {
        // Create the EventMedia with an existing ID
        eventMedia.setId(1L);
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        int databaseSizeBeforeCreate = eventMediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setTitle(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventMediaTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setEventMediaType(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStorageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setStorageType(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setCreatedAt(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setUpdatedAt(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventMedias() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventMediaType").value(hasItem(DEFAULT_EVENT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].storageType").value(hasItem(DEFAULT_STORAGE_TYPE)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].fileDataContentType").value(hasItem(DEFAULT_FILE_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileData").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_DATA))))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].eventFlyer").value(hasItem(DEFAULT_EVENT_FLYER.booleanValue())))
            .andExpect(
                jsonPath("$.[*].isEventManagementOfficialDocument")
                    .value(hasItem(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getEventMedia() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get the eventMedia
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, eventMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventMedia.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.eventMediaType").value(DEFAULT_EVENT_MEDIA_TYPE))
            .andExpect(jsonPath("$.storageType").value(DEFAULT_STORAGE_TYPE))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.fileDataContentType").value(DEFAULT_FILE_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileData").value(Base64Utils.encodeToString(DEFAULT_FILE_DATA)))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC.booleanValue()))
            .andExpect(jsonPath("$.eventFlyer").value(DEFAULT_EVENT_FLYER.booleanValue()))
            .andExpect(jsonPath("$.isEventManagementOfficialDocument").value(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEventMedia() throws Exception {
        // Get the eventMedia
        restEventMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventMedia() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();

        // Update the eventMedia
        EventMedia updatedEventMedia = eventMediaRepository.findById(eventMedia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventMedia are not directly saved in db
        em.detach(updatedEventMedia);
        updatedEventMedia
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .eventMediaType(UPDATED_EVENT_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .eventFlyer(UPDATED_EVENT_FLYER)
            .isEventManagementOfficialDocument(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(updatedEventMedia);

        restEventMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(UPDATED_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(UPDATED_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventMediaWithPatch() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();

        // Update the eventMedia using partial update
        EventMedia partialUpdatedEventMedia = new EventMedia();
        partialUpdatedEventMedia.setId(eventMedia.getId());

        partialUpdatedEventMedia
            .storageType(UPDATED_STORAGE_TYPE)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .contentType(UPDATED_CONTENT_TYPE)
            .eventFlyer(UPDATED_EVENT_FLYER);

        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventMedia))
            )
            .andExpect(status().isOk());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(DEFAULT_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(UPDATED_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventMediaWithPatch() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();

        // Update the eventMedia using partial update
        EventMedia partialUpdatedEventMedia = new EventMedia();
        partialUpdatedEventMedia.setId(eventMedia.getId());

        partialUpdatedEventMedia
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .eventMediaType(UPDATED_EVENT_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .eventFlyer(UPDATED_EVENT_FLYER)
            .isEventManagementOfficialDocument(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventMedia))
            )
            .andExpect(status().isOk());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(UPDATED_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(UPDATED_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventMedia() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeDelete = eventMediaRepository.findAll().size();

        // Delete the eventMedia
        restEventMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
