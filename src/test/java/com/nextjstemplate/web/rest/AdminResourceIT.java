package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.Admin;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.AdminRepository;
import com.nextjstemplate.service.dto.AdminDTO;
import com.nextjstemplate.service.mapper.AdminMapper;
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
 * Integration tests for the {@link AdminResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminResourceIT {

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/admins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminMockMvc;

    private Admin admin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Admin createEntity(EntityManager em) {
        Admin admin = new Admin().role(DEFAULT_ROLE).createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
        return admin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Admin createUpdatedEntity(EntityManager em) {
        Admin admin = new Admin().role(UPDATED_ROLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        return admin;
    }

    @BeforeEach
    public void initTest() {
        admin = createEntity(em);
    }

    @Test
    @Transactional
    void createAdmin() throws Exception {
        int databaseSizeBeforeCreate = adminRepository.findAll().size();
        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);
        restAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isCreated());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeCreate + 1);
        Admin testAdmin = adminList.get(adminList.size() - 1);
        assertThat(testAdmin.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testAdmin.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAdmin.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAdminWithExistingId() throws Exception {
        // Create the Admin with an existing ID
        admin.setId(1L);
        AdminDTO adminDTO = adminMapper.toDto(admin);

        int databaseSizeBeforeCreate = adminRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminRepository.findAll().size();
        // set the field null
        admin.setRole(null);

        // Create the Admin, which fails.
        AdminDTO adminDTO = adminMapper.toDto(admin);

        restAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isBadRequest());

        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminRepository.findAll().size();
        // set the field null
        admin.setCreatedAt(null);

        // Create the Admin, which fails.
        AdminDTO adminDTO = adminMapper.toDto(admin);

        restAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isBadRequest());

        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminRepository.findAll().size();
        // set the field null
        admin.setUpdatedAt(null);

        // Create the Admin, which fails.
        AdminDTO adminDTO = adminMapper.toDto(admin);

        restAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isBadRequest());

        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdmins() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList
        restAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(admin.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get the admin
        restAdminMockMvc
            .perform(get(ENTITY_API_URL_ID, admin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(admin.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getAdminsByIdFiltering() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        Long id = admin.getId();

        defaultAdminShouldBeFound("id.equals=" + id);
        defaultAdminShouldNotBeFound("id.notEquals=" + id);

        defaultAdminShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdminShouldNotBeFound("id.greaterThan=" + id);

        defaultAdminShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdminShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdminsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where role equals to DEFAULT_ROLE
        defaultAdminShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the adminList where role equals to UPDATED_ROLE
        defaultAdminShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllAdminsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultAdminShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the adminList where role equals to UPDATED_ROLE
        defaultAdminShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllAdminsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where role is not null
        defaultAdminShouldBeFound("role.specified=true");

        // Get all the adminList where role is null
        defaultAdminShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminsByRoleContainsSomething() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where role contains DEFAULT_ROLE
        defaultAdminShouldBeFound("role.contains=" + DEFAULT_ROLE);

        // Get all the adminList where role contains UPDATED_ROLE
        defaultAdminShouldNotBeFound("role.contains=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllAdminsByRoleNotContainsSomething() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where role does not contain DEFAULT_ROLE
        defaultAdminShouldNotBeFound("role.doesNotContain=" + DEFAULT_ROLE);

        // Get all the adminList where role does not contain UPDATED_ROLE
        defaultAdminShouldBeFound("role.doesNotContain=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllAdminsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where createdAt equals to DEFAULT_CREATED_AT
        defaultAdminShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the adminList where createdAt equals to UPDATED_CREATED_AT
        defaultAdminShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAdminsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAdminShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the adminList where createdAt equals to UPDATED_CREATED_AT
        defaultAdminShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAdminsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where createdAt is not null
        defaultAdminShouldBeFound("createdAt.specified=true");

        // Get all the adminList where createdAt is null
        defaultAdminShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultAdminShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the adminList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAdminShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAdminsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultAdminShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the adminList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAdminShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAdminsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList where updatedAt is not null
        defaultAdminShouldBeFound("updatedAt.specified=true");

        // Get all the adminList where updatedAt is null
        defaultAdminShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminsByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            adminRepository.saveAndFlush(admin);
            user = UserProfileResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        admin.setUser(user);
        adminRepository.saveAndFlush(admin);
        Long userId = user.getId();
        // Get all the adminList where user equals to userId
        defaultAdminShouldBeFound("userId.equals=" + userId);

        // Get all the adminList where user equals to (userId + 1)
        defaultAdminShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllAdminsByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            adminRepository.saveAndFlush(admin);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        admin.setCreatedBy(createdBy);
        adminRepository.saveAndFlush(admin);
        Long createdById = createdBy.getId();
        // Get all the adminList where createdBy equals to createdById
        defaultAdminShouldBeFound("createdById.equals=" + createdById);

        // Get all the adminList where createdBy equals to (createdById + 1)
        defaultAdminShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdminShouldBeFound(String filter) throws Exception {
        restAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(admin.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restAdminMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdminShouldNotBeFound(String filter) throws Exception {
        restAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdminMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdmin() throws Exception {
        // Get the admin
        restAdminMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        int databaseSizeBeforeUpdate = adminRepository.findAll().size();

        // Update the admin
        Admin updatedAdmin = adminRepository.findById(admin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdmin are not directly saved in db
        em.detach(updatedAdmin);
        updatedAdmin.role(UPDATED_ROLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        AdminDTO adminDTO = adminMapper.toDto(updatedAdmin);

        restAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminDTO))
            )
            .andExpect(status().isOk());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
        Admin testAdmin = adminList.get(adminList.size() - 1);
        assertThat(testAdmin.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testAdmin.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAdmin.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAdmin() throws Exception {
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();
        admin.setId(longCount.incrementAndGet());

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdmin() throws Exception {
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();
        admin.setId(longCount.incrementAndGet());

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdmin() throws Exception {
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();
        admin.setId(longCount.incrementAndGet());

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminWithPatch() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        int databaseSizeBeforeUpdate = adminRepository.findAll().size();

        // Update the admin using partial update
        Admin partialUpdatedAdmin = new Admin();
        partialUpdatedAdmin.setId(admin.getId());

        partialUpdatedAdmin.role(UPDATED_ROLE).createdAt(UPDATED_CREATED_AT);

        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdmin))
            )
            .andExpect(status().isOk());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
        Admin testAdmin = adminList.get(adminList.size() - 1);
        assertThat(testAdmin.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testAdmin.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAdmin.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAdminWithPatch() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        int databaseSizeBeforeUpdate = adminRepository.findAll().size();

        // Update the admin using partial update
        Admin partialUpdatedAdmin = new Admin();
        partialUpdatedAdmin.setId(admin.getId());

        partialUpdatedAdmin.role(UPDATED_ROLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdmin))
            )
            .andExpect(status().isOk());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
        Admin testAdmin = adminList.get(adminList.size() - 1);
        assertThat(testAdmin.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testAdmin.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAdmin.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAdmin() throws Exception {
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();
        admin.setId(longCount.incrementAndGet());

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdmin() throws Exception {
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();
        admin.setId(longCount.incrementAndGet());

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdmin() throws Exception {
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();
        admin.setId(longCount.incrementAndGet());

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        int databaseSizeBeforeDelete = adminRepository.findAll().size();

        // Delete the admin
        restAdminMockMvc
            .perform(delete(ENTITY_API_URL_ID, admin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
