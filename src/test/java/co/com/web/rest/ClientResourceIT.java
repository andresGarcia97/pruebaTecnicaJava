package co.com.web.rest;

import static co.com.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.IntegrationTest;
import co.com.entities.ClientEntity;
import co.com.entities.enumeration.IdentificationType;
import co.com.repository.ClientRepository;
import co.com.service.dto.ClientDTO;
import co.com.service.mapper.client.ClientQueriesMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final IdentificationType DEFAULT_IDENTIFICATION_TYPE = IdentificationType.CEDULA;
    private static final IdentificationType UPDATED_IDENTIFICATION_TYPE = IdentificationType.PASAPORTE;

    private static final String DEFAULT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BORN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientQueriesMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private ClientEntity client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientEntity createEntity(EntityManager em) {
        ClientEntity client = new ClientEntity()
            .identificationType(DEFAULT_IDENTIFICATION_TYPE)
            .identification(DEFAULT_IDENTIFICATION)
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .bornDate(DEFAULT_BORN_DATE)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastModificationDate(DEFAULT_LAST_MODIFICATION_DATE);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientEntity createUpdatedEntity(EntityManager em) {
        ClientEntity client = new ClientEntity()
            .identificationType(UPDATED_IDENTIFICATION_TYPE)
            .identification(UPDATED_IDENTIFICATION)
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .bornDate(UPDATED_BORN_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastModificationDate(UPDATED_LAST_MODIFICATION_DATE);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        // Create the ClientEntity
        ClientDTO clientDTO = clientMapper.toDomain(client);
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isCreated());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        ClientEntity testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdentificationType()).isEqualTo(DEFAULT_IDENTIFICATION_TYPE);
        assertThat(testClient.getIdentification()).isEqualTo(DEFAULT_IDENTIFICATION);
        assertThat(testClient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getBornDate()).isEqualTo(DEFAULT_BORN_DATE);
        assertThat(testClient.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testClient.getLastModificationDate()).isEqualTo(DEFAULT_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the ClientEntity with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDomain(client);

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentificationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setIdentificationType(null);

        // Create the ClientEntity, which fails.
        ClientDTO clientDTO = clientMapper.toDomain(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setIdentification(null);

        // Create the ClientEntity, which fails.
        ClientDTO clientDTO = clientMapper.toDomain(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBornDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setBornDate(null);

        // Create the ClientEntity, which fails.
        ClientDTO clientDTO = clientMapper.toDomain(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setCreationDate(null);

        // Create the ClientEntity, which fails.
        ClientDTO clientDTO = clientMapper.toDomain(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificationType").value(hasItem(DEFAULT_IDENTIFICATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].bornDate").value(hasItem(DEFAULT_BORN_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastModificationDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFICATION_DATE))));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.identificationType").value(DEFAULT_IDENTIFICATION_TYPE.toString()))
            .andExpect(jsonPath("$.identification").value(DEFAULT_IDENTIFICATION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.bornDate").value(DEFAULT_BORN_DATE.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastModificationDate").value(sameInstant(DEFAULT_LAST_MODIFICATION_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        ClientEntity updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .identificationType(UPDATED_IDENTIFICATION_TYPE)
            .identification(UPDATED_IDENTIFICATION)
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .bornDate(UPDATED_BORN_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastModificationDate(UPDATED_LAST_MODIFICATION_DATE);
        ClientDTO clientDTO = clientMapper.toDomain(updatedClient);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        ClientEntity testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdentificationType()).isEqualTo(UPDATED_IDENTIFICATION_TYPE);
        assertThat(testClient.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getBornDate()).isEqualTo(UPDATED_BORN_DATE);
        assertThat(testClient.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testClient.getLastModificationDate()).isEqualTo(UPDATED_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the ClientEntity
        ClientDTO clientDTO = clientMapper.toDomain(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the ClientEntity
        ClientDTO clientDTO = clientMapper.toDomain(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the ClientEntity
        ClientDTO clientDTO = clientMapper.toDomain(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        ClientEntity partialUpdatedClient = new ClientEntity();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient.identificationType(UPDATED_IDENTIFICATION_TYPE).bornDate(UPDATED_BORN_DATE);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        ClientEntity testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdentificationType()).isEqualTo(UPDATED_IDENTIFICATION_TYPE);
        assertThat(testClient.getIdentification()).isEqualTo(DEFAULT_IDENTIFICATION);
        assertThat(testClient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getBornDate()).isEqualTo(UPDATED_BORN_DATE);
        assertThat(testClient.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testClient.getLastModificationDate()).isEqualTo(DEFAULT_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        ClientEntity partialUpdatedClient = new ClientEntity();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .identificationType(UPDATED_IDENTIFICATION_TYPE)
            .identification(UPDATED_IDENTIFICATION)
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .bornDate(UPDATED_BORN_DATE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastModificationDate(UPDATED_LAST_MODIFICATION_DATE);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        ClientEntity testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdentificationType()).isEqualTo(UPDATED_IDENTIFICATION_TYPE);
        assertThat(testClient.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getBornDate()).isEqualTo(UPDATED_BORN_DATE);
        assertThat(testClient.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testClient.getLastModificationDate()).isEqualTo(UPDATED_LAST_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the ClientEntity
        ClientDTO clientDTO = clientMapper.toDomain(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the ClientEntity
        ClientDTO clientDTO = clientMapper.toDomain(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the ClientEntity
        ClientDTO clientDTO = clientMapper.toDomain(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientEntity in the database
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClientEntity> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
