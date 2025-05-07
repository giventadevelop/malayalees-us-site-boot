package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.AdminAuditLog;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.AdminAuditLogRepository;
import com.nextjstemplate.service.dto.AdminAuditLogDTO;
import com.nextjstemplate.service.mapper.AdminAuditLogMapper;
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
 * Integration tests for the {@link AdminAuditLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminAuditLogResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGES = "AAAAAAAAAA";
    private static final String UPDATED_CHANGES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/admin-audit-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminAuditLogRepository adminAuditLogRepository;

    @Autowired
    private AdminAuditLogMapper adminAuditLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminAuditLogMockMvc;

    private AdminAuditLog adminAuditLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminAuditLog createEntity(EntityManager em) {
        AdminAuditLog adminAuditLog = new AdminAuditLog()
            .action(DEFAULT_ACTION)
            .tableName(DEFAULT_TABLE_NAME)
            .recordId(DEFAULT_RECORD_ID)
            .changes(DEFAULT_CHANGES)
            .createdAt(DEFAULT_CREATED_AT);
        return adminAuditLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminAuditLog createUpdatedEntity(EntityManager em) {
        AdminAuditLog adminAuditLog = new AdminAuditLog()
            .action(UPDATED_ACTION)
            .tableName(UPDATED_TABLE_NAME)
            .recordId(UPDATED_RECORD_ID)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);
        return adminAuditLog;
    }

    @BeforeEach
    public void initTest() {
        adminAuditLog = createEntity(em);
    }

    @Test
    @Transactional
    void createAdminAuditLog() throws Exception {
        int databaseSizeBeforeCreate = adminAuditLogRepository.findAll().size();
        // Create the AdminAuditLog
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);
        restAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeCreate + 1);
        AdminAuditLog testAdminAuditLog = adminAuditLogList.get(adminAuditLogList.size() - 1);
        assertThat(testAdminAuditLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAdminAuditLog.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testAdminAuditLog.getRecordId()).isEqualTo(DEFAULT_RECORD_ID);
        assertThat(testAdminAuditLog.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testAdminAuditLog.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createAdminAuditLogWithExistingId() throws Exception {
        // Create the AdminAuditLog with an existing ID
        adminAuditLog.setId(1L);
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        int databaseSizeBeforeCreate = adminAuditLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAuditLogRepository.findAll().size();
        // set the field null
        adminAuditLog.setAction(null);

        // Create the AdminAuditLog, which fails.
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        restAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTableNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAuditLogRepository.findAll().size();
        // set the field null
        adminAuditLog.setTableName(null);

        // Create the AdminAuditLog, which fails.
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        restAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAuditLogRepository.findAll().size();
        // set the field null
        adminAuditLog.setRecordId(null);

        // Create the AdminAuditLog, which fails.
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        restAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAuditLogRepository.findAll().size();
        // set the field null
        adminAuditLog.setCreatedAt(null);

        // Create the AdminAuditLog, which fails.
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        restAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogs() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList
        restAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminAuditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].recordId").value(hasItem(DEFAULT_RECORD_ID)))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAdminAuditLog() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get the adminAuditLog
        restAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL_ID, adminAuditLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminAuditLog.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.recordId").value(DEFAULT_RECORD_ID))
            .andExpect(jsonPath("$.changes").value(DEFAULT_CHANGES.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getAdminAuditLogsByIdFiltering() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        Long id = adminAuditLog.getId();

        defaultAdminAuditLogShouldBeFound("id.equals=" + id);
        defaultAdminAuditLogShouldNotBeFound("id.notEquals=" + id);

        defaultAdminAuditLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdminAuditLogShouldNotBeFound("id.greaterThan=" + id);

        defaultAdminAuditLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdminAuditLogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where action equals to DEFAULT_ACTION
        defaultAdminAuditLogShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the adminAuditLogList where action equals to UPDATED_ACTION
        defaultAdminAuditLogShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultAdminAuditLogShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the adminAuditLogList where action equals to UPDATED_ACTION
        defaultAdminAuditLogShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where action is not null
        defaultAdminAuditLogShouldBeFound("action.specified=true");

        // Get all the adminAuditLogList where action is null
        defaultAdminAuditLogShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByActionContainsSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where action contains DEFAULT_ACTION
        defaultAdminAuditLogShouldBeFound("action.contains=" + DEFAULT_ACTION);

        // Get all the adminAuditLogList where action contains UPDATED_ACTION
        defaultAdminAuditLogShouldNotBeFound("action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByActionNotContainsSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where action does not contain DEFAULT_ACTION
        defaultAdminAuditLogShouldNotBeFound("action.doesNotContain=" + DEFAULT_ACTION);

        // Get all the adminAuditLogList where action does not contain UPDATED_ACTION
        defaultAdminAuditLogShouldBeFound("action.doesNotContain=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByTableNameIsEqualToSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where tableName equals to DEFAULT_TABLE_NAME
        defaultAdminAuditLogShouldBeFound("tableName.equals=" + DEFAULT_TABLE_NAME);

        // Get all the adminAuditLogList where tableName equals to UPDATED_TABLE_NAME
        defaultAdminAuditLogShouldNotBeFound("tableName.equals=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByTableNameIsInShouldWork() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where tableName in DEFAULT_TABLE_NAME or UPDATED_TABLE_NAME
        defaultAdminAuditLogShouldBeFound("tableName.in=" + DEFAULT_TABLE_NAME + "," + UPDATED_TABLE_NAME);

        // Get all the adminAuditLogList where tableName equals to UPDATED_TABLE_NAME
        defaultAdminAuditLogShouldNotBeFound("tableName.in=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByTableNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where tableName is not null
        defaultAdminAuditLogShouldBeFound("tableName.specified=true");

        // Get all the adminAuditLogList where tableName is null
        defaultAdminAuditLogShouldNotBeFound("tableName.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByTableNameContainsSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where tableName contains DEFAULT_TABLE_NAME
        defaultAdminAuditLogShouldBeFound("tableName.contains=" + DEFAULT_TABLE_NAME);

        // Get all the adminAuditLogList where tableName contains UPDATED_TABLE_NAME
        defaultAdminAuditLogShouldNotBeFound("tableName.contains=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByTableNameNotContainsSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where tableName does not contain DEFAULT_TABLE_NAME
        defaultAdminAuditLogShouldNotBeFound("tableName.doesNotContain=" + DEFAULT_TABLE_NAME);

        // Get all the adminAuditLogList where tableName does not contain UPDATED_TABLE_NAME
        defaultAdminAuditLogShouldBeFound("tableName.doesNotContain=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByRecordIdIsEqualToSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where recordId equals to DEFAULT_RECORD_ID
        defaultAdminAuditLogShouldBeFound("recordId.equals=" + DEFAULT_RECORD_ID);

        // Get all the adminAuditLogList where recordId equals to UPDATED_RECORD_ID
        defaultAdminAuditLogShouldNotBeFound("recordId.equals=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByRecordIdIsInShouldWork() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where recordId in DEFAULT_RECORD_ID or UPDATED_RECORD_ID
        defaultAdminAuditLogShouldBeFound("recordId.in=" + DEFAULT_RECORD_ID + "," + UPDATED_RECORD_ID);

        // Get all the adminAuditLogList where recordId equals to UPDATED_RECORD_ID
        defaultAdminAuditLogShouldNotBeFound("recordId.in=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByRecordIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where recordId is not null
        defaultAdminAuditLogShouldBeFound("recordId.specified=true");

        // Get all the adminAuditLogList where recordId is null
        defaultAdminAuditLogShouldNotBeFound("recordId.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByRecordIdContainsSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where recordId contains DEFAULT_RECORD_ID
        defaultAdminAuditLogShouldBeFound("recordId.contains=" + DEFAULT_RECORD_ID);

        // Get all the adminAuditLogList where recordId contains UPDATED_RECORD_ID
        defaultAdminAuditLogShouldNotBeFound("recordId.contains=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByRecordIdNotContainsSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where recordId does not contain DEFAULT_RECORD_ID
        defaultAdminAuditLogShouldNotBeFound("recordId.doesNotContain=" + DEFAULT_RECORD_ID);

        // Get all the adminAuditLogList where recordId does not contain UPDATED_RECORD_ID
        defaultAdminAuditLogShouldBeFound("recordId.doesNotContain=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where createdAt equals to DEFAULT_CREATED_AT
        defaultAdminAuditLogShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the adminAuditLogList where createdAt equals to UPDATED_CREATED_AT
        defaultAdminAuditLogShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAdminAuditLogShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the adminAuditLogList where createdAt equals to UPDATED_CREATED_AT
        defaultAdminAuditLogShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        // Get all the adminAuditLogList where createdAt is not null
        defaultAdminAuditLogShouldBeFound("createdAt.specified=true");

        // Get all the adminAuditLogList where createdAt is null
        defaultAdminAuditLogShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminAuditLogsByAdminIsEqualToSomething() throws Exception {
        UserProfile admin;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            adminAuditLogRepository.saveAndFlush(adminAuditLog);
            admin = UserProfileResourceIT.createEntity(em);
        } else {
            admin = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(admin);
        em.flush();
        adminAuditLog.setAdmin(admin);
        adminAuditLogRepository.saveAndFlush(adminAuditLog);
        Long adminId = admin.getId();
        // Get all the adminAuditLogList where admin equals to adminId
        defaultAdminAuditLogShouldBeFound("adminId.equals=" + adminId);

        // Get all the adminAuditLogList where admin equals to (adminId + 1)
        defaultAdminAuditLogShouldNotBeFound("adminId.equals=" + (adminId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdminAuditLogShouldBeFound(String filter) throws Exception {
        restAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminAuditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].recordId").value(hasItem(DEFAULT_RECORD_ID)))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdminAuditLogShouldNotBeFound(String filter) throws Exception {
        restAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdminAuditLog() throws Exception {
        // Get the adminAuditLog
        restAdminAuditLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdminAuditLog() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();

        // Update the adminAuditLog
        AdminAuditLog updatedAdminAuditLog = adminAuditLogRepository.findById(adminAuditLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdminAuditLog are not directly saved in db
        em.detach(updatedAdminAuditLog);
        updatedAdminAuditLog
            .action(UPDATED_ACTION)
            .tableName(UPDATED_TABLE_NAME)
            .recordId(UPDATED_RECORD_ID)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(updatedAdminAuditLog);

        restAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminAuditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
        AdminAuditLog testAdminAuditLog = adminAuditLogList.get(adminAuditLogList.size() - 1);
        assertThat(testAdminAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAdminAuditLog.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testAdminAuditLog.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
        assertThat(testAdminAuditLog.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testAdminAuditLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();
        adminAuditLog.setId(longCount.incrementAndGet());

        // Create the AdminAuditLog
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminAuditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();
        adminAuditLog.setId(longCount.incrementAndGet());

        // Create the AdminAuditLog
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();
        adminAuditLog.setId(longCount.incrementAndGet());

        // Create the AdminAuditLog
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminAuditLogWithPatch() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();

        // Update the adminAuditLog using partial update
        AdminAuditLog partialUpdatedAdminAuditLog = new AdminAuditLog();
        partialUpdatedAdminAuditLog.setId(adminAuditLog.getId());

        partialUpdatedAdminAuditLog
            .action(UPDATED_ACTION)
            .tableName(UPDATED_TABLE_NAME)
            .recordId(UPDATED_RECORD_ID)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);

        restAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
        AdminAuditLog testAdminAuditLog = adminAuditLogList.get(adminAuditLogList.size() - 1);
        assertThat(testAdminAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAdminAuditLog.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testAdminAuditLog.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
        assertThat(testAdminAuditLog.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testAdminAuditLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAdminAuditLogWithPatch() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();

        // Update the adminAuditLog using partial update
        AdminAuditLog partialUpdatedAdminAuditLog = new AdminAuditLog();
        partialUpdatedAdminAuditLog.setId(adminAuditLog.getId());

        partialUpdatedAdminAuditLog
            .action(UPDATED_ACTION)
            .tableName(UPDATED_TABLE_NAME)
            .recordId(UPDATED_RECORD_ID)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);

        restAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
        AdminAuditLog testAdminAuditLog = adminAuditLogList.get(adminAuditLogList.size() - 1);
        assertThat(testAdminAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAdminAuditLog.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testAdminAuditLog.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
        assertThat(testAdminAuditLog.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testAdminAuditLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();
        adminAuditLog.setId(longCount.incrementAndGet());

        // Create the AdminAuditLog
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminAuditLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();
        adminAuditLog.setId(longCount.incrementAndGet());

        // Create the AdminAuditLog
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = adminAuditLogRepository.findAll().size();
        adminAuditLog.setId(longCount.incrementAndGet());

        // Create the AdminAuditLog
        AdminAuditLogDTO adminAuditLogDTO = adminAuditLogMapper.toDto(adminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminAuditLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminAuditLog in the database
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminAuditLog() throws Exception {
        // Initialize the database
        adminAuditLogRepository.saveAndFlush(adminAuditLog);

        int databaseSizeBeforeDelete = adminAuditLogRepository.findAll().size();

        // Delete the adminAuditLog
        restAdminAuditLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminAuditLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdminAuditLog> adminAuditLogList = adminAuditLogRepository.findAll();
        assertThat(adminAuditLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
