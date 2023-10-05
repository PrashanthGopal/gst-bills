package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.Transporter;
import com.religate.gstbills.domain.enumeration.Status;
import com.religate.gstbills.repository.TransporterRepository;
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
 * Integration tests for the {@link TransporterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransporterResourceIT {

    private static final String DEFAULT_ORG_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORG_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSPORTER_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSPORTER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final String ENTITY_API_URL = "/api/transporters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransporterRepository transporterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransporterMockMvc;

    private Transporter transporter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transporter createEntity(EntityManager em) {
        Transporter transporter = new Transporter()
            .orgId(DEFAULT_ORG_ID)
            .transporterId(DEFAULT_TRANSPORTER_ID)
            .name(DEFAULT_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .status(DEFAULT_STATUS);
        return transporter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transporter createUpdatedEntity(EntityManager em) {
        Transporter transporter = new Transporter()
            .orgId(UPDATED_ORG_ID)
            .transporterId(UPDATED_TRANSPORTER_ID)
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .status(UPDATED_STATUS);
        return transporter;
    }

    @BeforeEach
    public void initTest() {
        transporter = createEntity(em);
    }

    @Test
    @Transactional
    void createTransporter() throws Exception {
        int databaseSizeBeforeCreate = transporterRepository.findAll().size();
        // Create the Transporter
        restTransporterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transporter)))
            .andExpect(status().isCreated());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeCreate + 1);
        Transporter testTransporter = transporterList.get(transporterList.size() - 1);
        assertThat(testTransporter.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testTransporter.getTransporterId()).isEqualTo(DEFAULT_TRANSPORTER_ID);
        assertThat(testTransporter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransporter.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTransporter.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createTransporterWithExistingId() throws Exception {
        // Create the Transporter with an existing ID
        transporter.setId(1L);

        int databaseSizeBeforeCreate = transporterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransporterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transporter)))
            .andExpect(status().isBadRequest());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransporters() throws Exception {
        // Initialize the database
        transporterRepository.saveAndFlush(transporter);

        // Get all the transporterList
        restTransporterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transporter.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgId").value(hasItem(DEFAULT_ORG_ID)))
            .andExpect(jsonPath("$.[*].transporterId").value(hasItem(DEFAULT_TRANSPORTER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getTransporter() throws Exception {
        // Initialize the database
        transporterRepository.saveAndFlush(transporter);

        // Get the transporter
        restTransporterMockMvc
            .perform(get(ENTITY_API_URL_ID, transporter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transporter.getId().intValue()))
            .andExpect(jsonPath("$.orgId").value(DEFAULT_ORG_ID))
            .andExpect(jsonPath("$.transporterId").value(DEFAULT_TRANSPORTER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransporter() throws Exception {
        // Get the transporter
        restTransporterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransporter() throws Exception {
        // Initialize the database
        transporterRepository.saveAndFlush(transporter);

        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();

        // Update the transporter
        Transporter updatedTransporter = transporterRepository.findById(transporter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransporter are not directly saved in db
        em.detach(updatedTransporter);
        updatedTransporter
            .orgId(UPDATED_ORG_ID)
            .transporterId(UPDATED_TRANSPORTER_ID)
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .status(UPDATED_STATUS);

        restTransporterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransporter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransporter))
            )
            .andExpect(status().isOk());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
        Transporter testTransporter = transporterList.get(transporterList.size() - 1);
        assertThat(testTransporter.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testTransporter.getTransporterId()).isEqualTo(UPDATED_TRANSPORTER_ID);
        assertThat(testTransporter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransporter.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTransporter.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingTransporter() throws Exception {
        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();
        transporter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransporterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transporter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransporter() throws Exception {
        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();
        transporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransporter() throws Exception {
        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();
        transporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transporter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransporterWithPatch() throws Exception {
        // Initialize the database
        transporterRepository.saveAndFlush(transporter);

        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();

        // Update the transporter using partial update
        Transporter partialUpdatedTransporter = new Transporter();
        partialUpdatedTransporter.setId(transporter.getId());

        partialUpdatedTransporter.orgId(UPDATED_ORG_ID).name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).status(UPDATED_STATUS);

        restTransporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransporter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransporter))
            )
            .andExpect(status().isOk());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
        Transporter testTransporter = transporterList.get(transporterList.size() - 1);
        assertThat(testTransporter.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testTransporter.getTransporterId()).isEqualTo(DEFAULT_TRANSPORTER_ID);
        assertThat(testTransporter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransporter.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTransporter.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateTransporterWithPatch() throws Exception {
        // Initialize the database
        transporterRepository.saveAndFlush(transporter);

        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();

        // Update the transporter using partial update
        Transporter partialUpdatedTransporter = new Transporter();
        partialUpdatedTransporter.setId(transporter.getId());

        partialUpdatedTransporter
            .orgId(UPDATED_ORG_ID)
            .transporterId(UPDATED_TRANSPORTER_ID)
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .status(UPDATED_STATUS);

        restTransporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransporter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransporter))
            )
            .andExpect(status().isOk());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
        Transporter testTransporter = transporterList.get(transporterList.size() - 1);
        assertThat(testTransporter.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testTransporter.getTransporterId()).isEqualTo(UPDATED_TRANSPORTER_ID);
        assertThat(testTransporter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransporter.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTransporter.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingTransporter() throws Exception {
        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();
        transporter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transporter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransporter() throws Exception {
        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();
        transporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transporter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransporter() throws Exception {
        int databaseSizeBeforeUpdate = transporterRepository.findAll().size();
        transporter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transporter))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transporter in the database
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransporter() throws Exception {
        // Initialize the database
        transporterRepository.saveAndFlush(transporter);

        int databaseSizeBeforeDelete = transporterRepository.findAll().size();

        // Delete the transporter
        restTransporterMockMvc
            .perform(delete(ENTITY_API_URL_ID, transporter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transporter> transporterList = transporterRepository.findAll();
        assertThat(transporterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
