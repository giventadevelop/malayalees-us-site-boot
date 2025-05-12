package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.AdminAuditLog;
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
        assertThat(testAdminAuditLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAdminAuditLog.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testAdminAuditLog.getRecordId()).isEqualTo(DEFAULT_RECORD_ID);
        assertThat(testAdminAuditLog.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testAdminAuditLog.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
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
