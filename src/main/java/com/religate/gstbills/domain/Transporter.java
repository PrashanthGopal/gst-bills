package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.religate.gstbills.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Transporter.
 */
@Entity
@Table(name = "transporter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transporter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "transporter_id")
    private String transporterId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @JsonIgnoreProperties(value = { "stateCode", "organization", "clients", "transporter", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Address address;

    @JsonIgnoreProperties(value = { "transporter", "address", "invoiceItems", "orgInvoices", "clients" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "transporter")
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transporter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public Transporter orgId(String orgId) {
        this.setOrgId(orgId);
        return this;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTransporterId() {
        return this.transporterId;
    }

    public Transporter transporterId(String transporterId) {
        this.setTransporterId(transporterId);
        return this;
    }

    public void setTransporterId(String transporterId) {
        this.transporterId = transporterId;
    }

    public String getName() {
        return this.name;
    }

    public Transporter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Transporter phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Status getStatus() {
        return this.status;
    }

    public Transporter status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Transporter address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        if (this.invoice != null) {
            this.invoice.setTransporter(null);
        }
        if (invoice != null) {
            invoice.setTransporter(this);
        }
        this.invoice = invoice;
    }

    public Transporter invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transporter)) {
            return false;
        }
        return id != null && id.equals(((Transporter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transporter{" +
            "id=" + getId() +
            ", orgId='" + getOrgId() + "'" +
            ", transporterId='" + getTransporterId() + "'" +
            ", name='" + getName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
