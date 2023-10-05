package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.InvoiceItems;
import com.religate.gstbills.repository.InvoiceItemsRepository;
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
 * Integration tests for the {@link InvoiceItemsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemsResourceIT {

    private static final String DEFAULT_ORG_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORG_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_ITEMS_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ITEMS_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_CGST = "AAAAAAAAAA";
    private static final String UPDATED_CGST = "BBBBBBBBBB";

    private static final String DEFAULT_SGST = "AAAAAAAAAA";
    private static final String UPDATED_SGST = "BBBBBBBBBB";

    private static final String DEFAULT_GST = "AAAAAAAAAA";
    private static final String UPDATED_GST = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/invoice-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceItemsRepository invoiceItemsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceItemsMockMvc;

    private InvoiceItems invoiceItems;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItems createEntity(EntityManager em) {
        InvoiceItems invoiceItems = new InvoiceItems()
            .orgId(DEFAULT_ORG_ID)
            .invoiceItemsId(DEFAULT_INVOICE_ITEMS_ID)
            .quantity(DEFAULT_QUANTITY)
            .cgst(DEFAULT_CGST)
            .sgst(DEFAULT_SGST)
            .gst(DEFAULT_GST)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
        return invoiceItems;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItems createUpdatedEntity(EntityManager em) {
        InvoiceItems invoiceItems = new InvoiceItems()
            .orgId(UPDATED_ORG_ID)
            .invoiceItemsId(UPDATED_INVOICE_ITEMS_ID)
            .quantity(UPDATED_QUANTITY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .gst(UPDATED_GST)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
        return invoiceItems;
    }

    @BeforeEach
    public void initTest() {
        invoiceItems = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceItems() throws Exception {
        int databaseSizeBeforeCreate = invoiceItemsRepository.findAll().size();
        // Create the InvoiceItems
        restInvoiceItemsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItems)))
            .andExpect(status().isCreated());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceItems testInvoiceItems = invoiceItemsList.get(invoiceItemsList.size() - 1);
        assertThat(testInvoiceItems.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testInvoiceItems.getInvoiceItemsId()).isEqualTo(DEFAULT_INVOICE_ITEMS_ID);
        assertThat(testInvoiceItems.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceItems.getCgst()).isEqualTo(DEFAULT_CGST);
        assertThat(testInvoiceItems.getSgst()).isEqualTo(DEFAULT_SGST);
        assertThat(testInvoiceItems.getGst()).isEqualTo(DEFAULT_GST);
        assertThat(testInvoiceItems.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void createInvoiceItemsWithExistingId() throws Exception {
        // Create the InvoiceItems with an existing ID
        invoiceItems.setId(1L);

        int databaseSizeBeforeCreate = invoiceItemsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItems)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemsRepository.saveAndFlush(invoiceItems);

        // Get all the invoiceItemsList
        restInvoiceItemsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgId").value(hasItem(DEFAULT_ORG_ID)))
            .andExpect(jsonPath("$.[*].invoiceItemsId").value(hasItem(DEFAULT_INVOICE_ITEMS_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(DEFAULT_CGST)))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(DEFAULT_SGST)))
            .andExpect(jsonPath("$.[*].gst").value(hasItem(DEFAULT_GST)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemsRepository.saveAndFlush(invoiceItems);

        // Get the invoiceItems
        restInvoiceItemsMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItems.getId().intValue()))
            .andExpect(jsonPath("$.orgId").value(DEFAULT_ORG_ID))
            .andExpect(jsonPath("$.invoiceItemsId").value(DEFAULT_INVOICE_ITEMS_ID))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.cgst").value(DEFAULT_CGST))
            .andExpect(jsonPath("$.sgst").value(DEFAULT_SGST))
            .andExpect(jsonPath("$.gst").value(DEFAULT_GST))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceItems() throws Exception {
        // Get the invoiceItems
        restInvoiceItemsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemsRepository.saveAndFlush(invoiceItems);

        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();

        // Update the invoiceItems
        InvoiceItems updatedInvoiceItems = invoiceItemsRepository.findById(invoiceItems.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceItems are not directly saved in db
        em.detach(updatedInvoiceItems);
        updatedInvoiceItems
            .orgId(UPDATED_ORG_ID)
            .invoiceItemsId(UPDATED_INVOICE_ITEMS_ID)
            .quantity(UPDATED_QUANTITY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .gst(UPDATED_GST)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceItems.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceItems))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItems testInvoiceItems = invoiceItemsList.get(invoiceItemsList.size() - 1);
        assertThat(testInvoiceItems.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testInvoiceItems.getInvoiceItemsId()).isEqualTo(UPDATED_INVOICE_ITEMS_ID);
        assertThat(testInvoiceItems.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceItems.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testInvoiceItems.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testInvoiceItems.getGst()).isEqualTo(UPDATED_GST);
        assertThat(testInvoiceItems.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceItems() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();
        invoiceItems.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItems.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceItems() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();
        invoiceItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceItems() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();
        invoiceItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItems)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceItemsWithPatch() throws Exception {
        // Initialize the database
        invoiceItemsRepository.saveAndFlush(invoiceItems);

        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();

        // Update the invoiceItems using partial update
        InvoiceItems partialUpdatedInvoiceItems = new InvoiceItems();
        partialUpdatedInvoiceItems.setId(invoiceItems.getId());

        partialUpdatedInvoiceItems.cgst(UPDATED_CGST).sgst(UPDATED_SGST);

        restInvoiceItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItems))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItems testInvoiceItems = invoiceItemsList.get(invoiceItemsList.size() - 1);
        assertThat(testInvoiceItems.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testInvoiceItems.getInvoiceItemsId()).isEqualTo(DEFAULT_INVOICE_ITEMS_ID);
        assertThat(testInvoiceItems.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceItems.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testInvoiceItems.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testInvoiceItems.getGst()).isEqualTo(DEFAULT_GST);
        assertThat(testInvoiceItems.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceItemsWithPatch() throws Exception {
        // Initialize the database
        invoiceItemsRepository.saveAndFlush(invoiceItems);

        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();

        // Update the invoiceItems using partial update
        InvoiceItems partialUpdatedInvoiceItems = new InvoiceItems();
        partialUpdatedInvoiceItems.setId(invoiceItems.getId());

        partialUpdatedInvoiceItems
            .orgId(UPDATED_ORG_ID)
            .invoiceItemsId(UPDATED_INVOICE_ITEMS_ID)
            .quantity(UPDATED_QUANTITY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .gst(UPDATED_GST)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItems))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItems testInvoiceItems = invoiceItemsList.get(invoiceItemsList.size() - 1);
        assertThat(testInvoiceItems.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testInvoiceItems.getInvoiceItemsId()).isEqualTo(UPDATED_INVOICE_ITEMS_ID);
        assertThat(testInvoiceItems.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceItems.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testInvoiceItems.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testInvoiceItems.getGst()).isEqualTo(UPDATED_GST);
        assertThat(testInvoiceItems.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceItems() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();
        invoiceItems.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceItems() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();
        invoiceItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceItems() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemsRepository.findAll().size();
        invoiceItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceItems))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItems in the database
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemsRepository.saveAndFlush(invoiceItems);

        int databaseSizeBeforeDelete = invoiceItemsRepository.findAll().size();

        // Delete the invoiceItems
        restInvoiceItemsMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItems.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceItems> invoiceItemsList = invoiceItemsRepository.findAll();
        assertThat(invoiceItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
