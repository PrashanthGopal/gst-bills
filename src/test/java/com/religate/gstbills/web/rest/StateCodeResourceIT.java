package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.StateCode;
import com.religate.gstbills.repository.StateCodeRepository;
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
 * Integration tests for the {@link StateCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StateCodeResourceIT {

    private static final String DEFAULT_STATE_CODE_ID = "AAAAAAAAAA";
    private static final String UPDATED_STATE_CODE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/state-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StateCodeRepository stateCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateCodeMockMvc;

    private StateCode stateCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateCode createEntity(EntityManager em) {
        StateCode stateCode = new StateCode().stateCodeId(DEFAULT_STATE_CODE_ID).code(DEFAULT_CODE).name(DEFAULT_NAME);
        return stateCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateCode createUpdatedEntity(EntityManager em) {
        StateCode stateCode = new StateCode().stateCodeId(UPDATED_STATE_CODE_ID).code(UPDATED_CODE).name(UPDATED_NAME);
        return stateCode;
    }

    @BeforeEach
    public void initTest() {
        stateCode = createEntity(em);
    }

    @Test
    @Transactional
    void createStateCode() throws Exception {
        int databaseSizeBeforeCreate = stateCodeRepository.findAll().size();
        // Create the StateCode
        restStateCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateCode)))
            .andExpect(status().isCreated());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeCreate + 1);
        StateCode testStateCode = stateCodeList.get(stateCodeList.size() - 1);
        assertThat(testStateCode.getStateCodeId()).isEqualTo(DEFAULT_STATE_CODE_ID);
        assertThat(testStateCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStateCode.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createStateCodeWithExistingId() throws Exception {
        // Create the StateCode with an existing ID
        stateCode.setId(1L);

        int databaseSizeBeforeCreate = stateCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateCode)))
            .andExpect(status().isBadRequest());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStateCodes() throws Exception {
        // Initialize the database
        stateCodeRepository.saveAndFlush(stateCode);

        // Get all the stateCodeList
        restStateCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].stateCodeId").value(hasItem(DEFAULT_STATE_CODE_ID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getStateCode() throws Exception {
        // Initialize the database
        stateCodeRepository.saveAndFlush(stateCode);

        // Get the stateCode
        restStateCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, stateCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stateCode.getId().intValue()))
            .andExpect(jsonPath("$.stateCodeId").value(DEFAULT_STATE_CODE_ID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStateCode() throws Exception {
        // Get the stateCode
        restStateCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStateCode() throws Exception {
        // Initialize the database
        stateCodeRepository.saveAndFlush(stateCode);

        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();

        // Update the stateCode
        StateCode updatedStateCode = stateCodeRepository.findById(stateCode.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStateCode are not directly saved in db
        em.detach(updatedStateCode);
        updatedStateCode.stateCodeId(UPDATED_STATE_CODE_ID).code(UPDATED_CODE).name(UPDATED_NAME);

        restStateCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStateCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStateCode))
            )
            .andExpect(status().isOk());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
        StateCode testStateCode = stateCodeList.get(stateCodeList.size() - 1);
        assertThat(testStateCode.getStateCodeId()).isEqualTo(UPDATED_STATE_CODE_ID);
        assertThat(testStateCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStateCode.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingStateCode() throws Exception {
        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();
        stateCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStateCode() throws Exception {
        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();
        stateCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStateCode() throws Exception {
        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();
        stateCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStateCodeWithPatch() throws Exception {
        // Initialize the database
        stateCodeRepository.saveAndFlush(stateCode);

        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();

        // Update the stateCode using partial update
        StateCode partialUpdatedStateCode = new StateCode();
        partialUpdatedStateCode.setId(stateCode.getId());

        partialUpdatedStateCode.code(UPDATED_CODE);

        restStateCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStateCode))
            )
            .andExpect(status().isOk());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
        StateCode testStateCode = stateCodeList.get(stateCodeList.size() - 1);
        assertThat(testStateCode.getStateCodeId()).isEqualTo(DEFAULT_STATE_CODE_ID);
        assertThat(testStateCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStateCode.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateStateCodeWithPatch() throws Exception {
        // Initialize the database
        stateCodeRepository.saveAndFlush(stateCode);

        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();

        // Update the stateCode using partial update
        StateCode partialUpdatedStateCode = new StateCode();
        partialUpdatedStateCode.setId(stateCode.getId());

        partialUpdatedStateCode.stateCodeId(UPDATED_STATE_CODE_ID).code(UPDATED_CODE).name(UPDATED_NAME);

        restStateCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStateCode))
            )
            .andExpect(status().isOk());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
        StateCode testStateCode = stateCodeList.get(stateCodeList.size() - 1);
        assertThat(testStateCode.getStateCodeId()).isEqualTo(UPDATED_STATE_CODE_ID);
        assertThat(testStateCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStateCode.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingStateCode() throws Exception {
        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();
        stateCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStateCode() throws Exception {
        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();
        stateCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStateCode() throws Exception {
        int databaseSizeBeforeUpdate = stateCodeRepository.findAll().size();
        stateCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateCodeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stateCode))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateCode in the database
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStateCode() throws Exception {
        // Initialize the database
        stateCodeRepository.saveAndFlush(stateCode);

        int databaseSizeBeforeDelete = stateCodeRepository.findAll().size();

        // Delete the stateCode
        restStateCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, stateCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StateCode> stateCodeList = stateCodeRepository.findAll();
        assertThat(stateCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
