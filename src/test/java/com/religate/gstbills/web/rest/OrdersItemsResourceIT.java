package com.religate.gstbills.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.religate.gstbills.IntegrationTest;
import com.religate.gstbills.domain.OrdersItems;
import com.religate.gstbills.repository.OrdersItemsRepository;
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
 * Integration tests for the {@link OrdersItemsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdersItemsResourceIT {

    private static final String DEFAULT_ORG_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORG_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_ITEMS_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ITEMS_ID = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/orders-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdersItemsRepository ordersItemsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdersItemsMockMvc;

    private OrdersItems ordersItems;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdersItems createEntity(EntityManager em) {
        OrdersItems ordersItems = new OrdersItems()
            .orgId(DEFAULT_ORG_ID)
            .orderItemsId(DEFAULT_ORDER_ITEMS_ID)
            .quantity(DEFAULT_QUANTITY)
            .cgst(DEFAULT_CGST)
            .sgst(DEFAULT_SGST)
            .gst(DEFAULT_GST)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
        return ordersItems;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdersItems createUpdatedEntity(EntityManager em) {
        OrdersItems ordersItems = new OrdersItems()
            .orgId(UPDATED_ORG_ID)
            .orderItemsId(UPDATED_ORDER_ITEMS_ID)
            .quantity(UPDATED_QUANTITY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .gst(UPDATED_GST)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
        return ordersItems;
    }

    @BeforeEach
    public void initTest() {
        ordersItems = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdersItems() throws Exception {
        int databaseSizeBeforeCreate = ordersItemsRepository.findAll().size();
        // Create the OrdersItems
        restOrdersItemsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersItems)))
            .andExpect(status().isCreated());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeCreate + 1);
        OrdersItems testOrdersItems = ordersItemsList.get(ordersItemsList.size() - 1);
        assertThat(testOrdersItems.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testOrdersItems.getOrderItemsId()).isEqualTo(DEFAULT_ORDER_ITEMS_ID);
        assertThat(testOrdersItems.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrdersItems.getCgst()).isEqualTo(DEFAULT_CGST);
        assertThat(testOrdersItems.getSgst()).isEqualTo(DEFAULT_SGST);
        assertThat(testOrdersItems.getGst()).isEqualTo(DEFAULT_GST);
        assertThat(testOrdersItems.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void createOrdersItemsWithExistingId() throws Exception {
        // Create the OrdersItems with an existing ID
        ordersItems.setId(1L);

        int databaseSizeBeforeCreate = ordersItemsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersItemsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersItems)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdersItems() throws Exception {
        // Initialize the database
        ordersItemsRepository.saveAndFlush(ordersItems);

        // Get all the ordersItemsList
        restOrdersItemsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgId").value(hasItem(DEFAULT_ORG_ID)))
            .andExpect(jsonPath("$.[*].orderItemsId").value(hasItem(DEFAULT_ORDER_ITEMS_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(DEFAULT_CGST)))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(DEFAULT_SGST)))
            .andExpect(jsonPath("$.[*].gst").value(hasItem(DEFAULT_GST)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getOrdersItems() throws Exception {
        // Initialize the database
        ordersItemsRepository.saveAndFlush(ordersItems);

        // Get the ordersItems
        restOrdersItemsMockMvc
            .perform(get(ENTITY_API_URL_ID, ordersItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordersItems.getId().intValue()))
            .andExpect(jsonPath("$.orgId").value(DEFAULT_ORG_ID))
            .andExpect(jsonPath("$.orderItemsId").value(DEFAULT_ORDER_ITEMS_ID))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.cgst").value(DEFAULT_CGST))
            .andExpect(jsonPath("$.sgst").value(DEFAULT_SGST))
            .andExpect(jsonPath("$.gst").value(DEFAULT_GST))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrdersItems() throws Exception {
        // Get the ordersItems
        restOrdersItemsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrdersItems() throws Exception {
        // Initialize the database
        ordersItemsRepository.saveAndFlush(ordersItems);

        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();

        // Update the ordersItems
        OrdersItems updatedOrdersItems = ordersItemsRepository.findById(ordersItems.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrdersItems are not directly saved in db
        em.detach(updatedOrdersItems);
        updatedOrdersItems
            .orgId(UPDATED_ORG_ID)
            .orderItemsId(UPDATED_ORDER_ITEMS_ID)
            .quantity(UPDATED_QUANTITY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .gst(UPDATED_GST)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restOrdersItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdersItems.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdersItems))
            )
            .andExpect(status().isOk());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
        OrdersItems testOrdersItems = ordersItemsList.get(ordersItemsList.size() - 1);
        assertThat(testOrdersItems.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testOrdersItems.getOrderItemsId()).isEqualTo(UPDATED_ORDER_ITEMS_ID);
        assertThat(testOrdersItems.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrdersItems.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testOrdersItems.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testOrdersItems.getGst()).isEqualTo(UPDATED_GST);
        assertThat(testOrdersItems.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingOrdersItems() throws Exception {
        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();
        ordersItems.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordersItems.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdersItems() throws Exception {
        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();
        ordersItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdersItems() throws Exception {
        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();
        ordersItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersItemsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersItems)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdersItemsWithPatch() throws Exception {
        // Initialize the database
        ordersItemsRepository.saveAndFlush(ordersItems);

        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();

        // Update the ordersItems using partial update
        OrdersItems partialUpdatedOrdersItems = new OrdersItems();
        partialUpdatedOrdersItems.setId(ordersItems.getId());

        partialUpdatedOrdersItems.sgst(UPDATED_SGST).gst(UPDATED_GST).totalAmount(UPDATED_TOTAL_AMOUNT);

        restOrdersItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdersItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdersItems))
            )
            .andExpect(status().isOk());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
        OrdersItems testOrdersItems = ordersItemsList.get(ordersItemsList.size() - 1);
        assertThat(testOrdersItems.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
        assertThat(testOrdersItems.getOrderItemsId()).isEqualTo(DEFAULT_ORDER_ITEMS_ID);
        assertThat(testOrdersItems.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrdersItems.getCgst()).isEqualTo(DEFAULT_CGST);
        assertThat(testOrdersItems.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testOrdersItems.getGst()).isEqualTo(UPDATED_GST);
        assertThat(testOrdersItems.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateOrdersItemsWithPatch() throws Exception {
        // Initialize the database
        ordersItemsRepository.saveAndFlush(ordersItems);

        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();

        // Update the ordersItems using partial update
        OrdersItems partialUpdatedOrdersItems = new OrdersItems();
        partialUpdatedOrdersItems.setId(ordersItems.getId());

        partialUpdatedOrdersItems
            .orgId(UPDATED_ORG_ID)
            .orderItemsId(UPDATED_ORDER_ITEMS_ID)
            .quantity(UPDATED_QUANTITY)
            .cgst(UPDATED_CGST)
            .sgst(UPDATED_SGST)
            .gst(UPDATED_GST)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restOrdersItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdersItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdersItems))
            )
            .andExpect(status().isOk());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
        OrdersItems testOrdersItems = ordersItemsList.get(ordersItemsList.size() - 1);
        assertThat(testOrdersItems.getOrgId()).isEqualTo(UPDATED_ORG_ID);
        assertThat(testOrdersItems.getOrderItemsId()).isEqualTo(UPDATED_ORDER_ITEMS_ID);
        assertThat(testOrdersItems.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrdersItems.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testOrdersItems.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testOrdersItems.getGst()).isEqualTo(UPDATED_GST);
        assertThat(testOrdersItems.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingOrdersItems() throws Exception {
        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();
        ordersItems.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordersItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdersItems() throws Exception {
        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();
        ordersItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdersItems() throws Exception {
        int databaseSizeBeforeUpdate = ordersItemsRepository.findAll().size();
        ordersItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersItemsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordersItems))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdersItems in the database
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdersItems() throws Exception {
        // Initialize the database
        ordersItemsRepository.saveAndFlush(ordersItems);

        int databaseSizeBeforeDelete = ordersItemsRepository.findAll().size();

        // Delete the ordersItems
        restOrdersItemsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordersItems.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdersItems> ordersItemsList = ordersItemsRepository.findAll();
        assertThat(ordersItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
