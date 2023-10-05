package com.religate.gstbills.web.rest;

import static com.religate.gstbills.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.OrgUsers;
import com.religate.gstbills.domain.enumeration.Status;
import com.religate.gstbills.repository.OrgUsersRepository;
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
 * Integration tests for the {@link OrgUsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgUsersResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_PROFILE_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/org-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgUsersRepository orgUsersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgUsersMockMvc;

    private OrgUsers orgUsers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUsers createEntity(EntityManager em) {
        OrgUsers orgUsers = new OrgUsers()
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .userName(DEFAULT_USER_NAME)
            .password(DEFAULT_PASSWORD)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .profilePhoto(DEFAULT_PROFILE_PHOTO)
            .profilePhotoContentType(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE)
            .emailId(DEFAULT_EMAIL_ID)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return orgUsers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUsers createUpdatedEntity(EntityManager em) {
        OrgUsers orgUsers = new OrgUsers()
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return orgUsers;
    }

    @BeforeEach
    public void initTest() {
        orgUsers = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgUsers() throws Exception {
        int databaseSizeBeforeCreate = orgUsersRepository.findAll().size();
        // Create the OrgUsers
        restOrgUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUsers)))
            .andExpect(status().isCreated());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUsers testOrgUsers = orgUsersList.get(orgUsersList.size() - 1);
        assertThat(testOrgUsers.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testOrgUsers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrgUsers.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testOrgUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testOrgUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrgUsers.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testOrgUsers.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testOrgUsers.getProfilePhoto()).isEqualTo(DEFAULT_PROFILE_PHOTO);
        assertThat(testOrgUsers.getProfilePhotoContentType()).isEqualTo(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testOrgUsers.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testOrgUsers.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void createOrgUsersWithExistingId() throws Exception {
        // Create the OrgUsers with an existing ID
        orgUsers.setId(1L);

        int databaseSizeBeforeCreate = orgUsersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUsers)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrgUsers() throws Exception {
        // Initialize the database
        orgUsersRepository.saveAndFlush(orgUsers);

        // Get all the orgUsersList
        restOrgUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))))
            .andExpect(jsonPath("$.[*].profilePhotoContentType").value(hasItem(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PHOTO))))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getOrgUsers() throws Exception {
        // Initialize the database
        orgUsersRepository.saveAndFlush(orgUsers);

        // Get the orgUsers
        restOrgUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, orgUsers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgUsers.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.updateDate").value(sameInstant(DEFAULT_UPDATE_DATE)))
            .andExpect(jsonPath("$.profilePhotoContentType").value(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.profilePhoto").value(Base64Utils.encodeToString(DEFAULT_PROFILE_PHOTO)))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingOrgUsers() throws Exception {
        // Get the orgUsers
        restOrgUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrgUsers() throws Exception {
        // Initialize the database
        orgUsersRepository.saveAndFlush(orgUsers);

        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();

        // Update the orgUsers
        OrgUsers updatedOrgUsers = orgUsersRepository.findById(orgUsers.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrgUsers are not directly saved in db
        em.detach(updatedOrgUsers);
        updatedOrgUsers
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restOrgUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrgUsers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrgUsers))
            )
            .andExpect(status().isOk());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
        OrgUsers testOrgUsers = orgUsersList.get(orgUsersList.size() - 1);
        assertThat(testOrgUsers.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrgUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUsers.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testOrgUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testOrgUsers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrgUsers.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testOrgUsers.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testOrgUsers.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testOrgUsers.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testOrgUsers.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testOrgUsers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingOrgUsers() throws Exception {
        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();
        orgUsers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgUsers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgUsers() throws Exception {
        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();
        orgUsers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgUsers() throws Exception {
        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();
        orgUsers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUsers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgUsersWithPatch() throws Exception {
        // Initialize the database
        orgUsersRepository.saveAndFlush(orgUsers);

        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();

        // Update the orgUsers using partial update
        OrgUsers partialUpdatedOrgUsers = new OrgUsers();
        partialUpdatedOrgUsers.setId(orgUsers.getId());

        partialUpdatedOrgUsers
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .userName(UPDATED_USER_NAME)
            .updateDate(UPDATED_UPDATE_DATE)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);

        restOrgUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUsers))
            )
            .andExpect(status().isOk());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
        OrgUsers testOrgUsers = orgUsersList.get(orgUsersList.size() - 1);
        assertThat(testOrgUsers.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrgUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUsers.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testOrgUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testOrgUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrgUsers.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testOrgUsers.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testOrgUsers.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testOrgUsers.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testOrgUsers.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testOrgUsers.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateOrgUsersWithPatch() throws Exception {
        // Initialize the database
        orgUsersRepository.saveAndFlush(orgUsers);

        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();

        // Update the orgUsers using partial update
        OrgUsers partialUpdatedOrgUsers = new OrgUsers();
        partialUpdatedOrgUsers.setId(orgUsers.getId());

        partialUpdatedOrgUsers
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restOrgUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUsers))
            )
            .andExpect(status().isOk());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
        OrgUsers testOrgUsers = orgUsersList.get(orgUsersList.size() - 1);
        assertThat(testOrgUsers.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrgUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUsers.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testOrgUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testOrgUsers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrgUsers.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testOrgUsers.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testOrgUsers.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testOrgUsers.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testOrgUsers.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testOrgUsers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingOrgUsers() throws Exception {
        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();
        orgUsers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgUsers() throws Exception {
        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();
        orgUsers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgUsers() throws Exception {
        int databaseSizeBeforeUpdate = orgUsersRepository.findAll().size();
        orgUsers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUsersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orgUsers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUsers in the database
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrgUsers() throws Exception {
        // Initialize the database
        orgUsersRepository.saveAndFlush(orgUsers);

        int databaseSizeBeforeDelete = orgUsersRepository.findAll().size();

        // Delete the orgUsers
        restOrgUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgUsers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgUsers> orgUsersList = orgUsersRepository.findAll();
        assertThat(orgUsersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
