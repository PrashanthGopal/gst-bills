package com.religate.gstbills.web.rest;

import com.religate.gstbills.domain.Transporter;
import com.religate.gstbills.repository.TransporterRepository;
import com.religate.gstbills.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.religate.gstbills.domain.Transporter}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TransporterResource {

    private final Logger log = LoggerFactory.getLogger(TransporterResource.class);

    private static final String ENTITY_NAME = "transporter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransporterRepository transporterRepository;

    public TransporterResource(TransporterRepository transporterRepository) {
        this.transporterRepository = transporterRepository;
    }

    /**
     * {@code POST  /transporters} : Create a new transporter.
     *
     * @param transporter the transporter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transporter, or with status {@code 400 (Bad Request)} if the transporter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transporters")
    public ResponseEntity<Transporter> createTransporter(@RequestBody Transporter transporter) throws URISyntaxException {
        log.debug("REST request to save Transporter : {}", transporter);
        if (transporter.getId() != null) {
            throw new BadRequestAlertException("A new transporter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transporter result = transporterRepository.save(transporter);
        return ResponseEntity
            .created(new URI("/api/transporters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transporters/:id} : Updates an existing transporter.
     *
     * @param id the id of the transporter to save.
     * @param transporter the transporter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transporter,
     * or with status {@code 400 (Bad Request)} if the transporter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transporter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transporters/{id}")
    public ResponseEntity<Transporter> updateTransporter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Transporter transporter
    ) throws URISyntaxException {
        log.debug("REST request to update Transporter : {}, {}", id, transporter);
        if (transporter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transporter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transporterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Transporter result = transporterRepository.save(transporter);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transporter.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transporters/:id} : Partial updates given fields of an existing transporter, field will ignore if it is null
     *
     * @param id the id of the transporter to save.
     * @param transporter the transporter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transporter,
     * or with status {@code 400 (Bad Request)} if the transporter is not valid,
     * or with status {@code 404 (Not Found)} if the transporter is not found,
     * or with status {@code 500 (Internal Server Error)} if the transporter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transporters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Transporter> partialUpdateTransporter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Transporter transporter
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transporter partially : {}, {}", id, transporter);
        if (transporter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transporter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transporterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Transporter> result = transporterRepository
            .findById(transporter.getId())
            .map(existingTransporter -> {
                if (transporter.getOrgId() != null) {
                    existingTransporter.setOrgId(transporter.getOrgId());
                }
                if (transporter.getTransporterId() != null) {
                    existingTransporter.setTransporterId(transporter.getTransporterId());
                }
                if (transporter.getName() != null) {
                    existingTransporter.setName(transporter.getName());
                }
                if (transporter.getPhoneNumber() != null) {
                    existingTransporter.setPhoneNumber(transporter.getPhoneNumber());
                }
                if (transporter.getStatus() != null) {
                    existingTransporter.setStatus(transporter.getStatus());
                }

                return existingTransporter;
            })
            .map(transporterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transporter.getId().toString())
        );
    }

    /**
     * {@code GET  /transporters} : get all the transporters.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transporters in body.
     */
    @GetMapping("/transporters")
    public List<Transporter> getAllTransporters(@RequestParam(required = false) String filter) {
//        if ("invoice-is-null".equals(filter)) {
//            log.debug("REST request to get all Transporters where invoice is null");
//            return StreamSupport
//                .stream(transporterRepository.findAll().spliterator(), false)
//                .filter(transporter -> transporter.getInvoice() == null)
//                .toList();
//        }
        log.debug("REST request to get all Transporters");
        return transporterRepository.findAll();
    }

    /**
     * {@code GET  /transporters/:id} : get the "id" transporter.
     *
     * @param id the id of the transporter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transporter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transporters/{id}")
    public ResponseEntity<Transporter> getTransporter(@PathVariable Long id) {
        log.debug("REST request to get Transporter : {}", id);
        Optional<Transporter> transporter = transporterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transporter);
    }

    /**
     * {@code DELETE  /transporters/:id} : delete the "id" transporter.
     *
     * @param id the id of the transporter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transporters/{id}")
    public ResponseEntity<Void> deleteTransporter(@PathVariable Long id) {
        log.debug("REST request to delete Transporter : {}", id);
        transporterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
