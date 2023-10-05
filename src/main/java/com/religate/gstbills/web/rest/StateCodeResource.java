package com.religate.gstbills.web.rest;

import com.religate.gstbills.domain.StateCode;
import com.religate.gstbills.repository.StateCodeRepository;
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
 * REST controller for managing {@link com.religate.gstbills.domain.StateCode}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StateCodeResource {

    private final Logger log = LoggerFactory.getLogger(StateCodeResource.class);

    private static final String ENTITY_NAME = "stateCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StateCodeRepository stateCodeRepository;

    public StateCodeResource(StateCodeRepository stateCodeRepository) {
        this.stateCodeRepository = stateCodeRepository;
    }

    /**
     * {@code POST  /state-codes} : Create a new stateCode.
     *
     * @param stateCode the stateCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stateCode, or with status {@code 400 (Bad Request)} if the stateCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/state-codes")
    public ResponseEntity<StateCode> createStateCode(@RequestBody StateCode stateCode) throws URISyntaxException {
        log.debug("REST request to save StateCode : {}", stateCode);
        if (stateCode.getId() != null) {
            throw new BadRequestAlertException("A new stateCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StateCode result = stateCodeRepository.save(stateCode);
        return ResponseEntity
            .created(new URI("/api/state-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /state-codes/:id} : Updates an existing stateCode.
     *
     * @param id the id of the stateCode to save.
     * @param stateCode the stateCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateCode,
     * or with status {@code 400 (Bad Request)} if the stateCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stateCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/state-codes/{id}")
    public ResponseEntity<StateCode> updateStateCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateCode stateCode
    ) throws URISyntaxException {
        log.debug("REST request to update StateCode : {}, {}", id, stateCode);
        if (stateCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StateCode result = stateCodeRepository.save(stateCode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /state-codes/:id} : Partial updates given fields of an existing stateCode, field will ignore if it is null
     *
     * @param id the id of the stateCode to save.
     * @param stateCode the stateCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateCode,
     * or with status {@code 400 (Bad Request)} if the stateCode is not valid,
     * or with status {@code 404 (Not Found)} if the stateCode is not found,
     * or with status {@code 500 (Internal Server Error)} if the stateCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/state-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StateCode> partialUpdateStateCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateCode stateCode
    ) throws URISyntaxException {
        log.debug("REST request to partial update StateCode partially : {}, {}", id, stateCode);
        if (stateCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StateCode> result = stateCodeRepository
            .findById(stateCode.getId())
            .map(existingStateCode -> {
                if (stateCode.getStateCodeId() != null) {
                    existingStateCode.setStateCodeId(stateCode.getStateCodeId());
                }
                if (stateCode.getCode() != null) {
                    existingStateCode.setCode(stateCode.getCode());
                }
                if (stateCode.getName() != null) {
                    existingStateCode.setName(stateCode.getName());
                }

                return existingStateCode;
            })
            .map(stateCodeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateCode.getId().toString())
        );
    }

    /**
     * {@code GET  /state-codes} : get all the stateCodes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stateCodes in body.
     */
    @GetMapping("/state-codes")
    public List<StateCode> getAllStateCodes(@RequestParam(required = false) String filter) {
        if ("address-is-null".equals(filter)) {
            log.debug("REST request to get all StateCodes where address is null");
            return StreamSupport
                .stream(stateCodeRepository.findAll().spliterator(), false)
                .filter(stateCode -> stateCode.getAddress() == null)
                .toList();
        }
        log.debug("REST request to get all StateCodes");
        return stateCodeRepository.findAll();
    }

    /**
     * {@code GET  /state-codes/:id} : get the "id" stateCode.
     *
     * @param id the id of the stateCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/state-codes/{id}")
    public ResponseEntity<StateCode> getStateCode(@PathVariable Long id) {
        log.debug("REST request to get StateCode : {}", id);
        Optional<StateCode> stateCode = stateCodeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stateCode);
    }

    /**
     * {@code DELETE  /state-codes/:id} : delete the "id" stateCode.
     *
     * @param id the id of the stateCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/state-codes/{id}")
    public ResponseEntity<Void> deleteStateCode(@PathVariable Long id) {
        log.debug("REST request to delete StateCode : {}", id);
        stateCodeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
