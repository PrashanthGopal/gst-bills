package com.religate.gstbills.web.rest;

import static com.religate.gstbills.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.Organization;
import com.religate.gstbills.domain.enumeration.Status;
import com.religate.gstbills.repository.OrganizationRepository;
import jakarta.persistence.EntityManager;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationResourceIT {

    private static final String DEFAULT_ORG_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORG_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ORG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_ORG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_ORG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAN = "AAAAAAAAAA";
    private static final String UPDATED_PAN = "BBBBBBBBBB";

    private static final String DEFAULT_GSTIN = "AAAAAAAAAA";
    private static final String UPDATED_GSTIN = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final ZonedDateTime DEFAULT_DATE_OF_REGESTATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_OF_REGESTATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_OF_DE_REGESTATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_OF_DE_REGESTATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .orgId(DEFAULT_ORG_ID)
            .orgName(DEFAULT_ORG_NAME)
            .legalOrgName(DEFAULT_LEGAL_ORG_NAME)
            .pan(DEFAULT_PAN)
            .gstin(DEFAULT_GSTIN)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .emailId(DEFAULT_EMAIL_ID)
            .status(DEFAULT_STATUS)
            .dateOfRegestation(DEFAULT_DATE_OF_REGESTATION)
            .dateOfDeRegestation(DEFAULT_DATE_OF_DE_REGESTATION)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return organization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization()
            .orgId(UPDATED_ORG_ID)
            .orgName(UPDATED_ORG_NAME)
            .legalOrgName(UPDATED_LEGAL_ORG_NAME)
            .pan(UPDATED_PAN)
            .gstin(UPDATED_GSTIN)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailId(UPDATED_EMAIL_ID)
            .status(UPDATED_STATUS)
            .dateOfRegestation(UPDATED_DATE_OF_REGESTATION)
            .dateOfDeRegestation(UPDATED_DATE_OF_DE_REGESTATION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();
        // Create the Organization
        restOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testOrganization.getOrgName()).isEqualTo(DEFAULT_ORG_NAME);
        assertThat(testOrganization.getLegalOrgName()).isEqualTo(DEFAULT_LEGAL_ORG_NAME);
        assertThat(testOrganization.getPan()).isEqualTo(DEFAULT_PAN);
        assertThat(testOrganization.getGstin()).isEqualTo(DEFAULT_GSTIN);
        assertThat(testOrganization.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrganization.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testOrganization.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrganization.getDateOfRegestation()).isEqualTo(DEFAULT_DATE_OF_REGESTATION);
        assertThat(testOrganization.getDateOfDeRegestation()).isEqualTo(DEFAULT_DATE_OF_DE_REGESTATION);
        assertThat(testOrganization.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testOrganization.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createOrganizationWithExistingId() throws Exception {
        // Create the Organization with an existing ID
        organization.setId(1L);

        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgId").value(hasItem(DEFAULT_ORG_ID)))
            .andExpect(jsonPath("$.[*].orgName").value(hasItem(DEFAULT_ORG_NAME)))
            .andExpect(jsonPath("$.[*].legalOrgName").value(hasItem(DEFAULT_LEGAL_ORG_NAME)))
            .andExpect(jsonPath("$.[*].pan").value(hasItem(DEFAULT_PAN)))
            .andExpect(jsonPath("$.[*].gstin").value(hasItem(DEFAULT_GSTIN)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfRegestation").value(hasItem(sameInstant(DEFAULT_DATE_OF_REGESTATION))))
            .andExpect(jsonPath("$.[*].dateOfDeRegestation").value(hasItem(sameInstant(DEFAULT_DATE_OF_DE_REGESTATION))))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.orgId").value(DEFAULT_ORG_ID))
            .andExpect(jsonPath("$.orgName").value(DEFAULT_ORG_NAME))
            .andExpect(jsonPath("$.legalOrgName").value(DEFAULT_LEGAL_ORG_NAME))
            .andExpect(jsonPath("$.pan").value(DEFAULT_PAN))
            .andExpect(jsonPath("$.gstin").value(DEFAULT_GSTIN))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dateOfRegestation").value(sameInstant(DEFAULT_DATE_OF_REGESTATION)))
            .andExpect(jsonPath("$.dateOfDeRegestation").value(sameInstant(DEFAULT_DATE_OF_DE_REGESTATION)))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization
            .orgId(UPDATED_ORG_ID)
            .orgName(UPDATED_ORG_NAME)
            .legalOrgName(UPDATED_LEGAL_ORG_NAME)
            .pan(UPDATED_PAN)
            .gstin(UPDATED_GSTIN)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailId(UPDATED_EMAIL_ID)
            .status(UPDATED_STATUS)
            .dateOfRegestation(UPDATED_DATE_OF_REGESTATION)
            .dateOfDeRegestation(UPDATED_DATE_OF_DE_REGESTATION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testOrganization.getOrgName()).isEqualTo(UPDATED_ORG_NAME);
        assertThat(testOrganization.getLegalOrgName()).isEqualTo(UPDATED_LEGAL_ORG_NAME);
        assertThat(testOrganization.getPan()).isEqualTo(UPDATED_PAN);
        assertThat(testOrganization.getGstin()).isEqualTo(UPDATED_GSTIN);
        assertThat(testOrganization.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrganization.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testOrganization.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrganization.getDateOfRegestation()).isEqualTo(UPDATED_DATE_OF_REGESTATION);
        assertThat(testOrganization.getDateOfDeRegestation()).isEqualTo(UPDATED_DATE_OF_DE_REGESTATION);
        assertThat(testOrganization.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testOrganization.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization.pan(UPDATED_PAN).dateOfDeRegestation(UPDATED_DATE_OF_DE_REGESTATION);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testOrganization.getOrgName()).isEqualTo(DEFAULT_ORG_NAME);
        assertThat(testOrganization.getLegalOrgName()).isEqualTo(DEFAULT_LEGAL_ORG_NAME);
        assertThat(testOrganization.getPan()).isEqualTo(UPDATED_PAN);
        assertThat(testOrganization.getGstin()).isEqualTo(DEFAULT_GSTIN);
        assertThat(testOrganization.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrganization.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testOrganization.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrganization.getDateOfRegestation()).isEqualTo(DEFAULT_DATE_OF_REGESTATION);
        assertThat(testOrganization.getDateOfDeRegestation()).isEqualTo(UPDATED_DATE_OF_DE_REGESTATION);
        assertThat(testOrganization.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testOrganization.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization
            .orgId(UPDATED_ORG_ID)
            .orgName(UPDATED_ORG_NAME)
            .legalOrgName(UPDATED_LEGAL_ORG_NAME)
            .pan(UPDATED_PAN)
            .gstin(UPDATED_GSTIN)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .emailId(UPDATED_EMAIL_ID)
            .status(UPDATED_STATUS)
            .dateOfRegestation(UPDATED_DATE_OF_REGESTATION)
            .dateOfDeRegestation(UPDATED_DATE_OF_DE_REGESTATION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testOrganization.getOrgName()).isEqualTo(UPDATED_ORG_NAME);
        assertThat(testOrganization.getLegalOrgName()).isEqualTo(UPDATED_LEGAL_ORG_NAME);
        assertThat(testOrganization.getPan()).isEqualTo(UPDATED_PAN);
        assertThat(testOrganization.getGstin()).isEqualTo(UPDATED_GSTIN);
        assertThat(testOrganization.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrganization.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testOrganization.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrganization.getDateOfRegestation()).isEqualTo(UPDATED_DATE_OF_REGESTATION);
        assertThat(testOrganization.getDateOfDeRegestation()).isEqualTo(UPDATED_DATE_OF_DE_REGESTATION);
        assertThat(testOrganization.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testOrganization.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Delete the organization
        restOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
