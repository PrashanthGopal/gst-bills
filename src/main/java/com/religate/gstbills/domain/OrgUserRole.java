package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A OrgUserRole.
 */
@Entity
@Table(name = "org_user_role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrgUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orgUserRoles", "orgUsers" }, allowSetters = true)
    private OrgUsers userRole;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrgUserRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public OrgUserRole roleId(String roleId) {
        this.setRoleId(roleId);
        return this;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public OrgUserRole orgId(String orgId) {
        this.setOrgId(orgId);
        return this;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return this.name;
    }

    public OrgUserRole name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrgUsers getUserRole() {
        return this.userRole;
    }

    public void setUserRole(OrgUsers orgUsers) {
        this.userRole = orgUsers;
    }

    public OrgUserRole userRole(OrgUsers orgUsers) {
        this.setUserRole(orgUsers);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgUserRole)) {
            return false;
        }
        return id != null && id.equals(((OrgUserRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgUserRole{" +
            "id=" + getId() +
            ", roleId='" + getRoleId() + "'" +
            ", orgId='" + getOrgId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
