package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.OrgUserRole;
import com.religate.gstbills.repository.OrgUserRoleRepository;
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
 * Integration tests for the {@link OrgUserRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgUserRoleResourceIT {

    private static final String DEFAULT_ROLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ORG_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORG_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/org-user-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgUserRoleRepository orgUserRoleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgUserRoleMockMvc;

    private OrgUserRole orgUserRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUserRole createEntity(EntityManager em) {
        OrgUserRole orgUserRole = new OrgUserRole().roleId(DEFAULT_ROLE_ID).orgId(DEFAULT_ORG_ID).name(DEFAULT_NAME);
        return orgUserRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUserRole createUpdatedEntity(EntityManager em) {
        OrgUserRole orgUserRole = new OrgUserRole().roleId(UPDATED_ROLE_ID).orgId(UPDATED_ORG_ID).name(UPDATED_NAME);
        return orgUserRole;
    }

    @BeforeEach
    public void initTest() {
        orgUserRole = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgUserRole() throws Exception {
        int databaseSizeBeforeCreate = orgUserRoleRepository.findAll().size();
        // Create the OrgUserRole
        restOrgUserRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserRole)))
            .andExpect(status().isCreated());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUserRole testOrgUserRole = orgUserRoleList.get(orgUserRoleList.size() - 1);
        assertThat(testOrgUserRole.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testOrgUserRole.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testOrgUserRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOrgUserRoleWithExistingId() throws Exception {
        // Create the OrgUserRole with an existing ID
        orgUserRole.setId(1L);

        int databaseSizeBeforeCreate = orgUserRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUserRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserRole)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrgUserRoles() throws Exception {
        // Initialize the database
        orgUserRoleRepository.saveAndFlush(orgUserRole);

        // Get all the orgUserRoleList
        restOrgUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUserRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].orgId").value(hasItem(DEFAULT_ORG_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOrgUserRole() throws Exception {
        // Initialize the database
        orgUserRoleRepository.saveAndFlush(orgUserRole);

        // Get the orgUserRole
        restOrgUserRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, orgUserRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgUserRole.getId().intValue()))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID))
            .andExpect(jsonPath("$.orgId").value(DEFAULT_ORG_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOrgUserRole() throws Exception {
        // Get the orgUserRole
        restOrgUserRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrgUserRole() throws Exception {
        // Initialize the database
        orgUserRoleRepository.saveAndFlush(orgUserRole);

        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();

        // Update the orgUserRole
        OrgUserRole updatedOrgUserRole = orgUserRoleRepository.findById(orgUserRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrgUserRole are not directly saved in db
        em.detach(updatedOrgUserRole);
        updatedOrgUserRole.roleId(UPDATED_ROLE_ID).orgId(UPDATED_ORG_ID).name(UPDATED_NAME);

        restOrgUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrgUserRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrgUserRole))
            )
            .andExpect(status().isOk());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
        OrgUserRole testOrgUserRole = orgUserRoleList.get(orgUserRoleList.size() - 1);
        assertThat(testOrgUserRole.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testOrgUserRole.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testOrgUserRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOrgUserRole() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();
        orgUserRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgUserRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUserRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgUserRole() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();
        orgUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUserRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgUserRole() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();
        orgUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgUserRoleWithPatch() throws Exception {
        // Initialize the database
        orgUserRoleRepository.saveAndFlush(orgUserRole);

        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();

        // Update the orgUserRole using partial update
        OrgUserRole partialUpdatedOrgUserRole = new OrgUserRole();
        partialUpdatedOrgUserRole.setId(orgUserRole.getId());

        partialUpdatedOrgUserRole.roleId(UPDATED_ROLE_ID);

        restOrgUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUserRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUserRole))
            )
            .andExpect(status().isOk());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
        OrgUserRole testOrgUserRole = orgUserRoleList.get(orgUserRoleList.size() - 1);
        assertThat(testOrgUserRole.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testOrgUserRole.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testOrgUserRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOrgUserRoleWithPatch() throws Exception {
        // Initialize the database
        orgUserRoleRepository.saveAndFlush(orgUserRole);

        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();

        // Update the orgUserRole using partial update
        OrgUserRole partialUpdatedOrgUserRole = new OrgUserRole();
        partialUpdatedOrgUserRole.setId(orgUserRole.getId());

        partialUpdatedOrgUserRole.roleId(UPDATED_ROLE_ID).orgId(UPDATED_ORG_ID).name(UPDATED_NAME);

        restOrgUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUserRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUserRole))
            )
            .andExpect(status().isOk());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
        OrgUserRole testOrgUserRole = orgUserRoleList.get(orgUserRoleList.size() - 1);
        assertThat(testOrgUserRole.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testOrgUserRole.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testOrgUserRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOrgUserRole() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();
        orgUserRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgUserRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUserRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgUserRole() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();
        orgUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUserRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgUserRole() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRoleRepository.findAll().size();
        orgUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orgUserRole))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUserRole in the database
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrgUserRole() throws Exception {
        // Initialize the database
        orgUserRoleRepository.saveAndFlush(orgUserRole);

        int databaseSizeBeforeDelete = orgUserRoleRepository.findAll().size();

        // Delete the orgUserRole
        restOrgUserRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgUserRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgUserRole> orgUserRoleList = orgUserRoleRepository.findAll();
        assertThat(orgUserRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
