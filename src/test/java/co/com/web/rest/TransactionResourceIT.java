package co.com.web.rest;

import static co.com.web.rest.TestUtil.sameInstant;
import static co.com.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.IntegrationTest;
import co.com.controller.TransactionResource;
import co.com.dto.TransactionDTO;
import co.com.entities.TransactionEntity;
import co.com.entities.enumeration.TransactionType;
import co.com.repository.TransactionRepository;
import co.com.service.mapper.transaction.TransactionQueriesMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionResourceIT {

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.CONSIGNACION;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.RETIRO;

    private static final ZonedDateTime DEFAULT_TRANSACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionQueriesMapper transactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionMockMvc;

    private TransactionEntity transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionEntity createEntity(EntityManager em) {
        TransactionEntity transaction = new TransactionEntity()
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .amount(DEFAULT_AMOUNT);
        return transaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionEntity createUpdatedEntity(EntityManager em) {
        TransactionEntity transaction = new TransactionEntity()
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .amount(UPDATED_AMOUNT);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();
        // Create the TransactionEntity
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);
        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionEntity testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testTransaction.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void createTransactionWithExistingId() throws Exception {
        // Create the TransactionEntity with an existing ID
        transactionRepository.saveAndFlush(transaction);
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTransactionType(null);

        // Create the TransactionEntity, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTransactionDate(null);

        // Create the TransactionEntity, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setAmount(null);

        // Create the TransactionEntity, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(sameInstant(DEFAULT_TRANSACTION_DATE))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.transactionDate").value(sameInstant(DEFAULT_TRANSACTION_DATE)))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        TransactionEntity updatedTransaction = transactionRepository.findById(transaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction.transactionType(UPDATED_TRANSACTION_TYPE).transactionDate(UPDATED_TRANSACTION_DATE).amount(UPDATED_AMOUNT);
        TransactionDTO transactionDTO = transactionMapper.toDomain(updatedTransaction);

        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        TransactionEntity testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(UUID.randomUUID());

        // Create the TransactionEntity
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(UUID.randomUUID());

        // Create the TransactionEntity
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(UUID.randomUUID());

        // Create the TransactionEntity
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        TransactionEntity partialUpdatedTransaction = new TransactionEntity();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction.transactionDate(UPDATED_TRANSACTION_DATE).amount(UPDATED_AMOUNT);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        TransactionEntity testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        TransactionEntity partialUpdatedTransaction = new TransactionEntity();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .amount(UPDATED_AMOUNT);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        TransactionEntity testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(UUID.randomUUID());

        // Create the TransactionEntity
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(UUID.randomUUID());

        // Create the TransactionEntity
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(UUID.randomUUID());

        // Create the TransactionEntity
        TransactionDTO transactionDTO = transactionMapper.toDomain(transaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionEntity in the database
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaction.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionEntity> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
