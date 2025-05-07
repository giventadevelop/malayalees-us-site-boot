package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.Media;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.MediaRepository;
import com.nextjstemplate.service.dto.MediaDTO;
import com.nextjstemplate.service.mapper.MediaMapper;
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
 * Integration tests for the {@link MediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MediaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_TYPE = "BBBBBBBBBB";

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
    private static final Integer SMALLER_FILE_SIZE = 1 - 1;

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/media";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMediaMockMvc;

    private Media media;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createEntity(EntityManager em) {
        Media media = new Media()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .mediaType(DEFAULT_MEDIA_TYPE)
            .storageType(DEFAULT_STORAGE_TYPE)
            .fileUrl(DEFAULT_FILE_URL)
            .fileData(DEFAULT_FILE_DATA)
            .fileDataContentType(DEFAULT_FILE_DATA_CONTENT_TYPE)
            .contentType(DEFAULT_CONTENT_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .isPublic(DEFAULT_IS_PUBLIC)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return media;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createUpdatedEntity(EntityManager em) {
        Media media = new Media()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .mediaType(UPDATED_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return media;
    }

    @BeforeEach
    public void initTest() {
        media = createEntity(em);
    }

    @Test
    @Transactional
    void createMedia() throws Exception {
        int databaseSizeBeforeCreate = mediaRepository.findAll().size();
        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);
        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isCreated());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate + 1);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMedia.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMedia.getMediaType()).isEqualTo(DEFAULT_MEDIA_TYPE);
        assertThat(testMedia.getStorageType()).isEqualTo(DEFAULT_STORAGE_TYPE);
        assertThat(testMedia.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testMedia.getFileData()).isEqualTo(DEFAULT_FILE_DATA);
        assertThat(testMedia.getFileDataContentType()).isEqualTo(DEFAULT_FILE_DATA_CONTENT_TYPE);
        assertThat(testMedia.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testMedia.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testMedia.getIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
        assertThat(testMedia.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMedia.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createMediaWithExistingId() throws Exception {
        // Create the Media with an existing ID
        media.setId(1L);
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        int databaseSizeBeforeCreate = mediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = mediaRepository.findAll().size();
        // set the field null
        media.setTitle(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMediaTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mediaRepository.findAll().size();
        // set the field null
        media.setMediaType(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStorageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mediaRepository.findAll().size();
        // set the field null
        media.setStorageType(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = mediaRepository.findAll().size();
        // set the field null
        media.setCreatedAt(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = mediaRepository.findAll().size();
        // set the field null
        media.setUpdatedAt(null);

        // Create the Media, which fails.
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mediaType").value(hasItem(DEFAULT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].storageType").value(hasItem(DEFAULT_STORAGE_TYPE)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].fileDataContentType").value(hasItem(DEFAULT_FILE_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileData").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_DATA))))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get the media
        restMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, media.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(media.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.mediaType").value(DEFAULT_MEDIA_TYPE))
            .andExpect(jsonPath("$.storageType").value(DEFAULT_STORAGE_TYPE))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.fileDataContentType").value(DEFAULT_FILE_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileData").value(Base64Utils.encodeToString(DEFAULT_FILE_DATA)))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getMediaByIdFiltering() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        Long id = media.getId();

        defaultMediaShouldBeFound("id.equals=" + id);
        defaultMediaShouldNotBeFound("id.notEquals=" + id);

        defaultMediaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMediaShouldNotBeFound("id.greaterThan=" + id);

        defaultMediaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMediaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMediaByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where title equals to DEFAULT_TITLE
        defaultMediaShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the mediaList where title equals to UPDATED_TITLE
        defaultMediaShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMediaByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMediaShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the mediaList where title equals to UPDATED_TITLE
        defaultMediaShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMediaByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where title is not null
        defaultMediaShouldBeFound("title.specified=true");

        // Get all the mediaList where title is null
        defaultMediaShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByTitleContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where title contains DEFAULT_TITLE
        defaultMediaShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the mediaList where title contains UPDATED_TITLE
        defaultMediaShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMediaByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where title does not contain DEFAULT_TITLE
        defaultMediaShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the mediaList where title does not contain UPDATED_TITLE
        defaultMediaShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMediaByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where description equals to DEFAULT_DESCRIPTION
        defaultMediaShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the mediaList where description equals to UPDATED_DESCRIPTION
        defaultMediaShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMediaByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMediaShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the mediaList where description equals to UPDATED_DESCRIPTION
        defaultMediaShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMediaByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where description is not null
        defaultMediaShouldBeFound("description.specified=true");

        // Get all the mediaList where description is null
        defaultMediaShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where description contains DEFAULT_DESCRIPTION
        defaultMediaShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the mediaList where description contains UPDATED_DESCRIPTION
        defaultMediaShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMediaByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where description does not contain DEFAULT_DESCRIPTION
        defaultMediaShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the mediaList where description does not contain UPDATED_DESCRIPTION
        defaultMediaShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMediaByMediaTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where mediaType equals to DEFAULT_MEDIA_TYPE
        defaultMediaShouldBeFound("mediaType.equals=" + DEFAULT_MEDIA_TYPE);

        // Get all the mediaList where mediaType equals to UPDATED_MEDIA_TYPE
        defaultMediaShouldNotBeFound("mediaType.equals=" + UPDATED_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByMediaTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where mediaType in DEFAULT_MEDIA_TYPE or UPDATED_MEDIA_TYPE
        defaultMediaShouldBeFound("mediaType.in=" + DEFAULT_MEDIA_TYPE + "," + UPDATED_MEDIA_TYPE);

        // Get all the mediaList where mediaType equals to UPDATED_MEDIA_TYPE
        defaultMediaShouldNotBeFound("mediaType.in=" + UPDATED_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByMediaTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where mediaType is not null
        defaultMediaShouldBeFound("mediaType.specified=true");

        // Get all the mediaList where mediaType is null
        defaultMediaShouldNotBeFound("mediaType.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByMediaTypeContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where mediaType contains DEFAULT_MEDIA_TYPE
        defaultMediaShouldBeFound("mediaType.contains=" + DEFAULT_MEDIA_TYPE);

        // Get all the mediaList where mediaType contains UPDATED_MEDIA_TYPE
        defaultMediaShouldNotBeFound("mediaType.contains=" + UPDATED_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByMediaTypeNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where mediaType does not contain DEFAULT_MEDIA_TYPE
        defaultMediaShouldNotBeFound("mediaType.doesNotContain=" + DEFAULT_MEDIA_TYPE);

        // Get all the mediaList where mediaType does not contain UPDATED_MEDIA_TYPE
        defaultMediaShouldBeFound("mediaType.doesNotContain=" + UPDATED_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByStorageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where storageType equals to DEFAULT_STORAGE_TYPE
        defaultMediaShouldBeFound("storageType.equals=" + DEFAULT_STORAGE_TYPE);

        // Get all the mediaList where storageType equals to UPDATED_STORAGE_TYPE
        defaultMediaShouldNotBeFound("storageType.equals=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByStorageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where storageType in DEFAULT_STORAGE_TYPE or UPDATED_STORAGE_TYPE
        defaultMediaShouldBeFound("storageType.in=" + DEFAULT_STORAGE_TYPE + "," + UPDATED_STORAGE_TYPE);

        // Get all the mediaList where storageType equals to UPDATED_STORAGE_TYPE
        defaultMediaShouldNotBeFound("storageType.in=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByStorageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where storageType is not null
        defaultMediaShouldBeFound("storageType.specified=true");

        // Get all the mediaList where storageType is null
        defaultMediaShouldNotBeFound("storageType.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByStorageTypeContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where storageType contains DEFAULT_STORAGE_TYPE
        defaultMediaShouldBeFound("storageType.contains=" + DEFAULT_STORAGE_TYPE);

        // Get all the mediaList where storageType contains UPDATED_STORAGE_TYPE
        defaultMediaShouldNotBeFound("storageType.contains=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByStorageTypeNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where storageType does not contain DEFAULT_STORAGE_TYPE
        defaultMediaShouldNotBeFound("storageType.doesNotContain=" + DEFAULT_STORAGE_TYPE);

        // Get all the mediaList where storageType does not contain UPDATED_STORAGE_TYPE
        defaultMediaShouldBeFound("storageType.doesNotContain=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileUrl equals to DEFAULT_FILE_URL
        defaultMediaShouldBeFound("fileUrl.equals=" + DEFAULT_FILE_URL);

        // Get all the mediaList where fileUrl equals to UPDATED_FILE_URL
        defaultMediaShouldNotBeFound("fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMediaByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileUrl in DEFAULT_FILE_URL or UPDATED_FILE_URL
        defaultMediaShouldBeFound("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL);

        // Get all the mediaList where fileUrl equals to UPDATED_FILE_URL
        defaultMediaShouldNotBeFound("fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMediaByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileUrl is not null
        defaultMediaShouldBeFound("fileUrl.specified=true");

        // Get all the mediaList where fileUrl is null
        defaultMediaShouldNotBeFound("fileUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileUrl contains DEFAULT_FILE_URL
        defaultMediaShouldBeFound("fileUrl.contains=" + DEFAULT_FILE_URL);

        // Get all the mediaList where fileUrl contains UPDATED_FILE_URL
        defaultMediaShouldNotBeFound("fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMediaByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileUrl does not contain DEFAULT_FILE_URL
        defaultMediaShouldNotBeFound("fileUrl.doesNotContain=" + DEFAULT_FILE_URL);

        // Get all the mediaList where fileUrl does not contain UPDATED_FILE_URL
        defaultMediaShouldBeFound("fileUrl.doesNotContain=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllMediaByContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where contentType equals to DEFAULT_CONTENT_TYPE
        defaultMediaShouldBeFound("contentType.equals=" + DEFAULT_CONTENT_TYPE);

        // Get all the mediaList where contentType equals to UPDATED_CONTENT_TYPE
        defaultMediaShouldNotBeFound("contentType.equals=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where contentType in DEFAULT_CONTENT_TYPE or UPDATED_CONTENT_TYPE
        defaultMediaShouldBeFound("contentType.in=" + DEFAULT_CONTENT_TYPE + "," + UPDATED_CONTENT_TYPE);

        // Get all the mediaList where contentType equals to UPDATED_CONTENT_TYPE
        defaultMediaShouldNotBeFound("contentType.in=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where contentType is not null
        defaultMediaShouldBeFound("contentType.specified=true");

        // Get all the mediaList where contentType is null
        defaultMediaShouldNotBeFound("contentType.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByContentTypeContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where contentType contains DEFAULT_CONTENT_TYPE
        defaultMediaShouldBeFound("contentType.contains=" + DEFAULT_CONTENT_TYPE);

        // Get all the mediaList where contentType contains UPDATED_CONTENT_TYPE
        defaultMediaShouldNotBeFound("contentType.contains=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where contentType does not contain DEFAULT_CONTENT_TYPE
        defaultMediaShouldNotBeFound("contentType.doesNotContain=" + DEFAULT_CONTENT_TYPE);

        // Get all the mediaList where contentType does not contain UPDATED_CONTENT_TYPE
        defaultMediaShouldBeFound("contentType.doesNotContain=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileSize equals to DEFAULT_FILE_SIZE
        defaultMediaShouldBeFound("fileSize.equals=" + DEFAULT_FILE_SIZE);

        // Get all the mediaList where fileSize equals to UPDATED_FILE_SIZE
        defaultMediaShouldNotBeFound("fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMediaByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileSize in DEFAULT_FILE_SIZE or UPDATED_FILE_SIZE
        defaultMediaShouldBeFound("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE);

        // Get all the mediaList where fileSize equals to UPDATED_FILE_SIZE
        defaultMediaShouldNotBeFound("fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMediaByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileSize is not null
        defaultMediaShouldBeFound("fileSize.specified=true");

        // Get all the mediaList where fileSize is null
        defaultMediaShouldNotBeFound("fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileSize is greater than or equal to DEFAULT_FILE_SIZE
        defaultMediaShouldBeFound("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the mediaList where fileSize is greater than or equal to UPDATED_FILE_SIZE
        defaultMediaShouldNotBeFound("fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMediaByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileSize is less than or equal to DEFAULT_FILE_SIZE
        defaultMediaShouldBeFound("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the mediaList where fileSize is less than or equal to SMALLER_FILE_SIZE
        defaultMediaShouldNotBeFound("fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMediaByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileSize is less than DEFAULT_FILE_SIZE
        defaultMediaShouldNotBeFound("fileSize.lessThan=" + DEFAULT_FILE_SIZE);

        // Get all the mediaList where fileSize is less than UPDATED_FILE_SIZE
        defaultMediaShouldBeFound("fileSize.lessThan=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMediaByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileSize is greater than DEFAULT_FILE_SIZE
        defaultMediaShouldNotBeFound("fileSize.greaterThan=" + DEFAULT_FILE_SIZE);

        // Get all the mediaList where fileSize is greater than SMALLER_FILE_SIZE
        defaultMediaShouldBeFound("fileSize.greaterThan=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMediaByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where isPublic equals to DEFAULT_IS_PUBLIC
        defaultMediaShouldBeFound("isPublic.equals=" + DEFAULT_IS_PUBLIC);

        // Get all the mediaList where isPublic equals to UPDATED_IS_PUBLIC
        defaultMediaShouldNotBeFound("isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllMediaByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where isPublic in DEFAULT_IS_PUBLIC or UPDATED_IS_PUBLIC
        defaultMediaShouldBeFound("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC);

        // Get all the mediaList where isPublic equals to UPDATED_IS_PUBLIC
        defaultMediaShouldNotBeFound("isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllMediaByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where isPublic is not null
        defaultMediaShouldBeFound("isPublic.specified=true");

        // Get all the mediaList where isPublic is null
        defaultMediaShouldNotBeFound("isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where createdAt equals to DEFAULT_CREATED_AT
        defaultMediaShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the mediaList where createdAt equals to UPDATED_CREATED_AT
        defaultMediaShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMediaByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultMediaShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the mediaList where createdAt equals to UPDATED_CREATED_AT
        defaultMediaShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMediaByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where createdAt is not null
        defaultMediaShouldBeFound("createdAt.specified=true");

        // Get all the mediaList where createdAt is null
        defaultMediaShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultMediaShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the mediaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMediaShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMediaByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultMediaShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the mediaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMediaShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMediaByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where updatedAt is not null
        defaultMediaShouldBeFound("updatedAt.specified=true");

        // Get all the mediaList where updatedAt is null
        defaultMediaShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            mediaRepository.saveAndFlush(media);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        media.setEvent(event);
        mediaRepository.saveAndFlush(media);
        Long eventId = event.getId();
        // Get all the mediaList where event equals to eventId
        defaultMediaShouldBeFound("eventId.equals=" + eventId);

        // Get all the mediaList where event equals to (eventId + 1)
        defaultMediaShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllMediaByUploadedByIsEqualToSomething() throws Exception {
        UserProfile uploadedBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            mediaRepository.saveAndFlush(media);
            uploadedBy = UserProfileResourceIT.createEntity(em);
        } else {
            uploadedBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(uploadedBy);
        em.flush();
        media.setUploadedBy(uploadedBy);
        mediaRepository.saveAndFlush(media);
        Long uploadedById = uploadedBy.getId();
        // Get all the mediaList where uploadedBy equals to uploadedById
        defaultMediaShouldBeFound("uploadedById.equals=" + uploadedById);

        // Get all the mediaList where uploadedBy equals to (uploadedById + 1)
        defaultMediaShouldNotBeFound("uploadedById.equals=" + (uploadedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMediaShouldBeFound(String filter) throws Exception {
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mediaType").value(hasItem(DEFAULT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].storageType").value(hasItem(DEFAULT_STORAGE_TYPE)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].fileDataContentType").value(hasItem(DEFAULT_FILE_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileData").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_DATA))))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMediaShouldNotBeFound(String filter) throws Exception {
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedia() throws Exception {
        // Get the media
        restMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media
        Media updatedMedia = mediaRepository.findById(media.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedia are not directly saved in db
        em.detach(updatedMedia);
        updatedMedia
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .mediaType(UPDATED_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        MediaDTO mediaDTO = mediaMapper.toDto(updatedMedia);

        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMedia.getMediaType()).isEqualTo(UPDATED_MEDIA_TYPE);
        assertThat(testMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testMedia.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testMedia.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testMedia.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testMedia.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media using partial update
        Media partialUpdatedMedia = new Media();
        partialUpdatedMedia.setId(media.getId());

        partialUpdatedMedia
            .description(UPDATED_DESCRIPTION)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .updatedAt(UPDATED_UPDATED_AT);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedia))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMedia.getMediaType()).isEqualTo(DEFAULT_MEDIA_TYPE);
        assertThat(testMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testMedia.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testMedia.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testMedia.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testMedia.getIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
        assertThat(testMedia.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media using partial update
        Media partialUpdatedMedia = new Media();
        partialUpdatedMedia.setId(media.getId());

        partialUpdatedMedia
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .mediaType(UPDATED_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedia))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMedia.getMediaType()).isEqualTo(UPDATED_MEDIA_TYPE);
        assertThat(testMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testMedia.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testMedia.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testMedia.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testMedia.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeDelete = mediaRepository.findAll().size();

        // Delete the media
        restMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, media.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
