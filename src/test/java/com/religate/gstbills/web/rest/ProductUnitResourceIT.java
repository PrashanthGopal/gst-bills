package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.ProductUnit;
import com.religate.gstbills.repository.ProductUnitRepository;
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
 * Integration tests for the {@link ProductUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductUnitResourceIT {

    private static final String DEFAULT_UNIT_CODE_ID = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_CODE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_CODE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_CODE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductUnitRepository productUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductUnitMockMvc;

    private ProductUnit productUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductUnit createEntity(EntityManager em) {
        ProductUnit productUnit = new ProductUnit()
            .unitCodeId(DEFAULT_UNIT_CODE_ID)
            .unitCode(DEFAULT_UNIT_CODE)
            .unitCodeDescription(DEFAULT_UNIT_CODE_DESCRIPTION);
        return productUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductUnit createUpdatedEntity(EntityManager em) {
        ProductUnit productUnit = new ProductUnit()
            .unitCodeId(UPDATED_UNIT_CODE_ID)
            .unitCode(UPDATED_UNIT_CODE)
            .unitCodeDescription(UPDATED_UNIT_CODE_DESCRIPTION);
        return productUnit;
    }

    @BeforeEach
    public void initTest() {
        productUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createProductUnit() throws Exception {
        int databaseSizeBeforeCreate = productUnitRepository.findAll().size();
        // Create the ProductUnit
        restProductUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productUnit)))
            .andExpect(status().isCreated());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeCreate + 1);
        ProductUnit testProductUnit = productUnitList.get(productUnitList.size() - 1);
        assertThat(testProductUnit.getUnitCodeId()).isEqualTo(DEFAULT_UNIT_CODE_ID);
        assertThat(testProductUnit.getUnitCode()).isEqualTo(DEFAULT_UNIT_CODE);
        assertThat(testProductUnit.getUnitCodeDescription()).isEqualTo(DEFAULT_UNIT_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProductUnitWithExistingId() throws Exception {
        // Create the ProductUnit with an existing ID
        productUnit.setId(1L);

        int databaseSizeBeforeCreate = productUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productUnit)))
            .andExpect(status().isBadRequest());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductUnits() throws Exception {
        // Initialize the database
        productUnitRepository.saveAndFlush(productUnit);

        // Get all the productUnitList
        restProductUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitCodeId").value(hasItem(DEFAULT_UNIT_CODE_ID)))
            .andExpect(jsonPath("$.[*].unitCode").value(hasItem(DEFAULT_UNIT_CODE)))
            .andExpect(jsonPath("$.[*].unitCodeDescription").value(hasItem(DEFAULT_UNIT_CODE_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductUnit() throws Exception {
        // Initialize the database
        productUnitRepository.saveAndFlush(productUnit);

        // Get the productUnit
        restProductUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, productUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productUnit.getId().intValue()))
            .andExpect(jsonPath("$.unitCodeId").value(DEFAULT_UNIT_CODE_ID))
            .andExpect(jsonPath("$.unitCode").value(DEFAULT_UNIT_CODE))
            .andExpect(jsonPath("$.unitCodeDescription").value(DEFAULT_UNIT_CODE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingProductUnit() throws Exception {
        // Get the productUnit
        restProductUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductUnit() throws Exception {
        // Initialize the database
        productUnitRepository.saveAndFlush(productUnit);

        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();

        // Update the productUnit
        ProductUnit updatedProductUnit = productUnitRepository.findById(productUnit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductUnit are not directly saved in db
        em.detach(updatedProductUnit);
        updatedProductUnit.unitCodeId(UPDATED_UNIT_CODE_ID).unitCode(UPDATED_UNIT_CODE).unitCodeDescription(UPDATED_UNIT_CODE_DESCRIPTION);

        restProductUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductUnit))
            )
            .andExpect(status().isOk());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
        ProductUnit testProductUnit = productUnitList.get(productUnitList.size() - 1);
        assertThat(testProductUnit.getUnitCodeId()).isEqualTo(UPDATED_UNIT_CODE_ID);
        assertThat(testProductUnit.getUnitCode()).isEqualTo(UPDATED_UNIT_CODE);
        assertThat(testProductUnit.getUnitCodeDescription()).isEqualTo(UPDATED_UNIT_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProductUnit() throws Exception {
        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();
        productUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductUnit() throws Exception {
        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();
        productUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductUnit() throws Exception {
        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();
        productUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductUnitWithPatch() throws Exception {
        // Initialize the database
        productUnitRepository.saveAndFlush(productUnit);

        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();

        // Update the productUnit using partial update
        ProductUnit partialUpdatedProductUnit = new ProductUnit();
        partialUpdatedProductUnit.setId(productUnit.getId());

        partialUpdatedProductUnit
            .unitCodeId(UPDATED_UNIT_CODE_ID)
            .unitCode(UPDATED_UNIT_CODE)
            .unitCodeDescription(UPDATED_UNIT_CODE_DESCRIPTION);

        restProductUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductUnit))
            )
            .andExpect(status().isOk());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
        ProductUnit testProductUnit = productUnitList.get(productUnitList.size() - 1);
        assertThat(testProductUnit.getUnitCodeId()).isEqualTo(UPDATED_UNIT_CODE_ID);
        assertThat(testProductUnit.getUnitCode()).isEqualTo(UPDATED_UNIT_CODE);
        assertThat(testProductUnit.getUnitCodeDescription()).isEqualTo(UPDATED_UNIT_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProductUnitWithPatch() throws Exception {
        // Initialize the database
        productUnitRepository.saveAndFlush(productUnit);

        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();

        // Update the productUnit using partial update
        ProductUnit partialUpdatedProductUnit = new ProductUnit();
        partialUpdatedProductUnit.setId(productUnit.getId());

        partialUpdatedProductUnit
            .unitCodeId(UPDATED_UNIT_CODE_ID)
            .unitCode(UPDATED_UNIT_CODE)
            .unitCodeDescription(UPDATED_UNIT_CODE_DESCRIPTION);

        restProductUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductUnit))
            )
            .andExpect(status().isOk());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
        ProductUnit testProductUnit = productUnitList.get(productUnitList.size() - 1);
        assertThat(testProductUnit.getUnitCodeId()).isEqualTo(UPDATED_UNIT_CODE_ID);
        assertThat(testProductUnit.getUnitCode()).isEqualTo(UPDATED_UNIT_CODE);
        assertThat(testProductUnit.getUnitCodeDescription()).isEqualTo(UPDATED_UNIT_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProductUnit() throws Exception {
        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();
        productUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductUnit() throws Exception {
        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();
        productUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductUnit() throws Exception {
        int databaseSizeBeforeUpdate = productUnitRepository.findAll().size();
        productUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductUnit in the database
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductUnit() throws Exception {
        // Initialize the database
        productUnitRepository.saveAndFlush(productUnit);

        int databaseSizeBeforeDelete = productUnitRepository.findAll().size();

        // Delete the productUnit
        restProductUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, productUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductUnit> productUnitList = productUnitRepository.findAll();
        assertThat(productUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
