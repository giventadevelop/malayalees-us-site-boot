package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.domain.PollOption;
import com.nextjstemplate.domain.PollResponse;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.PollResponseRepository;
import com.nextjstemplate.service.dto.PollResponseDTO;
import com.nextjstemplate.service.mapper.PollResponseMapper;
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
 * Integration tests for the {@link PollResponseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PollResponseResourceIT {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/poll-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PollResponseRepository pollResponseRepository;

    @Autowired
    private PollResponseMapper pollResponseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPollResponseMockMvc;

    private PollResponse pollResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollResponse createEntity(EntityManager em) {
        PollResponse pollResponse = new PollResponse().comment(DEFAULT_COMMENT).createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
        return pollResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollResponse createUpdatedEntity(EntityManager em) {
        PollResponse pollResponse = new PollResponse().comment(UPDATED_COMMENT).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        return pollResponse;
    }

    @BeforeEach
    public void initTest() {
        pollResponse = createEntity(em);
    }

    @Test
    @Transactional
    void createPollResponse() throws Exception {
        int databaseSizeBeforeCreate = pollResponseRepository.findAll().size();
        // Create the PollResponse
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);
        restPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeCreate + 1);
        PollResponse testPollResponse = pollResponseList.get(pollResponseList.size() - 1);
        assertThat(testPollResponse.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testPollResponse.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPollResponse.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createPollResponseWithExistingId() throws Exception {
        // Create the PollResponse with an existing ID
        pollResponse.setId(1L);
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        int databaseSizeBeforeCreate = pollResponseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollResponseRepository.findAll().size();
        // set the field null
        pollResponse.setCreatedAt(null);

        // Create the PollResponse, which fails.
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        restPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollResponseRepository.findAll().size();
        // set the field null
        pollResponse.setUpdatedAt(null);

        // Create the PollResponse, which fails.
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        restPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPollResponses() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList
        restPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getPollResponse() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get the pollResponse
        restPollResponseMockMvc
            .perform(get(ENTITY_API_URL_ID, pollResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pollResponse.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getPollResponsesByIdFiltering() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        Long id = pollResponse.getId();

        defaultPollResponseShouldBeFound("id.equals=" + id);
        defaultPollResponseShouldNotBeFound("id.notEquals=" + id);

        defaultPollResponseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPollResponseShouldNotBeFound("id.greaterThan=" + id);

        defaultPollResponseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPollResponseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPollResponsesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where comment equals to DEFAULT_COMMENT
        defaultPollResponseShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the pollResponseList where comment equals to UPDATED_COMMENT
        defaultPollResponseShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultPollResponseShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the pollResponseList where comment equals to UPDATED_COMMENT
        defaultPollResponseShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where comment is not null
        defaultPollResponseShouldBeFound("comment.specified=true");

        // Get all the pollResponseList where comment is null
        defaultPollResponseShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllPollResponsesByCommentContainsSomething() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where comment contains DEFAULT_COMMENT
        defaultPollResponseShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the pollResponseList where comment contains UPDATED_COMMENT
        defaultPollResponseShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where comment does not contain DEFAULT_COMMENT
        defaultPollResponseShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the pollResponseList where comment does not contain UPDATED_COMMENT
        defaultPollResponseShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where createdAt equals to DEFAULT_CREATED_AT
        defaultPollResponseShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the pollResponseList where createdAt equals to UPDATED_CREATED_AT
        defaultPollResponseShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPollResponseShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the pollResponseList where createdAt equals to UPDATED_CREATED_AT
        defaultPollResponseShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where createdAt is not null
        defaultPollResponseShouldBeFound("createdAt.specified=true");

        // Get all the pollResponseList where createdAt is null
        defaultPollResponseShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPollResponsesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPollResponseShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the pollResponseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPollResponseShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPollResponseShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the pollResponseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPollResponseShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPollResponsesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        // Get all the pollResponseList where updatedAt is not null
        defaultPollResponseShouldBeFound("updatedAt.specified=true");

        // Get all the pollResponseList where updatedAt is null
        defaultPollResponseShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPollResponsesByPollIsEqualToSomething() throws Exception {
        Poll poll;
        if (TestUtil.findAll(em, Poll.class).isEmpty()) {
            pollResponseRepository.saveAndFlush(pollResponse);
            poll = PollResourceIT.createEntity(em);
        } else {
            poll = TestUtil.findAll(em, Poll.class).get(0);
        }
        em.persist(poll);
        em.flush();
        pollResponse.setPoll(poll);
        pollResponseRepository.saveAndFlush(pollResponse);
        Long pollId = poll.getId();
        // Get all the pollResponseList where poll equals to pollId
        defaultPollResponseShouldBeFound("pollId.equals=" + pollId);

        // Get all the pollResponseList where poll equals to (pollId + 1)
        defaultPollResponseShouldNotBeFound("pollId.equals=" + (pollId + 1));
    }

    @Test
    @Transactional
    void getAllPollResponsesByPollOptionIsEqualToSomething() throws Exception {
        PollOption pollOption;
        if (TestUtil.findAll(em, PollOption.class).isEmpty()) {
            pollResponseRepository.saveAndFlush(pollResponse);
            pollOption = PollOptionResourceIT.createEntity(em);
        } else {
            pollOption = TestUtil.findAll(em, PollOption.class).get(0);
        }
        em.persist(pollOption);
        em.flush();
        pollResponse.setPollOption(pollOption);
        pollResponseRepository.saveAndFlush(pollResponse);
        Long pollOptionId = pollOption.getId();
        // Get all the pollResponseList where pollOption equals to pollOptionId
        defaultPollResponseShouldBeFound("pollOptionId.equals=" + pollOptionId);

        // Get all the pollResponseList where pollOption equals to (pollOptionId + 1)
        defaultPollResponseShouldNotBeFound("pollOptionId.equals=" + (pollOptionId + 1));
    }

    @Test
    @Transactional
    void getAllPollResponsesByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            pollResponseRepository.saveAndFlush(pollResponse);
            user = UserProfileResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        pollResponse.setUser(user);
        pollResponseRepository.saveAndFlush(pollResponse);
        Long userId = user.getId();
        // Get all the pollResponseList where user equals to userId
        defaultPollResponseShouldBeFound("userId.equals=" + userId);

        // Get all the pollResponseList where user equals to (userId + 1)
        defaultPollResponseShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPollResponseShouldBeFound(String filter) throws Exception {
        restPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPollResponseShouldNotBeFound(String filter) throws Exception {
        restPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPollResponse() throws Exception {
        // Get the pollResponse
        restPollResponseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPollResponse() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();

        // Update the pollResponse
        PollResponse updatedPollResponse = pollResponseRepository.findById(pollResponse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPollResponse are not directly saved in db
        em.detach(updatedPollResponse);
        updatedPollResponse.comment(UPDATED_COMMENT).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(updatedPollResponse);

        restPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollResponseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isOk());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
        PollResponse testPollResponse = pollResponseList.get(pollResponseList.size() - 1);
        assertThat(testPollResponse.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testPollResponse.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPollResponse.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();
        pollResponse.setId(longCount.incrementAndGet());

        // Create the PollResponse
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollResponseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();
        pollResponse.setId(longCount.incrementAndGet());

        // Create the PollResponse
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();
        pollResponse.setId(longCount.incrementAndGet());

        // Create the PollResponse
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePollResponseWithPatch() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();

        // Update the pollResponse using partial update
        PollResponse partialUpdatedPollResponse = new PollResponse();
        partialUpdatedPollResponse.setId(pollResponse.getId());

        partialUpdatedPollResponse.createdAt(UPDATED_CREATED_AT);

        restPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPollResponse))
            )
            .andExpect(status().isOk());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
        PollResponse testPollResponse = pollResponseList.get(pollResponseList.size() - 1);
        assertThat(testPollResponse.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testPollResponse.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPollResponse.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePollResponseWithPatch() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();

        // Update the pollResponse using partial update
        PollResponse partialUpdatedPollResponse = new PollResponse();
        partialUpdatedPollResponse.setId(pollResponse.getId());

        partialUpdatedPollResponse.comment(UPDATED_COMMENT).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPollResponse))
            )
            .andExpect(status().isOk());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
        PollResponse testPollResponse = pollResponseList.get(pollResponseList.size() - 1);
        assertThat(testPollResponse.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testPollResponse.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPollResponse.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();
        pollResponse.setId(longCount.incrementAndGet());

        // Create the PollResponse
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pollResponseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();
        pollResponse.setId(longCount.incrementAndGet());

        // Create the PollResponse
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = pollResponseRepository.findAll().size();
        pollResponse.setId(longCount.incrementAndGet());

        // Create the PollResponse
        PollResponseDTO pollResponseDTO = pollResponseMapper.toDto(pollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollResponseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollResponse in the database
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePollResponse() throws Exception {
        // Initialize the database
        pollResponseRepository.saveAndFlush(pollResponse);

        int databaseSizeBeforeDelete = pollResponseRepository.findAll().size();

        // Delete the pollResponse
        restPollResponseMockMvc
            .perform(delete(ENTITY_API_URL_ID, pollResponse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PollResponse> pollResponseList = pollResponseRepository.findAll();
        assertThat(pollResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
