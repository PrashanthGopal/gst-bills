package com.religate.gstbills.web.rest;

import com.religate.gstbills.domain.Clients;
import com.religate.gstbills.repository.ClientsRepository;
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
 * REST controller for managing {@link com.religate.gstbills.domain.Clients}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClientsResource {

    private final Logger log = LoggerFactory.getLogger(ClientsResource.class);

    private static final String ENTITY_NAME = "clients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientsRepository clientsRepository;

    public ClientsResource(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    /**
     * {@code POST  /clients} : Create a new clients.
     *
     * @param clients the clients to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clients, or with status {@code 400 (Bad Request)} if the clients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clients")
    public ResponseEntity<Clients> createClients(@RequestBody Clients clients) throws URISyntaxException {
        log.debug("REST request to save Clients : {}", clients);
        if (clients.getId() != null) {
            throw new BadRequestAlertException("A new clients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clients result = clientsRepository.save(clients);
        return ResponseEntity
            .created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clients/:id} : Updates an existing clients.
     *
     * @param id the id of the clients to save.
     * @param clients the clients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clients,
     * or with status {@code 400 (Bad Request)} if the clients is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clients/{id}")
    public ResponseEntity<Clients> updateClients(@PathVariable(value = "id", required = false) final Long id, @RequestBody Clients clients)
        throws URISyntaxException {
        log.debug("REST request to update Clients : {}, {}", id, clients);
        if (clients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clients result = clientsRepository.save(clients);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clients.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clients/:id} : Partial updates given fields of an existing clients, field will ignore if it is null
     *
     * @param id the id of the clients to save.
     * @param clients the clients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clients,
     * or with status {@code 400 (Bad Request)} if the clients is not valid,
     * or with status {@code 404 (Not Found)} if the clients is not found,
     * or with status {@code 500 (Internal Server Error)} if the clients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Clients> partialUpdateClients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Clients clients
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clients partially : {}, {}", id, clients);
        if (clients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clients> result = clientsRepository
            .findById(clients.getId())
            .map(existingClients -> {
                if (clients.getClientId() != null) {
                    existingClients.setClientId(clients.getClientId());
                }
                if (clients.getName() != null) {
                    existingClients.setName(clients.getName());
                }
                if (clients.getGstin() != null) {
                    existingClients.setGstin(clients.getGstin());
                }
                if (clients.getStatus() != null) {
                    existingClients.setStatus(clients.getStatus());
                }
                if (clients.getEmailId() != null) {
                    existingClients.setEmailId(clients.getEmailId());
                }
                if (clients.getPhoneNumber() != null) {
                    existingClients.setPhoneNumber(clients.getPhoneNumber());
                }

                return existingClients;
            })
            .map(clientsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clients.getId().toString())
        );
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("/clients")
    public List<Clients> getAllClients(@RequestParam(required = false) String filter) {
        if ("orders-is-null".equals(filter)) {
            log.debug("REST request to get all Clientss where orders is null");
            return StreamSupport
                .stream(clientsRepository.findAll().spliterator(), false)
                .filter(clients -> clients.getOrders() == null)
                .toList();
        }
        log.debug("REST request to get all Clients");
        return clientsRepository.findAll();
    }

    /**
     * {@code GET  /clients/:id} : get the "id" clients.
     *
     * @param id the id of the clients to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clients, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public ResponseEntity<Clients> getClients(@PathVariable Long id) {
        log.debug("REST request to get Clients : {}", id);
        Optional<Clients> clients = clientsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clients);
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" clients.
     *
     * @param id the id of the clients to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClients(@PathVariable Long id) {
        log.debug("REST request to delete Clients : {}", id);
        clientsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
