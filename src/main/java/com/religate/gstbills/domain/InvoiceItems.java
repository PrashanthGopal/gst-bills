package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A InvoiceItems.
 */
@Entity
@Table(name = "invoice_items")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "invoice_items_id")
    private String invoiceItemsId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "cgst")
    private String cgst;

    @Column(name = "sgst")
    private String sgst;

    @Column(name = "gst")
    private String gst;

    @Column(name = "total_amount")
    private Double totalAmount;

    @JsonIgnoreProperties(value = { "productUnit", "invoiceItems", "ordersItems", "orgProducts" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "transporter", "address", "invoiceItems", "orgInvoices", "clients" }, allowSetters = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceItems id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public InvoiceItems orgId(String orgId) {
        this.setOrgId(orgId);
        return this;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getInvoiceItemsId() {
        return this.invoiceItemsId;
    }

    public InvoiceItems invoiceItemsId(String invoiceItemsId) {
        this.setInvoiceItemsId(invoiceItemsId);
        return this;
    }

    public void setInvoiceItemsId(String invoiceItemsId) {
        this.invoiceItemsId = invoiceItemsId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public InvoiceItems quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCgst() {
        return this.cgst;
    }

    public InvoiceItems cgst(String cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return this.sgst;
    }

    public InvoiceItems sgst(String sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getGst() {
        return this.gst;
    }

    public InvoiceItems gst(String gst) {
        this.setGst(gst);
        return this;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public InvoiceItems totalAmount(Double totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Products getProducts() {
        return this.products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public InvoiceItems products(Products products) {
        this.setProducts(products);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceItems invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItems)) {
            return false;
        }
        return id != null && id.equals(((InvoiceItems) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItems{" +
            "id=" + getId() +
            ", orgId='" + getOrgId() + "'" +
            ", invoiceItemsId='" + getInvoiceItemsId() + "'" +
            ", quantity=" + getQuantity() +
            ", cgst='" + getCgst() + "'" +
            ", sgst='" + getSgst() + "'" +
            ", gst='" + getGst() + "'" +
            ", totalAmount=" + getTotalAmount() +
            "}";
    }
}
