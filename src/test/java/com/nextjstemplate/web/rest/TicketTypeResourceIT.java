package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TicketType;
import com.nextjstemplate.repository.TicketTypeRepository;
import com.nextjstemplate.service.dto.TicketTypeDTO;
import com.nextjstemplate.service.mapper.TicketTypeMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TicketTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVAILABLE_QUANTITY = 1;
    private static final Integer UPDATED_AVAILABLE_QUANTITY = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ticket-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketTypeMockMvc;

    private TicketType ticketType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketType createEntity(EntityManager em) {
        TicketType ticketType = new TicketType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .code(DEFAULT_CODE)
            .availableQuantity(DEFAULT_AVAILABLE_QUANTITY)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return ticketType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketType createUpdatedEntity(EntityManager em) {
        TicketType ticketType = new TicketType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .code(UPDATED_CODE)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return ticketType;
    }

    @BeforeEach
    public void initTest() {
        ticketType = createEntity(em);
    }

    @Test
    @Transactional
    void createTicketType() throws Exception {
        int databaseSizeBeforeCreate = ticketTypeRepository.findAll().size();
        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);
        restTicketTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTicketType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicketType.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testTicketType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTicketType.getAvailableQuantity()).isEqualTo(DEFAULT_AVAILABLE_QUANTITY);
        assertThat(testTicketType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTicketType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTicketType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTicketTypeWithExistingId() throws Exception {
        // Create the TicketType with an existing ID
        ticketType.setId(1L);
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        int databaseSizeBeforeCreate = ticketTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().size();
        // set the field null
        ticketType.setName(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        restTicketTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().size();
        // set the field null
        ticketType.setPrice(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        restTicketTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().size();
        // set the field null
        ticketType.setCode(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        restTicketTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().size();
        // set the field null
        ticketType.setCreatedAt(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        restTicketTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().size();
        // set the field null
        ticketType.setUpdatedAt(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        restTicketTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTicketTypes() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        // Get all the ticketTypeList
        restTicketTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].availableQuantity").value(hasItem(DEFAULT_AVAILABLE_QUANTITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTicketType() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        // Get the ticketType
        restTicketTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, ticketType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticketType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.availableQuantity").value(DEFAULT_AVAILABLE_QUANTITY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTicketType() throws Exception {
        // Get the ticketType
        restTicketTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicketType() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();

        // Update the ticketType
        TicketType updatedTicketType = ticketTypeRepository.findById(ticketType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTicketType are not directly saved in db
        em.detach(updatedTicketType);
        updatedTicketType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .code(UPDATED_CODE)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(updatedTicketType);

        restTicketTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicketType.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testTicketType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTicketType.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testTicketType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTicketType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTicketType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();
        ticketType.setId(longCount.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();
        ticketType.setId(longCount.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();
        ticketType.setId(longCount.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketTypeWithPatch() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();

        // Update the ticketType using partial update
        TicketType partialUpdatedTicketType = new TicketType();
        partialUpdatedTicketType.setId(ticketType.getId());

        partialUpdatedTicketType.name(UPDATED_NAME).price(UPDATED_PRICE).code(UPDATED_CODE).updatedAt(UPDATED_UPDATED_AT);

        restTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketType))
            )
            .andExpect(status().isOk());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicketType.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testTicketType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTicketType.getAvailableQuantity()).isEqualTo(DEFAULT_AVAILABLE_QUANTITY);
        assertThat(testTicketType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTicketType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTicketType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTicketTypeWithPatch() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();

        // Update the ticketType using partial update
        TicketType partialUpdatedTicketType = new TicketType();
        partialUpdatedTicketType.setId(ticketType.getId());

        partialUpdatedTicketType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .code(UPDATED_CODE)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketType))
            )
            .andExpect(status().isOk());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicketType.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testTicketType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTicketType.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testTicketType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTicketType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTicketType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();
        ticketType.setId(longCount.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticketTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();
        ticketType.setId(longCount.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();
        ticketType.setId(longCount.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicketType() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        int databaseSizeBeforeDelete = ticketTypeRepository.findAll().size();

        // Delete the ticketType
        restTicketTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticketType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
