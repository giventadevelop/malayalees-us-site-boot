package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.TicketTransaction;
import com.nextjstemplate.domain.TicketType;
import com.nextjstemplate.domain.UserProfile;
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
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_PRICE_PER_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_UNIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE_PER_UNIT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

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
    void getTicketTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        Long id = ticketTransaction.getId();

        defaultTicketTransactionShouldBeFound("id.equals=" + id);
        defaultTicketTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultTicketTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email equals to DEFAULT_EMAIL
        defaultTicketTransactionShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the ticketTransactionList where email equals to UPDATED_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTicketTransactionShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the ticketTransactionList where email equals to UPDATED_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email is not null
        defaultTicketTransactionShouldBeFound("email.specified=true");

        // Get all the ticketTransactionList where email is null
        defaultTicketTransactionShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email contains DEFAULT_EMAIL
        defaultTicketTransactionShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the ticketTransactionList where email contains UPDATED_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email does not contain DEFAULT_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the ticketTransactionList where email does not contain UPDATED_EMAIL
        defaultTicketTransactionShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where firstName equals to DEFAULT_FIRST_NAME
        defaultTicketTransactionShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the ticketTransactionList where firstName equals to UPDATED_FIRST_NAME
        defaultTicketTransactionShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTicketTransactionShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the ticketTransactionList where firstName equals to UPDATED_FIRST_NAME
        defaultTicketTransactionShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where firstName is not null
        defaultTicketTransactionShouldBeFound("firstName.specified=true");

        // Get all the ticketTransactionList where firstName is null
        defaultTicketTransactionShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where firstName contains DEFAULT_FIRST_NAME
        defaultTicketTransactionShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the ticketTransactionList where firstName contains UPDATED_FIRST_NAME
        defaultTicketTransactionShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTicketTransactionShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the ticketTransactionList where firstName does not contain UPDATED_FIRST_NAME
        defaultTicketTransactionShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where lastName equals to DEFAULT_LAST_NAME
        defaultTicketTransactionShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the ticketTransactionList where lastName equals to UPDATED_LAST_NAME
        defaultTicketTransactionShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTicketTransactionShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the ticketTransactionList where lastName equals to UPDATED_LAST_NAME
        defaultTicketTransactionShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where lastName is not null
        defaultTicketTransactionShouldBeFound("lastName.specified=true");

        // Get all the ticketTransactionList where lastName is null
        defaultTicketTransactionShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where lastName contains DEFAULT_LAST_NAME
        defaultTicketTransactionShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the ticketTransactionList where lastName contains UPDATED_LAST_NAME
        defaultTicketTransactionShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where lastName does not contain DEFAULT_LAST_NAME
        defaultTicketTransactionShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the ticketTransactionList where lastName does not contain UPDATED_LAST_NAME
        defaultTicketTransactionShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity equals to DEFAULT_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity equals to UPDATED_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the ticketTransactionList where quantity equals to UPDATED_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is not null
        defaultTicketTransactionShouldBeFound("quantity.specified=true");

        // Get all the ticketTransactionList where quantity is null
        defaultTicketTransactionShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is less than or equal to SMALLER_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is less than DEFAULT_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is less than UPDATED_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is greater than DEFAULT_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is greater than SMALLER_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit equals to DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.equals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.equals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit in DEFAULT_PRICE_PER_UNIT or UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.in=" + DEFAULT_PRICE_PER_UNIT + "," + UPDATED_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.in=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is not null
        defaultTicketTransactionShouldBeFound("pricePerUnit.specified=true");

        // Get all the ticketTransactionList where pricePerUnit is null
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is greater than or equal to DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.greaterThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is greater than or equal to UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.greaterThanOrEqual=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is less than or equal to DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.lessThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is less than or equal to SMALLER_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.lessThanOrEqual=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is less than DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.lessThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is less than UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.lessThan=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is greater than DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.greaterThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is greater than SMALLER_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.greaterThan=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is not null
        defaultTicketTransactionShouldBeFound("totalAmount.specified=true");

        // Get all the ticketTransactionList where totalAmount is null
        defaultTicketTransactionShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status equals to DEFAULT_STATUS
        defaultTicketTransactionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the ticketTransactionList where status equals to UPDATED_STATUS
        defaultTicketTransactionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTicketTransactionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the ticketTransactionList where status equals to UPDATED_STATUS
        defaultTicketTransactionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status is not null
        defaultTicketTransactionShouldBeFound("status.specified=true");

        // Get all the ticketTransactionList where status is null
        defaultTicketTransactionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status contains DEFAULT_STATUS
        defaultTicketTransactionShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the ticketTransactionList where status contains UPDATED_STATUS
        defaultTicketTransactionShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status does not contain DEFAULT_STATUS
        defaultTicketTransactionShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the ticketTransactionList where status does not contain UPDATED_STATUS
        defaultTicketTransactionShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate is not null
        defaultTicketTransactionShouldBeFound("purchaseDate.specified=true");

        // Get all the ticketTransactionList where purchaseDate is null
        defaultTicketTransactionShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where createdAt equals to DEFAULT_CREATED_AT
        defaultTicketTransactionShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the ticketTransactionList where createdAt equals to UPDATED_CREATED_AT
        defaultTicketTransactionShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTicketTransactionShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the ticketTransactionList where createdAt equals to UPDATED_CREATED_AT
        defaultTicketTransactionShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where createdAt is not null
        defaultTicketTransactionShouldBeFound("createdAt.specified=true");

        // Get all the ticketTransactionList where createdAt is null
        defaultTicketTransactionShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTicketTransactionShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the ticketTransactionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTicketTransactionShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTicketTransactionShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the ticketTransactionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTicketTransactionShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where updatedAt is not null
        defaultTicketTransactionShouldBeFound("updatedAt.specified=true");

        // Get all the ticketTransactionList where updatedAt is null
        defaultTicketTransactionShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            ticketTransactionRepository.saveAndFlush(ticketTransaction);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        ticketTransaction.setEvent(event);
        ticketTransactionRepository.saveAndFlush(ticketTransaction);
        Long eventId = event.getId();
        // Get all the ticketTransactionList where event equals to eventId
        defaultTicketTransactionShouldBeFound("eventId.equals=" + eventId);

        // Get all the ticketTransactionList where event equals to (eventId + 1)
        defaultTicketTransactionShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTicketTypeIsEqualToSomething() throws Exception {
        TicketType ticketType;
        if (TestUtil.findAll(em, TicketType.class).isEmpty()) {
            ticketTransactionRepository.saveAndFlush(ticketTransaction);
            ticketType = TicketTypeResourceIT.createEntity(em);
        } else {
            ticketType = TestUtil.findAll(em, TicketType.class).get(0);
        }
        em.persist(ticketType);
        em.flush();
        ticketTransaction.setTicketType(ticketType);
        ticketTransactionRepository.saveAndFlush(ticketTransaction);
        Long ticketTypeId = ticketType.getId();
        // Get all the ticketTransactionList where ticketType equals to ticketTypeId
        defaultTicketTransactionShouldBeFound("ticketTypeId.equals=" + ticketTypeId);

        // Get all the ticketTransactionList where ticketType equals to (ticketTypeId + 1)
        defaultTicketTransactionShouldNotBeFound("ticketTypeId.equals=" + (ticketTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            ticketTransactionRepository.saveAndFlush(ticketTransaction);
            user = UserProfileResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        ticketTransaction.setUser(user);
        ticketTransactionRepository.saveAndFlush(ticketTransaction);
        Long userId = user.getId();
        // Get all the ticketTransactionList where user equals to userId
        defaultTicketTransactionShouldBeFound("userId.equals=" + userId);

        // Get all the ticketTransactionList where user equals to (userId + 1)
        defaultTicketTransactionShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketTransactionShouldBeFound(String filter) throws Exception {
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketTransactionShouldNotBeFound(String filter) throws Exception {
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .quantity(UPDATED_QUANTITY)
            .purchaseDate(UPDATED_PURCHASE_DATE);

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
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testTicketTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
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
