package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.Products;
import com.religate.gstbills.repository.ProductsRepository;
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
 * Integration tests for the {@link ProductsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductsResourceIT {

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_HSN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_HSN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_TAX_RATE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TAX_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_CODE_ID = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_CODE_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_COST_PER_QTY = 1D;
    private static final Double UPDATED_COST_PER_QTY = 2D;

    private static final String DEFAULT_CGST = "AAAAAAAAAA";
    private static final String UPDATED_CGST = "BBBBBBBBBB";

    private static final String DEFAULT_SGST = "AAAAAAAAAA";
    private static final String UPDATED_SGST = "BBBBBBBBBB";

    private static final String DEFAULT_IGST = "AAAAAAAAAA";
    private static final String UPDATED_IGST = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_TAX_RATE = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_TAX_RATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsMockMvc;

    private Products products;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createEntity(EntityManager em) {
        Products products = new Products()
            .productId(DEFAULT_PRODUCT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .productHsnCode(DEFAULT_PRODUCT_HSN_CODE)
            .productTaxRate(DEFAULT_PRODUCT_TAX_RATE)
            .unitCodeId(DEFAULT_UNIT_CODE_ID)
            .costPerQty(DEFAULT_COST_PER_QTY)
            .cgst(DEFAULT_CGST)
            .sgst(DEFAULT_SGST)
            .igst(DEFAULT_IGST)
            .totalTaxRate(DEFAULT_TOTAL_TAX_RATE);
        return products;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createUpdatedEntity(EntityManager em) {
        Products products = new Products()
            .productId(UPDATED_PRODUCT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productHsnCode(UPDATED_PRODUCT_HSN_CODE)
            .productTaxRate(UPDATED_PRODUCT_TAX_RATE)
            .unitCodeId(UPDATED_UNIT_CODE_ID)
            .costPerQty(UPDATED_COST_PER_QTY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .igst(UPDATED_IGST)
            .totalTaxRate(UPDATED_TOTAL_TAX_RATE);
        return products;
    }

    @BeforeEach
    public void initTest() {
        products = createEntity(em);
    }

    @Test
    @Transactional
    void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();
        // Create the Products
        restProductsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProducts.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProducts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProducts.getProductHsnCode()).isEqualTo(DEFAULT_PRODUCT_HSN_CODE);
        assertThat(testProducts.getProductTaxRate()).isEqualTo(DEFAULT_PRODUCT_TAX_RATE);
        assertThat(testProducts.getUnitCodeId()).isEqualTo(DEFAULT_UNIT_CODE_ID);
        assertThat(testProducts.getCostPerQty()).isEqualTo(DEFAULT_COST_PER_QTY);
        assertThat(testProducts.getCgst()).isEqualTo(DEFAULT_CGST);
        assertThat(testProducts.getSgst()).isEqualTo(DEFAULT_SGST);
        assertThat(testProducts.getIgst()).isEqualTo(DEFAULT_IGST);
        assertThat(testProducts.getTotalTaxRate()).isEqualTo(DEFAULT_TOTAL_TAX_RATE);
    }

    @Test
    @Transactional
    void createProductsWithExistingId() throws Exception {
        // Create the Products with an existing ID
        products.setId(1L);

        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList
        restProductsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productHsnCode").value(hasItem(DEFAULT_PRODUCT_HSN_CODE)))
            .andExpect(jsonPath("$.[*].productTaxRate").value(hasItem(DEFAULT_PRODUCT_TAX_RATE)))
            .andExpect(jsonPath("$.[*].unitCodeId").value(hasItem(DEFAULT_UNIT_CODE_ID)))
            .andExpect(jsonPath("$.[*].costPerQty").value(hasItem(DEFAULT_COST_PER_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(DEFAULT_CGST)))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(DEFAULT_SGST)))
            .andExpect(jsonPath("$.[*].igst").value(hasItem(DEFAULT_IGST)))
            .andExpect(jsonPath("$.[*].totalTaxRate").value(hasItem(DEFAULT_TOTAL_TAX_RATE)));
    }

    @Test
    @Transactional
    void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc
            .perform(get(ENTITY_API_URL_ID, products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.productHsnCode").value(DEFAULT_PRODUCT_HSN_CODE))
            .andExpect(jsonPath("$.productTaxRate").value(DEFAULT_PRODUCT_TAX_RATE))
            .andExpect(jsonPath("$.unitCodeId").value(DEFAULT_UNIT_CODE_ID))
            .andExpect(jsonPath("$.costPerQty").value(DEFAULT_COST_PER_QTY.doubleValue()))
            .andExpect(jsonPath("$.cgst").value(DEFAULT_CGST))
            .andExpect(jsonPath("$.sgst").value(DEFAULT_SGST))
            .andExpect(jsonPath("$.igst").value(DEFAULT_IGST))
            .andExpect(jsonPath("$.totalTaxRate").value(DEFAULT_TOTAL_TAX_RATE));
    }

    @Test
    @Transactional
    void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        Products updatedProducts = productsRepository.findById(products.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProducts are not directly saved in db
        em.detach(updatedProducts);
        updatedProducts
            .productId(UPDATED_PRODUCT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productHsnCode(UPDATED_PRODUCT_HSN_CODE)
            .productTaxRate(UPDATED_PRODUCT_TAX_RATE)
            .unitCodeId(UPDATED_UNIT_CODE_ID)
            .costPerQty(UPDATED_COST_PER_QTY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .igst(UPDATED_IGST)
            .totalTaxRate(UPDATED_TOTAL_TAX_RATE);

        restProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProducts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProducts))
            )
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProducts.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProducts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProducts.getProductHsnCode()).isEqualTo(UPDATED_PRODUCT_HSN_CODE);
        assertThat(testProducts.getProductTaxRate()).isEqualTo(UPDATED_PRODUCT_TAX_RATE);
        assertThat(testProducts.getUnitCodeId()).isEqualTo(UPDATED_UNIT_CODE_ID);
        assertThat(testProducts.getCostPerQty()).isEqualTo(UPDATED_COST_PER_QTY);
        assertThat(testProducts.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testProducts.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testProducts.getIgst()).isEqualTo(UPDATED_IGST);
        assertThat(testProducts.getTotalTaxRate()).isEqualTo(UPDATED_TOTAL_TAX_RATE);
    }

    @Test
    @Transactional
    void putNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, products.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(products))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(products))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductsWithPatch() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products using partial update
        Products partialUpdatedProducts = new Products();
        partialUpdatedProducts.setId(products.getId());

        partialUpdatedProducts
            .productId(UPDATED_PRODUCT_ID)
            .costPerQty(UPDATED_COST_PER_QTY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .igst(UPDATED_IGST)
            .totalTaxRate(UPDATED_TOTAL_TAX_RATE);

        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducts))
            )
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProducts.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProducts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProducts.getProductHsnCode()).isEqualTo(DEFAULT_PRODUCT_HSN_CODE);
        assertThat(testProducts.getProductTaxRate()).isEqualTo(DEFAULT_PRODUCT_TAX_RATE);
        assertThat(testProducts.getUnitCodeId()).isEqualTo(DEFAULT_UNIT_CODE_ID);
        assertThat(testProducts.getCostPerQty()).isEqualTo(UPDATED_COST_PER_QTY);
        assertThat(testProducts.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testProducts.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testProducts.getIgst()).isEqualTo(UPDATED_IGST);
        assertThat(testProducts.getTotalTaxRate()).isEqualTo(UPDATED_TOTAL_TAX_RATE);
    }

    @Test
    @Transactional
    void fullUpdateProductsWithPatch() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products using partial update
        Products partialUpdatedProducts = new Products();
        partialUpdatedProducts.setId(products.getId());

        partialUpdatedProducts
            .productId(UPDATED_PRODUCT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productHsnCode(UPDATED_PRODUCT_HSN_CODE)
            .productTaxRate(UPDATED_PRODUCT_TAX_RATE)
            .unitCodeId(UPDATED_UNIT_CODE_ID)
            .costPerQty(UPDATED_COST_PER_QTY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .igst(UPDATED_IGST)
            .totalTaxRate(UPDATED_TOTAL_TAX_RATE);

        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducts))
            )
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProducts.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProducts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProducts.getProductHsnCode()).isEqualTo(UPDATED_PRODUCT_HSN_CODE);
        assertThat(testProducts.getProductTaxRate()).isEqualTo(UPDATED_PRODUCT_TAX_RATE);
        assertThat(testProducts.getUnitCodeId()).isEqualTo(UPDATED_UNIT_CODE_ID);
        assertThat(testProducts.getCostPerQty()).isEqualTo(UPDATED_COST_PER_QTY);
        assertThat(testProducts.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testProducts.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testProducts.getIgst()).isEqualTo(UPDATED_IGST);
        assertThat(testProducts.getTotalTaxRate()).isEqualTo(UPDATED_TOTAL_TAX_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, products.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(products))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(products))
            )
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();
        products.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(products)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Delete the products
        restProductsMockMvc
            .perform(delete(ENTITY_API_URL_ID, products.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
