package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "address_id")
    private String addressId;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "pincode")
    private String pincode;

    @JsonIgnoreProperties(value = { "address" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private StateCode stateCode;

    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgUsers", "clients", "products" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private Organization organization;

    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgClients" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private Clients clients;

    @JsonIgnoreProperties(value = { "address", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private Transporter transporter;

    @JsonIgnoreProperties(value = { "transporter", "address", "invoiceItems", "orgInvoices", "clients" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public Address orgId(String orgId) {
        this.setOrgId(orgId);
        return this;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAddressId() {
        return this.addressId;
    }

    public Address addressId(String addressId) {
        this.setAddressId(addressId);
        return this;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress1() {
        return this.address1;
    }

    public Address address1(String address1) {
        this.setAddress1(address1);
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public Address address2(String address2) {
        this.setAddress2(address2);
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPincode() {
        return this.pincode;
    }

    public Address pincode(String pincode) {
        this.setPincode(pincode);
        return this;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateCode getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(StateCode stateCode) {
        this.stateCode = stateCode;
    }

    public Address stateCode(StateCode stateCode) {
        this.setStateCode(stateCode);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        if (this.organization != null) {
            this.organization.setAddress(null);
        }
        if (organization != null) {
            organization.setAddress(this);
        }
        this.organization = organization;
    }

    public Address organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    public Clients getClients() {
        return this.clients;
    }

    public void setClients(Clients clients) {
        if (this.clients != null) {
            this.clients.setAddress(null);
        }
        if (clients != null) {
            clients.setAddress(this);
        }
        this.clients = clients;
    }

    public Address clients(Clients clients) {
        this.setClients(clients);
        return this;
    }

    public Transporter getTransporter() {
        return this.transporter;
    }

    public void setTransporter(Transporter transporter) {
        if (this.transporter != null) {
            this.transporter.setAddress(null);
        }
        if (transporter != null) {
            transporter.setAddress(this);
        }
        this.transporter = transporter;
    }

    public Address transporter(Transporter transporter) {
        this.setTransporter(transporter);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        if (this.invoice != null) {
            this.invoice.setAddress(null);
        }
        if (invoice != null) {
            invoice.setAddress(this);
        }
        this.invoice = invoice;
    }

    public Address invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", orgId='" + getOrgId() + "'" +
            ", addressId='" + getAddressId() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", pincode='" + getPincode() + "'" +
            "}";
    }
}
