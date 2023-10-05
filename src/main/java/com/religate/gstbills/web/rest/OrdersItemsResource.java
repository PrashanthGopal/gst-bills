package com.religate.gstbills.web.rest;

import com.religate.gstbills.domain.OrdersItems;
import com.religate.gstbills.repository.OrdersItemsRepository;
import com.religate.gstbills.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.religate.gstbills.domain.OrdersItems}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrdersItemsResource {

    private final Logger log = LoggerFactory.getLogger(OrdersItemsResource.class);

    private static final String ENTITY_NAME = "ordersItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdersItemsRepository ordersItemsRepository;

    public OrdersItemsResource(OrdersItemsRepository ordersItemsRepository) {
        this.ordersItemsRepository = ordersItemsRepository;
    }

    /**
     * {@code POST  /orders-items} : Create a new ordersItems.
     *
     * @param ordersItems the ordersItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordersItems, or with status {@code 400 (Bad Request)} if the ordersItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders-items")
    public ResponseEntity<OrdersItems> createOrdersItems(@RequestBody OrdersItems ordersItems) throws URISyntaxException {
        log.debug("REST request to save OrdersItems : {}", ordersItems);
        if (ordersItems.getId() != null) {
            throw new BadRequestAlertException("A new ordersItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdersItems result = ordersItemsRepository.save(ordersItems);
        return ResponseEntity
            .created(new URI("/api/orders-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orders-items/:id} : Updates an existing ordersItems.
     *
     * @param id the id of the ordersItems to save.
     * @param ordersItems the ordersItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordersItems,
     * or with status {@code 400 (Bad Request)} if the ordersItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordersItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orders-items/{id}")
    public ResponseEntity<OrdersItems> updateOrdersItems(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdersItems ordersItems
    ) throws URISyntaxException {
        log.debug("REST request to update OrdersItems : {}, {}", id, ordersItems);
        if (ordersItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordersItems.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordersItemsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdersItems result = ordersItemsRepository.save(ordersItems);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordersItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orders-items/:id} : Partial updates given fields of an existing ordersItems, field will ignore if it is null
     *
     * @param id the id of the ordersItems to save.
     * @param ordersItems the ordersItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordersItems,
     * or with status {@code 400 (Bad Request)} if the ordersItems is not valid,
     * or with status {@code 404 (Not Found)} if the ordersItems is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordersItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/orders-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdersItems> partialUpdateOrdersItems(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdersItems ordersItems
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdersItems partially : {}, {}", id, ordersItems);
        if (ordersItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordersItems.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordersItemsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdersItems> result = ordersItemsRepository
            .findById(ordersItems.getId())
            .map(existingOrdersItems -> {
                if (ordersItems.getOrgId() != null) {
                    existingOrdersItems.setOrgId(ordersItems.getOrgId());
                }
                if (ordersItems.getOrderItemsId() != null) {
                    existingOrdersItems.setOrderItemsId(ordersItems.getOrderItemsId());
                }
                if (ordersItems.getQuantity() != null) {
                    existingOrdersItems.setQuantity(ordersItems.getQuantity());
                }
                if (ordersItems.getCgst() != null) {
                    existingOrdersItems.setCgst(ordersItems.getCgst());
                }
                if (ordersItems.getSgst() != null) {
                    existingOrdersItems.setSgst(ordersItems.getSgst());
                }
                if (ordersItems.getGst() != null) {
                    existingOrdersItems.setGst(ordersItems.getGst());
                }
                if (ordersItems.getTotalAmount() != null) {
                    existingOrdersItems.setTotalAmount(ordersItems.getTotalAmount());
                }

                return existingOrdersItems;
            })
            .map(ordersItemsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordersItems.getId().toString())
        );
    }

    /**
     * {@code GET  /orders-items} : get all the ordersItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordersItems in body.
     */
    @GetMapping("/orders-items")
    public List<OrdersItems> getAllOrdersItems() {
        log.debug("REST request to get all OrdersItems");
        return ordersItemsRepository.findAll();
    }

    /**
     * {@code GET  /orders-items/:id} : get the "id" ordersItems.
     *
     * @param id the id of the ordersItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordersItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders-items/{id}")
    public ResponseEntity<OrdersItems> getOrdersItems(@PathVariable Long id) {
        log.debug("REST request to get OrdersItems : {}", id);
        Optional<OrdersItems> ordersItems = ordersItemsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ordersItems);
    }

    /**
     * {@code DELETE  /orders-items/:id} : delete the "id" ordersItems.
     *
     * @param id the id of the ordersItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orders-items/{id}")
    public ResponseEntity<Void> deleteOrdersItems(@PathVariable Long id) {
        log.debug("REST request to delete OrdersItems : {}", id);
        ordersItemsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
