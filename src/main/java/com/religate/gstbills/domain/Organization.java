package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.religate.gstbills.domain.enumeration.Status;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "legal_org_name")
    private String legalOrgName;

    @Column(name = "pan")
    private String pan;

    @Column(name = "gstin")
    private String gstin;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_id")
    private String emailId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "date_of_regestation")
    private ZonedDateTime dateOfRegestation;

    @Column(name = "date_of_de_regestation")
    private ZonedDateTime dateOfDeRegestation;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @JsonIgnoreProperties(value = { "stateCode", "organization", "clients", "transporter", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgInvoices")
    @JsonIgnoreProperties(value = { "transporter", "address", "invoiceItems", "orgInvoices", "clients" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgOrders")
    @JsonIgnoreProperties(value = { "clients", "ordersItems", "orgOrders" }, allowSetters = true)
    private Set<Orders> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgUsers")
    @JsonIgnoreProperties(value = { "orgUserRoles", "orgUsers" }, allowSetters = true)
    private Set<OrgUsers> orgUsers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgClients")
    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgClients" }, allowSetters = true)
    private Set<Clients> clients = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgProducts")
    @JsonIgnoreProperties(value = { "productUnit", "invoiceItems", "ordersItems", "orgProducts" }, allowSetters = true)
    private Set<Products> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Organization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public Organization orgId(String orgId) {
        this.setOrgId(orgId);
        return this;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public Organization orgName(String orgName) {
        this.setOrgName(orgName);
        return this;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLegalOrgName() {
        return this.legalOrgName;
    }

    public Organization legalOrgName(String legalOrgName) {
        this.setLegalOrgName(legalOrgName);
        return this;
    }

    public void setLegalOrgName(String legalOrgName) {
        this.legalOrgName = legalOrgName;
    }

    public String getPan() {
        return this.pan;
    }

    public Organization pan(String pan) {
        this.setPan(pan);
        return this;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getGstin() {
        return this.gstin;
    }

    public Organization gstin(String gstin) {
        this.setGstin(gstin);
        return this;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Organization phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public Organization emailId(String emailId) {
        this.setEmailId(emailId);
        return this;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Status getStatus() {
        return this.status;
    }

    public Organization status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ZonedDateTime getDateOfRegestation() {
        return this.dateOfRegestation;
    }

    public Organization dateOfRegestation(ZonedDateTime dateOfRegestation) {
        this.setDateOfRegestation(dateOfRegestation);
        return this;
    }

    public void setDateOfRegestation(ZonedDateTime dateOfRegestation) {
        this.dateOfRegestation = dateOfRegestation;
    }

    public ZonedDateTime getDateOfDeRegestation() {
        return this.dateOfDeRegestation;
    }

    public Organization dateOfDeRegestation(ZonedDateTime dateOfDeRegestation) {
        this.setDateOfDeRegestation(dateOfDeRegestation);
        return this;
    }

    public void setDateOfDeRegestation(ZonedDateTime dateOfDeRegestation) {
        this.dateOfDeRegestation = dateOfDeRegestation;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Organization logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Organization logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Organization address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setOrgInvoices(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setOrgInvoices(this));
        }
        this.invoices = invoices;
    }

    public Organization invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Organization addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setOrgInvoices(this);
        return this;
    }

    public Organization removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setOrgInvoices(null);
        return this;
    }

    public Set<Orders> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Orders> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setOrgOrders(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setOrgOrders(this));
        }
        this.orders = orders;
    }

    public Organization orders(Set<Orders> orders) {
        this.setOrders(orders);
        return this;
    }

    public Organization addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setOrgOrders(this);
        return this;
    }

    public Organization removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setOrgOrders(null);
        return this;
    }

    public Set<OrgUsers> getOrgUsers() {
        return this.orgUsers;
    }

    public void setOrgUsers(Set<OrgUsers> orgUsers) {
        if (this.orgUsers != null) {
            this.orgUsers.forEach(i -> i.setOrgUsers(null));
        }
        if (orgUsers != null) {
            orgUsers.forEach(i -> i.setOrgUsers(this));
        }
        this.orgUsers = orgUsers;
    }

    public Organization orgUsers(Set<OrgUsers> orgUsers) {
        this.setOrgUsers(orgUsers);
        return this;
    }

    public Organization addOrgUsers(OrgUsers orgUsers) {
        this.orgUsers.add(orgUsers);
        orgUsers.setOrgUsers(this);
        return this;
    }

    public Organization removeOrgUsers(OrgUsers orgUsers) {
        this.orgUsers.remove(orgUsers);
        orgUsers.setOrgUsers(null);
        return this;
    }

    public Set<Clients> getClients() {
        return this.clients;
    }

    public void setClients(Set<Clients> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setOrgClients(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setOrgClients(this));
        }
        this.clients = clients;
    }

    public Organization clients(Set<Clients> clients) {
        this.setClients(clients);
        return this;
    }

    public Organization addClients(Clients clients) {
        this.clients.add(clients);
        clients.setOrgClients(this);
        return this;
    }

    public Organization removeClients(Clients clients) {
        this.clients.remove(clients);
        clients.setOrgClients(null);
        return this;
    }

    public Set<Products> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Products> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrgProducts(null));
        }
        if (products != null) {
            products.forEach(i -> i.setOrgProducts(this));
        }
        this.products = products;
    }

    public Organization products(Set<Products> products) {
        this.setProducts(products);
        return this;
    }

    public Organization addProducts(Products products) {
        this.products.add(products);
        products.setOrgProducts(this);
        return this;
    }

    public Organization removeProducts(Products products) {
        this.products.remove(products);
        products.setOrgProducts(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", orgId='" + getOrgId() + "'" +
            ", orgName='" + getOrgName() + "'" +
            ", legalOrgName='" + getLegalOrgName() + "'" +
            ", pan='" + getPan() + "'" +
            ", gstin='" + getGstin() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", emailId='" + getEmailId() + "'" +
            ", status='" + getStatus() + "'" +
            ", dateOfRegestation='" + getDateOfRegestation() + "'" +
            ", dateOfDeRegestation='" + getDateOfDeRegestation() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
