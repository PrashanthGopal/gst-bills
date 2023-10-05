package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.religate.gstbills.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Clients.
 */
@Entity
@Table(name = "clients")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Clients implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "name")
    private String name;

    @Column(name = "gstin")
    private String gstin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonIgnoreProperties(value = { "stateCode", "organization", "clients", "transporter", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clients")
    @JsonIgnoreProperties(value = { "transporter", "address", "invoiceItems", "orgInvoices", "clients" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @JsonIgnoreProperties(value = { "clients", "ordersItems", "orgOrders" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "clients")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgUsers", "clients", "products" }, allowSetters = true)
    private Organization orgClients;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Clients id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return this.clientId;
    }

    public Clients clientId(String clientId) {
        this.setClientId(clientId);
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return this.name;
    }

    public Clients name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGstin() {
        return this.gstin;
    }

    public Clients gstin(String gstin) {
        this.setGstin(gstin);
        return this;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public Status getStatus() {
        return this.status;
    }

    public Clients status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public Clients emailId(String emailId) {
        this.setEmailId(emailId);
        return this;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Clients phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Clients address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setClients(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setClients(this));
        }
        this.invoices = invoices;
    }

    public Clients invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Clients addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setClients(this);
        return this;
    }

    public Clients removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setClients(null);
        return this;
    }

    public Orders getOrders() {
        return this.orders;
    }

    public void setOrders(Orders orders) {
        if (this.orders != null) {
            this.orders.setClients(null);
        }
        if (orders != null) {
            orders.setClients(this);
        }
        this.orders = orders;
    }

    public Clients orders(Orders orders) {
        this.setOrders(orders);
        return this;
    }

    public Organization getOrgClients() {
        return this.orgClients;
    }

    public void setOrgClients(Organization organization) {
        this.orgClients = organization;
    }

    public Clients orgClients(Organization organization) {
        this.setOrgClients(organization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clients)) {
            return false;
        }
        return id != null && id.equals(((Clients) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clients{" +
            "id=" + getId() +
            ", clientId='" + getClientId() + "'" +
            ", name='" + getName() + "'" +
            ", gstin='" + getGstin() + "'" +
            ", status='" + getStatus() + "'" +
            ", emailId='" + getEmailId() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
