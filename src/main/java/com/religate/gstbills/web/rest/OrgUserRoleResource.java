package com.religate.gstbills.web.rest;

import com.religate.gstbills.domain.OrgUserRole;
import com.religate.gstbills.repository.OrgUserRoleRepository;
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
 * REST controller for managing {@link com.religate.gstbills.domain.OrgUserRole}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrgUserRoleResource {

    private final Logger log = LoggerFactory.getLogger(OrgUserRoleResource.class);

    private static final String ENTITY_NAME = "orgUserRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgUserRoleRepository orgUserRoleRepository;

    public OrgUserRoleResource(OrgUserRoleRepository orgUserRoleRepository) {
        this.orgUserRoleRepository = orgUserRoleRepository;
    }

    /**
     * {@code POST  /org-user-roles} : Create a new orgUserRole.
     *
     * @param orgUserRole the orgUserRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgUserRole, or with status {@code 400 (Bad Request)} if the orgUserRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-user-roles")
    public ResponseEntity<OrgUserRole> createOrgUserRole(@RequestBody OrgUserRole orgUserRole) throws URISyntaxException {
        log.debug("REST request to save OrgUserRole : {}", orgUserRole);
        if (orgUserRole.getId() != null) {
            throw new BadRequestAlertException("A new orgUserRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUserRole result = orgUserRoleRepository.save(orgUserRole);
        return ResponseEntity
            .created(new URI("/api/org-user-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-user-roles/:id} : Updates an existing orgUserRole.
     *
     * @param id the id of the orgUserRole to save.
     * @param orgUserRole the orgUserRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUserRole,
     * or with status {@code 400 (Bad Request)} if the orgUserRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgUserRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-user-roles/{id}")
    public ResponseEntity<OrgUserRole> updateOrgUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrgUserRole orgUserRole
    ) throws URISyntaxException {
        log.debug("REST request to update OrgUserRole : {}, {}", id, orgUserRole);
        if (orgUserRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUserRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUserRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgUserRole result = orgUserRoleRepository.save(orgUserRole);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgUserRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /org-user-roles/:id} : Partial updates given fields of an existing orgUserRole, field will ignore if it is null
     *
     * @param id the id of the orgUserRole to save.
     * @param orgUserRole the orgUserRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUserRole,
     * or with status {@code 400 (Bad Request)} if the orgUserRole is not valid,
     * or with status {@code 404 (Not Found)} if the orgUserRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgUserRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/org-user-roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrgUserRole> partialUpdateOrgUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrgUserRole orgUserRole
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgUserRole partially : {}, {}", id, orgUserRole);
        if (orgUserRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUserRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUserRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgUserRole> result = orgUserRoleRepository
            .findById(orgUserRole.getId())
            .map(existingOrgUserRole -> {
                if (orgUserRole.getRoleId() != null) {
                    existingOrgUserRole.setRoleId(orgUserRole.getRoleId());
                }
                if (orgUserRole.getOrgId() != null) {
                    existingOrgUserRole.setOrgId(orgUserRole.getOrgId());
                }
                if (orgUserRole.getName() != null) {
                    existingOrgUserRole.setName(orgUserRole.getName());
                }

                return existingOrgUserRole;
            })
            .map(orgUserRoleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgUserRole.getId().toString())
        );
    }

    /**
     * {@code GET  /org-user-roles} : get all the orgUserRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgUserRoles in body.
     */
    @GetMapping("/org-user-roles")
    public List<OrgUserRole> getAllOrgUserRoles() {
        log.debug("REST request to get all OrgUserRoles");
        return orgUserRoleRepository.findAll();
    }

    /**
     * {@code GET  /org-user-roles/:id} : get the "id" orgUserRole.
     *
     * @param id the id of the orgUserRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgUserRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-user-roles/{id}")
    public ResponseEntity<OrgUserRole> getOrgUserRole(@PathVariable Long id) {
        log.debug("REST request to get OrgUserRole : {}", id);
        Optional<OrgUserRole> orgUserRole = orgUserRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orgUserRole);
    }

    /**
     * {@code DELETE  /org-user-roles/:id} : delete the "id" orgUserRole.
     *
     * @param id the id of the orgUserRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-user-roles/{id}")
    public ResponseEntity<Void> deleteOrgUserRole(@PathVariable Long id) {
        log.debug("REST request to delete OrgUserRole : {}", id);
        orgUserRoleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
