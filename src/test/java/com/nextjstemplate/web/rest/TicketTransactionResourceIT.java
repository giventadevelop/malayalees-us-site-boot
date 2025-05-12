package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TicketTransaction;
import com.nextjstemplate.repository.TicketTransactionRepository;
import com.nextjstemplate.service.dto.TicketTransactionDTO;
import com.nextjstemplate.service.mapper.TicketTransactionMapper;
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
 * Integration tests for the {@link TicketTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketTransactionResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_PRICE_PER_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_UNIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_PURCHASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PURCHASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ticket-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    @Autowired
    private TicketTransactionMapper ticketTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketTransactionMockMvc;

    private TicketTransaction ticketTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketTransaction createEntity(EntityManager em) {
        TicketTransaction ticketTransaction = new TicketTransaction()
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .quantity(DEFAULT_QUANTITY)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .status(DEFAULT_STATUS)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return ticketTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketTransaction createUpdatedEntity(EntityManager em) {
        TicketTransaction ticketTransaction = new TicketTransaction()
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return ticketTransaction;
    }

    @BeforeEach
    public void initTest() {
        ticketTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createTicketTransaction() throws Exception {
        int databaseSizeBeforeCreate = ticketTransactionRepository.findAll().size();
        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);
        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTicketTransaction.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTicketTransaction.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testTicketTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTicketTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTicketTransactionWithExistingId() throws Exception {
        // Create the TicketTransaction with an existing ID
        ticketTransaction.setId(1L);
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        int databaseSizeBeforeCreate = ticketTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setEmail(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setQuantity(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setPricePerUnit(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setTotalAmount(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setStatus(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPurchaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setPurchaseDate(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setCreatedAt(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setUpdatedAt(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTicketTransactions() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTicketTransaction() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get the ticketTransaction
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, ticketTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticketTransaction.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.pricePerUnit").value(sameNumber(DEFAULT_PRICE_PER_UNIT)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTicketTransaction() throws Exception {
        // Get the ticketTransaction
        restTicketTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicketTransaction() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();

        // Update the ticketTransaction
        TicketTransaction updatedTicketTransaction = ticketTransactionRepository.findById(ticketTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTicketTransaction are not directly saved in db
        em.detach(updatedTicketTransaction);
        updatedTicketTransaction
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(updatedTicketTransaction);

        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTicketTransaction.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTicketTransaction.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testTicketTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTicketTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();

        // Update the ticketTransaction using partial update
        TicketTransaction partialUpdatedTicketTransaction = new TicketTransaction();
        partialUpdatedTicketTransaction.setId(ticketTransaction.getId());

        partialUpdatedTicketTransaction
            .firstName(UPDATED_FIRST_NAME)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .createdAt(UPDATED_CREATED_AT);

        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTicketTransaction.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTicketTransaction.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testTicketTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTicketTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();

        // Update the ticketTransaction using partial update
        TicketTransaction partialUpdatedTicketTransaction = new TicketTransaction();
        partialUpdatedTicketTransaction.setId(ticketTransaction.getId());

        partialUpdatedTicketTransaction
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTicketTransaction.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTicketTransaction.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testTicketTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTicketTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticketTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicketTransaction() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeDelete = ticketTransactionRepository.findAll().size();

        // Delete the ticketTransaction
        restTicketTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticketTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
