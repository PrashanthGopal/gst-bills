package com.religate.gstbills.web.rest;

import com.religate.gstbills.domain.InvoiceItems;
import com.religate.gstbills.repository.InvoiceItemsRepository;
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
 * REST controller for managing {@link com.religate.gstbills.domain.InvoiceItems}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InvoiceItemsResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceItemsResource.class);

    private static final String ENTITY_NAME = "invoiceItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceItemsRepository invoiceItemsRepository;

    public InvoiceItemsResource(InvoiceItemsRepository invoiceItemsRepository) {
        this.invoiceItemsRepository = invoiceItemsRepository;
    }

    /**
     * {@code POST  /invoice-items} : Create a new invoiceItems.
     *
     * @param invoiceItems the invoiceItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceItems, or with status {@code 400 (Bad Request)} if the invoiceItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-items")
    public ResponseEntity<InvoiceItems> createInvoiceItems(@RequestBody InvoiceItems invoiceItems) throws URISyntaxException {
        log.debug("REST request to save InvoiceItems : {}", invoiceItems);
        if (invoiceItems.getId() != null) {
            throw new BadRequestAlertException("A new invoiceItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceItems result = invoiceItemsRepository.save(invoiceItems);
        return ResponseEntity
            .created(new URI("/api/invoice-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-items/:id} : Updates an existing invoiceItems.
     *
     * @param id the id of the invoiceItems to save.
     * @param invoiceItems the invoiceItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItems,
     * or with status {@code 400 (Bad Request)} if the invoiceItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-items/{id}")
    public ResponseEntity<InvoiceItems> updateInvoiceItems(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvoiceItems invoiceItems
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceItems : {}, {}", id, invoiceItems);
        if (invoiceItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItems.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceItems result = invoiceItemsRepository.save(invoiceItems);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-items/:id} : Partial updates given fields of an existing invoiceItems, field will ignore if it is null
     *
     * @param id the id of the invoiceItems to save.
     * @param invoiceItems the invoiceItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItems,
     * or with status {@code 400 (Bad Request)} if the invoiceItems is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceItems is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoice-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceItems> partialUpdateInvoiceItems(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvoiceItems invoiceItems
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceItems partially : {}, {}", id, invoiceItems);
        if (invoiceItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItems.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceItems> result = invoiceItemsRepository
            .findById(invoiceItems.getId())
            .map(existingInvoiceItems -> {
                if (invoiceItems.getOrgId() != null) {
                    existingInvoiceItems.setOrgId(invoiceItems.getOrgId());
                }
                if (invoiceItems.getInvoiceItemsId() != null) {
                    existingInvoiceItems.setInvoiceItemsId(invoiceItems.getInvoiceItemsId());
                }
                if (invoiceItems.getQuantity() != null) {
                    existingInvoiceItems.setQuantity(invoiceItems.getQuantity());
                }
                if (invoiceItems.getCgst() != null) {
                    existingInvoiceItems.setCgst(invoiceItems.getCgst());
                }
                if (invoiceItems.getSgst() != null) {
                    existingInvoiceItems.setSgst(invoiceItems.getSgst());
                }
                if (invoiceItems.getGst() != null) {
                    existingInvoiceItems.setGst(invoiceItems.getGst());
                }
                if (invoiceItems.getTotalAmount() != null) {
                    existingInvoiceItems.setTotalAmount(invoiceItems.getTotalAmount());
                }

                return existingInvoiceItems;
            })
            .map(invoiceItemsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItems.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-items} : get all the invoiceItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceItems in body.
     */
    @GetMapping("/invoice-items")
    public List<InvoiceItems> getAllInvoiceItems() {
        log.debug("REST request to get all InvoiceItems");
        return invoiceItemsRepository.findAll();
    }

    /**
     * {@code GET  /invoice-items/:id} : get the "id" invoiceItems.
     *
     * @param id the id of the invoiceItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-items/{id}")
    public ResponseEntity<InvoiceItems> getInvoiceItems(@PathVariable Long id) {
        log.debug("REST request to get InvoiceItems : {}", id);
        Optional<InvoiceItems> invoiceItems = invoiceItemsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invoiceItems);
    }

    /**
     * {@code DELETE  /invoice-items/:id} : delete the "id" invoiceItems.
     *
     * @param id the id of the invoiceItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-items/{id}")
    public ResponseEntity<Void> deleteInvoiceItems(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceItems : {}", id);
        invoiceItemsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
