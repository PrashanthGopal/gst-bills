package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.religate.gstbills.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A OrgUsers.
 */
@Entity
@Table(name = "org_users")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrgUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "update_date")
    private ZonedDateTime updateDate;

    @Lob
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Column(name = "profile_photo_content_type")
    private String profilePhotoContentType;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userRole")
    @JsonIgnoreProperties(value = { "userRole" }, allowSetters = true)
    private Set<OrgUserRole> orgUserRoles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgUsers", "clients", "products" }, allowSetters = true)
    private Organization orgUsers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrgUsers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public OrgUsers userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public OrgUsers name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return this.userName;
    }

    public OrgUsers userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public OrgUsers password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return this.status;
    }

    public OrgUsers status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ZonedDateTime getCreateDate() {
        return this.createDate;
    }

    public OrgUsers createDate(ZonedDateTime createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getUpdateDate() {
        return this.updateDate;
    }

    public OrgUsers updateDate(ZonedDateTime updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public byte[] getProfilePhoto() {
        return this.profilePhoto;
    }

    public OrgUsers profilePhoto(byte[] profilePhoto) {
        this.setProfilePhoto(profilePhoto);
        return this;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoContentType() {
        return this.profilePhotoContentType;
    }

    public OrgUsers profilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
        return this;
    }

    public void setProfilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public OrgUsers emailId(String emailId) {
        this.setEmailId(emailId);
        return this;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public OrgUsers phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<OrgUserRole> getOrgUserRoles() {
        return this.orgUserRoles;
    }

    public void setOrgUserRoles(Set<OrgUserRole> orgUserRoles) {
        if (this.orgUserRoles != null) {
            this.orgUserRoles.forEach(i -> i.setUserRole(null));
        }
        if (orgUserRoles != null) {
            orgUserRoles.forEach(i -> i.setUserRole(this));
        }
        this.orgUserRoles = orgUserRoles;
    }

    public OrgUsers orgUserRoles(Set<OrgUserRole> orgUserRoles) {
        this.setOrgUserRoles(orgUserRoles);
        return this;
    }

    public OrgUsers addOrgUserRole(OrgUserRole orgUserRole) {
        this.orgUserRoles.add(orgUserRole);
        orgUserRole.setUserRole(this);
        return this;
    }

    public OrgUsers removeOrgUserRole(OrgUserRole orgUserRole) {
        this.orgUserRoles.remove(orgUserRole);
        orgUserRole.setUserRole(null);
        return this;
    }

    public Organization getOrgUsers() {
        return this.orgUsers;
    }

    public void setOrgUsers(Organization organization) {
        this.orgUsers = organization;
    }

    public OrgUsers orgUsers(Organization organization) {
        this.setOrgUsers(organization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgUsers)) {
            return false;
        }
        return id != null && id.equals(((OrgUsers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgUsers{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", name='" + getName() + "'" +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            ", status='" + getStatus() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", profilePhoto='" + getProfilePhoto() + "'" +
            ", profilePhotoContentType='" + getProfilePhotoContentType() + "'" +
            ", emailId='" + getEmailId() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
