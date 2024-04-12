package co.com.web.rest;

import static co.com.web.rest.TestUtil.sameInstant;
import static co.com.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.IntegrationTest;
import co.com.entities.AccountBankEntity;
import co.com.entities.ClientEntity;
import co.com.entities.enumeration.AccountState;
import co.com.entities.enumeration.AccountType;
import co.com.repository.AccountBankRepository;
import co.com.service.dto.AccountBankDTO;
import co.com.service.mapper.accountbank.AccountBankQueriesMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link AccountBankResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountBankResourceIT {

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.CUENTA_CORRIENTE;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.CUENTA_AHORROS;

    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;

    private static final AccountState DEFAULT_STATE = AccountState.ACTIVA;
    private static final AccountState UPDATED_STATE = AccountState.INACTIVA;

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(1);

    private static final Boolean DEFAULT_EXENT_GMF = false;
    private static final Boolean UPDATED_EXENT_GMF = true;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/account-banks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountBankRepository accountBankRepository;

    @Autowired
    private AccountBankQueriesMapper accountBankMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountBankMockMvc;

    private AccountBankEntity accountBank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBankEntity createEntity(EntityManager em) {
        AccountBankEntity accountBank = new AccountBankEntity()
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .number(DEFAULT_NUMBER)
            .state(DEFAULT_STATE)
            .balance(DEFAULT_BALANCE)
            .exentGMF(DEFAULT_EXENT_GMF)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastModificationDate(DEFAULT_LAST_MODIFICATION_DATE);
        // Add required entity
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        accountBank.setAccount(client);
        return accountBank;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBankEntity createUpdatedEntity(EntityManager em) {
        AccountBankEntity accountBank = new AccountBankEntity()
            .accountType(UPDATED_ACCOUNT_TYPE)
            .number(UPDATED_NUMBER)
            .state(UPDATED_STATE)
            .balance(UPDATED_BALANCE)
            .exentGMF(UPDATED_EXENT_GMF)
            .creationDate(UPDATED_CREATION_DATE)
            .lastModificationDate(UPDATED_LAST_MODIFICATION_DATE);
        // Add required entity
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        accountBank.setAccount(client);
        return accountBank;
    }

    @BeforeEach
    public void initTest() {
        accountBank = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountBank() throws Exception {
        int databaseSizeBeforeCreate = accountBankRepository.findAll().size();
        // Create the AccountBankEntity
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);
        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeCreate + 1);
        AccountBankEntity testAccountBank = accountBankList.get(accountBankList.size() - 1);
        assertThat(testAccountBank.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccountBank.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testAccountBank.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAccountBank.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testAccountBank.getExentGMF()).isEqualTo(DEFAULT_EXENT_GMF);
        assertThat(testAccountBank.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAccountBank.getLastModificationDate()).isEqualTo(DEFAULT_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void createAccountBankWithExistingId() throws Exception {
        // Create the AccountBankEntity with an existing ID
        accountBank.setId(1L);
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        int databaseSizeBeforeCreate = accountBankRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBankRepository.findAll().size();
        // set the field null
        accountBank.setAccountType(null);

        // Create the AccountBankEntity, which fails.
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBankRepository.findAll().size();
        // set the field null
        accountBank.setNumber(null);

        // Create the AccountBankEntity, which fails.
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBankRepository.findAll().size();
        // set the field null
        accountBank.setState(null);

        // Create the AccountBankEntity, which fails.
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBankRepository.findAll().size();
        // set the field null
        accountBank.setBalance(null);

        // Create the AccountBankEntity, which fails.
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExentGMFIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBankRepository.findAll().size();
        // set the field null
        accountBank.setExentGMF(null);

        // Create the AccountBankEntity, which fails.
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBankRepository.findAll().size();
        // set the field null
        accountBank.setCreationDate(null);

        // Create the AccountBankEntity, which fails.
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        restAccountBankMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountBanks() throws Exception {
        // Initialize the database
        accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBank.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].exentGMF").value(hasItem(DEFAULT_EXENT_GMF.booleanValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastModificationDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFICATION_DATE))));
    }

    @Test
    @Transactional
    void getAccountBank() throws Exception {
        // Initialize the database
        accountBankRepository.saveAndFlush(accountBank);

        // Get the accountBank
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL_ID, accountBank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountBank.getId().intValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.exentGMF").value(DEFAULT_EXENT_GMF.booleanValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastModificationDate").value(sameInstant(DEFAULT_LAST_MODIFICATION_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingAccountBank() throws Exception {
        // Get the accountBank
        restAccountBankMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountBank() throws Exception {
        // Initialize the database
        accountBankRepository.saveAndFlush(accountBank);

        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();

        // Update the accountBank
        AccountBankEntity updatedAccountBank = accountBankRepository.findById(accountBank.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccountBank are not directly saved in db
        em.detach(updatedAccountBank);
        updatedAccountBank
            .accountType(UPDATED_ACCOUNT_TYPE)
            .number(UPDATED_NUMBER)
            .state(UPDATED_STATE)
            .balance(UPDATED_BALANCE)
            .exentGMF(UPDATED_EXENT_GMF)
            .creationDate(UPDATED_CREATION_DATE)
            .lastModificationDate(UPDATED_LAST_MODIFICATION_DATE);
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(updatedAccountBank);

        restAccountBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBankDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
        AccountBankEntity testAccountBank = accountBankList.get(accountBankList.size() - 1);
        assertThat(testAccountBank.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountBank.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAccountBank.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAccountBank.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAccountBank.getExentGMF()).isEqualTo(UPDATED_EXENT_GMF);
        assertThat(testAccountBank.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAccountBank.getLastModificationDate()).isEqualTo(UPDATED_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAccountBank() throws Exception {
        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBankEntity
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBankDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountBank() throws Exception {
        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBankEntity
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountBank() throws Exception {
        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBankEntity
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBankDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountBankWithPatch() throws Exception {
        // Initialize the database
        accountBankRepository.saveAndFlush(accountBank);

        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();

        // Update the accountBank using partial update
        AccountBankEntity partialUpdatedAccountBank = new AccountBankEntity();
        partialUpdatedAccountBank.setId(accountBank.getId());

        partialUpdatedAccountBank.state(UPDATED_STATE).balance(UPDATED_BALANCE).lastModificationDate(UPDATED_LAST_MODIFICATION_DATE);

        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountBank))
            )
            .andExpect(status().isOk());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
        AccountBankEntity testAccountBank = accountBankList.get(accountBankList.size() - 1);
        assertThat(testAccountBank.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccountBank.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testAccountBank.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAccountBank.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAccountBank.getExentGMF()).isEqualTo(DEFAULT_EXENT_GMF);
        assertThat(testAccountBank.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAccountBank.getLastModificationDate()).isEqualTo(UPDATED_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAccountBankWithPatch() throws Exception {
        // Initialize the database
        accountBankRepository.saveAndFlush(accountBank);

        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();

        // Update the accountBank using partial update
        AccountBankEntity partialUpdatedAccountBank = new AccountBankEntity();
        partialUpdatedAccountBank.setId(accountBank.getId());

        partialUpdatedAccountBank
            .accountType(UPDATED_ACCOUNT_TYPE)
            .number(UPDATED_NUMBER)
            .state(UPDATED_STATE)
            .balance(UPDATED_BALANCE)
            .exentGMF(UPDATED_EXENT_GMF)
            .creationDate(UPDATED_CREATION_DATE)
            .lastModificationDate(UPDATED_LAST_MODIFICATION_DATE);

        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountBank))
            )
            .andExpect(status().isOk());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
        AccountBankEntity testAccountBank = accountBankList.get(accountBankList.size() - 1);
        assertThat(testAccountBank.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountBank.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAccountBank.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAccountBank.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAccountBank.getExentGMF()).isEqualTo(UPDATED_EXENT_GMF);
        assertThat(testAccountBank.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAccountBank.getLastModificationDate()).isEqualTo(UPDATED_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAccountBank() throws Exception {
        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBankEntity
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountBankDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountBank() throws Exception {
        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBankEntity
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountBank() throws Exception {
        int databaseSizeBeforeUpdate = accountBankRepository.findAll().size();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBankEntity
        AccountBankDTO accountBankDTO = accountBankMapper.toDomain(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBankEntity in the database
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountBank() throws Exception {
        // Initialize the database
        accountBankRepository.saveAndFlush(accountBank);

        int databaseSizeBeforeDelete = accountBankRepository.findAll().size();

        // Delete the accountBank
        restAccountBankMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountBank.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountBankEntity> accountBankList = accountBankRepository.findAll();
        assertThat(accountBankList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
