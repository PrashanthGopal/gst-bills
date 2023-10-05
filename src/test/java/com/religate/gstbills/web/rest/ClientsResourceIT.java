package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.Clients;
import com.religate.gstbills.domain.enumeration.Status;
import com.religate.gstbills.repository.ClientsRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ClientsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientsResourceIT {

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GSTIN = "AAAAAAAAAA";
    private static final String UPDATED_GSTIN = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientsMockMvc;

    private Clients clients;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clients createEntity(EntityManager em) {
        Clients clients = new Clients()
            .clientId(DEFAULT_CLIENT_ID)
            .name(DEFAULT_NAME)
            .gstin(DEFAULT_GSTIN)
            .status(DEFAULT_STATUS)
            .emailId(DEFAULT_EMAIL_ID)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return clients;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clients createUpdatedEntity(EntityManager em) {
        Clients clients = new Clients()
            .clientId(UPDATED_CLIENT_ID)
            .name(UPDATED_NAME)
            .gstin(UPDATED_GSTIN)
            .status(UPDATED_STATUS)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return clients;
    }

    @BeforeEach
    public void initTest() {
        clients = createEntity(em);
    }

    @Test
    @Transactional
    void createClients() throws Exception {
        int databaseSizeBeforeCreate = clientsRepository.findAll().size();
        // Create the Clients
        restClientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isCreated());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeCreate + 1);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testClients.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClients.getGstin()).isEqualTo(DEFAULT_GSTIN);
        assertThat(testClients.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testClients.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testClients.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void createClientsWithExistingId() throws Exception {
        // Create the Clients with an existing ID
        clients.setId(1L);

        int databaseSizeBeforeCreate = clientsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        // Get all the clientsList
        restClientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clients.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gstin").value(hasItem(DEFAULT_GSTIN)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        // Get the clients
        restClientsMockMvc
            .perform(get(ENTITY_API_URL_ID, clients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clients.getId().intValue()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gstin").value(DEFAULT_GSTIN))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingClients() throws Exception {
        // Get the clients
        restClientsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();

        // Update the clients
        Clients updatedClients = clientsRepository.findById(clients.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClients are not directly saved in db
        em.detach(updatedClients);
        updatedClients
            .clientId(UPDATED_CLIENT_ID)
            .name(UPDATED_NAME)
            .gstin(UPDATED_GSTIN)
            .status(UPDATED_STATUS)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restClientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClients.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClients))
            )
            .andExpect(status().isOk());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testClients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClients.getGstin()).isEqualTo(UPDATED_GSTIN);
        assertThat(testClients.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testClients.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testClients.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clients.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientsWithPatch() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();

        // Update the clients using partial update
        Clients partialUpdatedClients = new Clients();
        partialUpdatedClients.setId(clients.getId());

        partialUpdatedClients.name(UPDATED_NAME).gstin(UPDATED_GSTIN).emailId(UPDATED_EMAIL_ID).phoneNumber(UPDATED_PHONE_NUMBER);

        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClients))
            )
            .andExpect(status().isOk());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testClients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClients.getGstin()).isEqualTo(UPDATED_GSTIN);
        assertThat(testClients.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testClients.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testClients.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateClientsWithPatch() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();

        // Update the clients using partial update
        Clients partialUpdatedClients = new Clients();
        partialUpdatedClients.setId(clients.getId());

        partialUpdatedClients
            .clientId(UPDATED_CLIENT_ID)
            .name(UPDATED_NAME)
            .gstin(UPDATED_GSTIN)
            .status(UPDATED_STATUS)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClients))
            )
            .andExpect(status().isOk());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testClients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClients.getGstin()).isEqualTo(UPDATED_GSTIN);
        assertThat(testClients.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testClients.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testClients.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeDelete = clientsRepository.findAll().size();

        // Delete the clients
        restClientsMockMvc
            .perform(delete(ENTITY_API_URL_ID, clients.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
