package com.religate.gstbills.web.rest;

import com.religate.gstbills.domain.OrgUsers;
import com.religate.gstbills.repository.OrgUsersRepository;
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
 * REST controller for managing {@link com.religate.gstbills.domain.OrgUsers}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrgUsersResource {

    private final Logger log = LoggerFactory.getLogger(OrgUsersResource.class);

    private static final String ENTITY_NAME = "orgUsers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgUsersRepository orgUsersRepository;

    public OrgUsersResource(OrgUsersRepository orgUsersRepository) {
        this.orgUsersRepository = orgUsersRepository;
    }

    /**
     * {@code POST  /org-users} : Create a new orgUsers.
     *
     * @param orgUsers the orgUsers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgUsers, or with status {@code 400 (Bad Request)} if the orgUsers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-users")
    public ResponseEntity<OrgUsers> createOrgUsers(@RequestBody OrgUsers orgUsers) throws URISyntaxException {
        log.debug("REST request to save OrgUsers : {}", orgUsers);
        if (orgUsers.getId() != null) {
            throw new BadRequestAlertException("A new orgUsers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUsers result = orgUsersRepository.save(orgUsers);
        return ResponseEntity
            .created(new URI("/api/org-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-users/:id} : Updates an existing orgUsers.
     *
     * @param id the id of the orgUsers to save.
     * @param orgUsers the orgUsers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUsers,
     * or with status {@code 400 (Bad Request)} if the orgUsers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgUsers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-users/{id}")
    public ResponseEntity<OrgUsers> updateOrgUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrgUsers orgUsers
    ) throws URISyntaxException {
        log.debug("REST request to update OrgUsers : {}, {}", id, orgUsers);
        if (orgUsers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUsers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgUsers result = orgUsersRepository.save(orgUsers);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgUsers.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /org-users/:id} : Partial updates given fields of an existing orgUsers, field will ignore if it is null
     *
     * @param id the id of the orgUsers to save.
     * @param orgUsers the orgUsers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUsers,
     * or with status {@code 400 (Bad Request)} if the orgUsers is not valid,
     * or with status {@code 404 (Not Found)} if the orgUsers is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgUsers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/org-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrgUsers> partialUpdateOrgUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrgUsers orgUsers
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgUsers partially : {}, {}", id, orgUsers);
        if (orgUsers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUsers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgUsers> result = orgUsersRepository
            .findById(orgUsers.getId())
            .map(existingOrgUsers -> {
                if (orgUsers.getUserId() != null) {
                    existingOrgUsers.setUserId(orgUsers.getUserId());
                }
                if (orgUsers.getName() != null) {
                    existingOrgUsers.setName(orgUsers.getName());
                }
                if (orgUsers.getUserName() != null) {
                    existingOrgUsers.setUserName(orgUsers.getUserName());
                }
                if (orgUsers.getPassword() != null) {
                    existingOrgUsers.setPassword(orgUsers.getPassword());
                }
                if (orgUsers.getStatus() != null) {
                    existingOrgUsers.setStatus(orgUsers.getStatus());
                }
                if (orgUsers.getCreateDate() != null) {
                    existingOrgUsers.setCreateDate(orgUsers.getCreateDate());
                }
                if (orgUsers.getUpdateDate() != null) {
                    existingOrgUsers.setUpdateDate(orgUsers.getUpdateDate());
                }
                if (orgUsers.getProfilePhoto() != null) {
                    existingOrgUsers.setProfilePhoto(orgUsers.getProfilePhoto());
                }
                if (orgUsers.getProfilePhotoContentType() != null) {
                    existingOrgUsers.setProfilePhotoContentType(orgUsers.getProfilePhotoContentType());
                }
                if (orgUsers.getEmailId() != null) {
                    existingOrgUsers.setEmailId(orgUsers.getEmailId());
                }
                if (orgUsers.getPhoneNumber() != null) {
                    existingOrgUsers.setPhoneNumber(orgUsers.getPhoneNumber());
                }

                return existingOrgUsers;
            })
            .map(orgUsersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgUsers.getId().toString())
        );
    }

    /**
     * {@code GET  /org-users} : get all the orgUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgUsers in body.
     */
    @GetMapping("/org-users")
    public List<OrgUsers> getAllOrgUsers() {
        log.debug("REST request to get all OrgUsers");
        return orgUsersRepository.findAll();
    }

    /**
     * {@code GET  /org-users/:id} : get the "id" orgUsers.
     *
     * @param id the id of the orgUsers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgUsers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-users/{id}")
    public ResponseEntity<OrgUsers> getOrgUsers(@PathVariable Long id) {
        log.debug("REST request to get OrgUsers : {}", id);
        Optional<OrgUsers> orgUsers = orgUsersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orgUsers);
    }

    /**
     * {@code DELETE  /org-users/:id} : delete the "id" orgUsers.
     *
     * @param id the id of the orgUsers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-users/{id}")
    public ResponseEntity<Void> deleteOrgUsers(@PathVariable Long id) {
        log.debug("REST request to delete OrgUsers : {}", id);
        orgUsersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
