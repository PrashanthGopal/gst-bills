package com.religate.gstbills.web.rest;

import static com.religate.gstbills.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.Invoice;
import com.religate.gstbills.domain.enumeration.InvoiceStatus;
import com.religate.gstbills.repository.InvoiceRepository;
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

/**
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final String DEFAULT_INVOICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BUYER_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_BUYER_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_ITEMS_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ITEMS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SHIPPING_ADDRESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_ADDRESS_ID = "BBBBBBBBBB";

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.DRAFT;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.ISSUED;

    private static final ZonedDateTime DEFAULT_CREATE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_IGST_RATE = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_IGST_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_CGST_RATE = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_CGST_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_SGST_RATE = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_SGST_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_TAX_RATE = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_TAX_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_ASS_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_ASS_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_NO_ITEMS = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_NO_ITEMS = "BBBBBBBBBB";

    private static final Double DEFAULT_GRAND_TOTAL = 1D;
    private static final Double UPDATED_GRAND_TOTAL = 2D;

    private static final Double DEFAULT_DISTANCE = 1D;
    private static final Double UPDATED_DISTANCE = 2D;

    private static final String DEFAULT_MODE_OF_TRANSACTION = "AAAAAAAAAA";
    private static final String UPDATED_MODE_OF_TRANSACTION = "BBBBBBBBBB";

    private static final String DEFAULT_TRANS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICLE_NO = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_NO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceId(DEFAULT_INVOICE_ID)
            .supplierClientId(DEFAULT_SUPPLIER_CLIENT_ID)
            .buyerClientId(DEFAULT_BUYER_CLIENT_ID)
            .invoiceItemsId(DEFAULT_INVOICE_ITEMS_ID)
            .shippingAddressId(DEFAULT_SHIPPING_ADDRESS_ID)
            .status(DEFAULT_STATUS)
            .createDateTime(DEFAULT_CREATE_DATE_TIME)
            .updateDateTime(DEFAULT_UPDATE_DATE_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY)
            .totalIgstRate(DEFAULT_TOTAL_IGST_RATE)
            .totalCgstRate(DEFAULT_TOTAL_CGST_RATE)
            .totalSgstRate(DEFAULT_TOTAL_SGST_RATE)
            .totalTaxRate(DEFAULT_TOTAL_TAX_RATE)
            .totalAssAmount(DEFAULT_TOTAL_ASS_AMOUNT)
            .totalNoItems(DEFAULT_TOTAL_NO_ITEMS)
            .grandTotal(DEFAULT_GRAND_TOTAL)
            .distance(DEFAULT_DISTANCE)
            .modeOfTransaction(DEFAULT_MODE_OF_TRANSACTION)
            .transType(DEFAULT_TRANS_TYPE)
            .vehicleNo(DEFAULT_VEHICLE_NO);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceId(UPDATED_INVOICE_ID)
            .supplierClientId(UPDATED_SUPPLIER_CLIENT_ID)
            .buyerClientId(UPDATED_BUYER_CLIENT_ID)
            .invoiceItemsId(UPDATED_INVOICE_ITEMS_ID)
            .shippingAddressId(UPDATED_SHIPPING_ADDRESS_ID)
            .status(UPDATED_STATUS)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .totalIgstRate(UPDATED_TOTAL_IGST_RATE)
            .totalCgstRate(UPDATED_TOTAL_CGST_RATE)
            .totalSgstRate(UPDATED_TOTAL_SGST_RATE)
            .totalTaxRate(UPDATED_TOTAL_TAX_RATE)
            .totalAssAmount(UPDATED_TOTAL_ASS_AMOUNT)
            .totalNoItems(UPDATED_TOTAL_NO_ITEMS)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .distance(UPDATED_DISTANCE)
            .modeOfTransaction(UPDATED_MODE_OF_TRANSACTION)
            .transType(UPDATED_TRANS_TYPE)
            .vehicleNo(UPDATED_VEHICLE_NO);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testInvoice.getSupplierClientId()).isEqualTo(DEFAULT_SUPPLIER_CLIENT_ID);
        assertThat(testInvoice.getBuyerClientId()).isEqualTo(DEFAULT_BUYER_CLIENT_ID);
        assertThat(testInvoice.getInvoiceItemsId()).isEqualTo(DEFAULT_INVOICE_ITEMS_ID);
        assertThat(testInvoice.getShippingAddressId()).isEqualTo(DEFAULT_SHIPPING_ADDRESS_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoice.getCreateDateTime()).isEqualTo(DEFAULT_CREATE_DATE_TIME);
        assertThat(testInvoice.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
        assertThat(testInvoice.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInvoice.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testInvoice.getTotalIgstRate()).isEqualTo(DEFAULT_TOTAL_IGST_RATE);
        assertThat(testInvoice.getTotalCgstRate()).isEqualTo(DEFAULT_TOTAL_CGST_RATE);
        assertThat(testInvoice.getTotalSgstRate()).isEqualTo(DEFAULT_TOTAL_SGST_RATE);
        assertThat(testInvoice.getTotalTaxRate()).isEqualTo(DEFAULT_TOTAL_TAX_RATE);
        assertThat(testInvoice.getTotalAssAmount()).isEqualTo(DEFAULT_TOTAL_ASS_AMOUNT);
        assertThat(testInvoice.getTotalNoItems()).isEqualTo(DEFAULT_TOTAL_NO_ITEMS);
        assertThat(testInvoice.getGrandTotal()).isEqualTo(DEFAULT_GRAND_TOTAL);
        assertThat(testInvoice.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testInvoice.getModeOfTransaction()).isEqualTo(DEFAULT_MODE_OF_TRANSACTION);
        assertThat(testInvoice.getTransType()).isEqualTo(DEFAULT_TRANS_TYPE);
        assertThat(testInvoice.getVehicleNo()).isEqualTo(DEFAULT_VEHICLE_NO);
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].supplierClientId").value(hasItem(DEFAULT_SUPPLIER_CLIENT_ID)))
            .andExpect(jsonPath("$.[*].buyerClientId").value(hasItem(DEFAULT_BUYER_CLIENT_ID)))
            .andExpect(jsonPath("$.[*].invoiceItemsId").value(hasItem(DEFAULT_INVOICE_ITEMS_ID)))
            .andExpect(jsonPath("$.[*].shippingAddressId").value(hasItem(DEFAULT_SHIPPING_ADDRESS_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDateTime").value(hasItem(sameInstant(DEFAULT_CREATE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].totalIgstRate").value(hasItem(DEFAULT_TOTAL_IGST_RATE)))
            .andExpect(jsonPath("$.[*].totalCgstRate").value(hasItem(DEFAULT_TOTAL_CGST_RATE)))
            .andExpect(jsonPath("$.[*].totalSgstRate").value(hasItem(DEFAULT_TOTAL_SGST_RATE)))
            .andExpect(jsonPath("$.[*].totalTaxRate").value(hasItem(DEFAULT_TOTAL_TAX_RATE)))
            .andExpect(jsonPath("$.[*].totalAssAmount").value(hasItem(DEFAULT_TOTAL_ASS_AMOUNT)))
            .andExpect(jsonPath("$.[*].totalNoItems").value(hasItem(DEFAULT_TOTAL_NO_ITEMS)))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(DEFAULT_GRAND_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].modeOfTransaction").value(hasItem(DEFAULT_MODE_OF_TRANSACTION)))
            .andExpect(jsonPath("$.[*].transType").value(hasItem(DEFAULT_TRANS_TYPE)))
            .andExpect(jsonPath("$.[*].vehicleNo").value(hasItem(DEFAULT_VEHICLE_NO)));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID))
            .andExpect(jsonPath("$.supplierClientId").value(DEFAULT_SUPPLIER_CLIENT_ID))
            .andExpect(jsonPath("$.buyerClientId").value(DEFAULT_BUYER_CLIENT_ID))
            .andExpect(jsonPath("$.invoiceItemsId").value(DEFAULT_INVOICE_ITEMS_ID))
            .andExpect(jsonPath("$.shippingAddressId").value(DEFAULT_SHIPPING_ADDRESS_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createDateTime").value(sameInstant(DEFAULT_CREATE_DATE_TIME)))
            .andExpect(jsonPath("$.updateDateTime").value(sameInstant(DEFAULT_UPDATE_DATE_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.totalIgstRate").value(DEFAULT_TOTAL_IGST_RATE))
            .andExpect(jsonPath("$.totalCgstRate").value(DEFAULT_TOTAL_CGST_RATE))
            .andExpect(jsonPath("$.totalSgstRate").value(DEFAULT_TOTAL_SGST_RATE))
            .andExpect(jsonPath("$.totalTaxRate").value(DEFAULT_TOTAL_TAX_RATE))
            .andExpect(jsonPath("$.totalAssAmount").value(DEFAULT_TOTAL_ASS_AMOUNT))
            .andExpect(jsonPath("$.totalNoItems").value(DEFAULT_TOTAL_NO_ITEMS))
            .andExpect(jsonPath("$.grandTotal").value(DEFAULT_GRAND_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.modeOfTransaction").value(DEFAULT_MODE_OF_TRANSACTION))
            .andExpect(jsonPath("$.transType").value(DEFAULT_TRANS_TYPE))
            .andExpect(jsonPath("$.vehicleNo").value(DEFAULT_VEHICLE_NO));
    }

    @Test
    @Transactional
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .invoiceId(UPDATED_INVOICE_ID)
            .supplierClientId(UPDATED_SUPPLIER_CLIENT_ID)
            .buyerClientId(UPDATED_BUYER_CLIENT_ID)
            .invoiceItemsId(UPDATED_INVOICE_ITEMS_ID)
            .shippingAddressId(UPDATED_SHIPPING_ADDRESS_ID)
            .status(UPDATED_STATUS)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .totalIgstRate(UPDATED_TOTAL_IGST_RATE)
            .totalCgstRate(UPDATED_TOTAL_CGST_RATE)
            .totalSgstRate(UPDATED_TOTAL_SGST_RATE)
            .totalTaxRate(UPDATED_TOTAL_TAX_RATE)
            .totalAssAmount(UPDATED_TOTAL_ASS_AMOUNT)
            .totalNoItems(UPDATED_TOTAL_NO_ITEMS)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .distance(UPDATED_DISTANCE)
            .modeOfTransaction(UPDATED_MODE_OF_TRANSACTION)
            .transType(UPDATED_TRANS_TYPE)
            .vehicleNo(UPDATED_VEHICLE_NO);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testInvoice.getSupplierClientId()).isEqualTo(UPDATED_SUPPLIER_CLIENT_ID);
        assertThat(testInvoice.getBuyerClientId()).isEqualTo(UPDATED_BUYER_CLIENT_ID);
        assertThat(testInvoice.getInvoiceItemsId()).isEqualTo(UPDATED_INVOICE_ITEMS_ID);
        assertThat(testInvoice.getShippingAddressId()).isEqualTo(UPDATED_SHIPPING_ADDRESS_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testInvoice.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
        assertThat(testInvoice.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoice.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInvoice.getTotalIgstRate()).isEqualTo(UPDATED_TOTAL_IGST_RATE);
        assertThat(testInvoice.getTotalCgstRate()).isEqualTo(UPDATED_TOTAL_CGST_RATE);
        assertThat(testInvoice.getTotalSgstRate()).isEqualTo(UPDATED_TOTAL_SGST_RATE);
        assertThat(testInvoice.getTotalTaxRate()).isEqualTo(UPDATED_TOTAL_TAX_RATE);
        assertThat(testInvoice.getTotalAssAmount()).isEqualTo(UPDATED_TOTAL_ASS_AMOUNT);
        assertThat(testInvoice.getTotalNoItems()).isEqualTo(UPDATED_TOTAL_NO_ITEMS);
        assertThat(testInvoice.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testInvoice.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testInvoice.getModeOfTransaction()).isEqualTo(UPDATED_MODE_OF_TRANSACTION);
        assertThat(testInvoice.getTransType()).isEqualTo(UPDATED_TRANS_TYPE);
        assertThat(testInvoice.getVehicleNo()).isEqualTo(UPDATED_VEHICLE_NO);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .supplierClientId(UPDATED_SUPPLIER_CLIENT_ID)
            .buyerClientId(UPDATED_BUYER_CLIENT_ID)
            .createdBy(UPDATED_CREATED_BY)
            .totalSgstRate(UPDATED_TOTAL_SGST_RATE)
            .distance(UPDATED_DISTANCE)
            .modeOfTransaction(UPDATED_MODE_OF_TRANSACTION)
            .vehicleNo(UPDATED_VEHICLE_NO);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testInvoice.getSupplierClientId()).isEqualTo(UPDATED_SUPPLIER_CLIENT_ID);
        assertThat(testInvoice.getBuyerClientId()).isEqualTo(UPDATED_BUYER_CLIENT_ID);
        assertThat(testInvoice.getInvoiceItemsId()).isEqualTo(DEFAULT_INVOICE_ITEMS_ID);
        assertThat(testInvoice.getShippingAddressId()).isEqualTo(DEFAULT_SHIPPING_ADDRESS_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoice.getCreateDateTime()).isEqualTo(DEFAULT_CREATE_DATE_TIME);
        assertThat(testInvoice.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
        assertThat(testInvoice.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoice.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testInvoice.getTotalIgstRate()).isEqualTo(DEFAULT_TOTAL_IGST_RATE);
        assertThat(testInvoice.getTotalCgstRate()).isEqualTo(DEFAULT_TOTAL_CGST_RATE);
        assertThat(testInvoice.getTotalSgstRate()).isEqualTo(UPDATED_TOTAL_SGST_RATE);
        assertThat(testInvoice.getTotalTaxRate()).isEqualTo(DEFAULT_TOTAL_TAX_RATE);
        assertThat(testInvoice.getTotalAssAmount()).isEqualTo(DEFAULT_TOTAL_ASS_AMOUNT);
        assertThat(testInvoice.getTotalNoItems()).isEqualTo(DEFAULT_TOTAL_NO_ITEMS);
        assertThat(testInvoice.getGrandTotal()).isEqualTo(DEFAULT_GRAND_TOTAL);
        assertThat(testInvoice.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testInvoice.getModeOfTransaction()).isEqualTo(UPDATED_MODE_OF_TRANSACTION);
        assertThat(testInvoice.getTransType()).isEqualTo(DEFAULT_TRANS_TYPE);
        assertThat(testInvoice.getVehicleNo()).isEqualTo(UPDATED_VEHICLE_NO);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .invoiceId(UPDATED_INVOICE_ID)
            .supplierClientId(UPDATED_SUPPLIER_CLIENT_ID)
            .buyerClientId(UPDATED_BUYER_CLIENT_ID)
            .invoiceItemsId(UPDATED_INVOICE_ITEMS_ID)
            .shippingAddressId(UPDATED_SHIPPING_ADDRESS_ID)
            .status(UPDATED_STATUS)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .totalIgstRate(UPDATED_TOTAL_IGST_RATE)
            .totalCgstRate(UPDATED_TOTAL_CGST_RATE)
            .totalSgstRate(UPDATED_TOTAL_SGST_RATE)
            .totalTaxRate(UPDATED_TOTAL_TAX_RATE)
            .totalAssAmount(UPDATED_TOTAL_ASS_AMOUNT)
            .totalNoItems(UPDATED_TOTAL_NO_ITEMS)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .distance(UPDATED_DISTANCE)
            .modeOfTransaction(UPDATED_MODE_OF_TRANSACTION)
            .transType(UPDATED_TRANS_TYPE)
            .vehicleNo(UPDATED_VEHICLE_NO);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testInvoice.getSupplierClientId()).isEqualTo(UPDATED_SUPPLIER_CLIENT_ID);
        assertThat(testInvoice.getBuyerClientId()).isEqualTo(UPDATED_BUYER_CLIENT_ID);
        assertThat(testInvoice.getInvoiceItemsId()).isEqualTo(UPDATED_INVOICE_ITEMS_ID);
        assertThat(testInvoice.getShippingAddressId()).isEqualTo(UPDATED_SHIPPING_ADDRESS_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testInvoice.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
        assertThat(testInvoice.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoice.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInvoice.getTotalIgstRate()).isEqualTo(UPDATED_TOTAL_IGST_RATE);
        assertThat(testInvoice.getTotalCgstRate()).isEqualTo(UPDATED_TOTAL_CGST_RATE);
        assertThat(testInvoice.getTotalSgstRate()).isEqualTo(UPDATED_TOTAL_SGST_RATE);
        assertThat(testInvoice.getTotalTaxRate()).isEqualTo(UPDATED_TOTAL_TAX_RATE);
        assertThat(testInvoice.getTotalAssAmount()).isEqualTo(UPDATED_TOTAL_ASS_AMOUNT);
        assertThat(testInvoice.getTotalNoItems()).isEqualTo(UPDATED_TOTAL_NO_ITEMS);
        assertThat(testInvoice.getGrandTotal()).isEqualTo(UPDATED_GRAND_TOTAL);
        assertThat(testInvoice.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testInvoice.getModeOfTransaction()).isEqualTo(UPDATED_MODE_OF_TRANSACTION);
        assertThat(testInvoice.getTransType()).isEqualTo(UPDATED_TRANS_TYPE);
        assertThat(testInvoice.getVehicleNo()).isEqualTo(UPDATED_VEHICLE_NO);
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
