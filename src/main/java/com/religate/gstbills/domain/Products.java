package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "product_hsn_code")
    private String productHsnCode;

    @Column(name = "product_tax_rate")
    private String productTaxRate;

    @Column(name = "unit_code_id")
    private String unitCodeId;

    @Column(name = "cost_per_qty")
    private Double costPerQty;

    @Column(name = "cgst")
    private String cgst;

    @Column(name = "sgst")
    private String sgst;

    @Column(name = "igst")
    private String igst;

    @Column(name = "total_tax_rate")
    private String totalTaxRate;

    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ProductUnit productUnit;

    @JsonIgnoreProperties(value = { "products", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "products")
    private InvoiceItems invoiceItems;

    @JsonIgnoreProperties(value = { "products", "orders" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "products")
    private OrdersItems ordersItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgUsers", "clients", "products" }, allowSetters = true)
    private Organization orgProducts;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Products id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return this.productId;
    }

    public Products productId(String productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return this.name;
    }

    public Products name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Products description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductHsnCode() {
        return this.productHsnCode;
    }

    public Products productHsnCode(String productHsnCode) {
        this.setProductHsnCode(productHsnCode);
        return this;
    }

    public void setProductHsnCode(String productHsnCode) {
        this.productHsnCode = productHsnCode;
    }

    public String getProductTaxRate() {
        return this.productTaxRate;
    }

    public Products productTaxRate(String productTaxRate) {
        this.setProductTaxRate(productTaxRate);
        return this;
    }

    public void setProductTaxRate(String productTaxRate) {
        this.productTaxRate = productTaxRate;
    }

    public String getUnitCodeId() {
        return this.unitCodeId;
    }

    public Products unitCodeId(String unitCodeId) {
        this.setUnitCodeId(unitCodeId);
        return this;
    }

    public void setUnitCodeId(String unitCodeId) {
        this.unitCodeId = unitCodeId;
    }

    public Double getCostPerQty() {
        return this.costPerQty;
    }

    public Products costPerQty(Double costPerQty) {
        this.setCostPerQty(costPerQty);
        return this;
    }

    public void setCostPerQty(Double costPerQty) {
        this.costPerQty = costPerQty;
    }

    public String getCgst() {
        return this.cgst;
    }

    public Products cgst(String cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return this.sgst;
    }

    public Products sgst(String sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getIgst() {
        return this.igst;
    }

    public Products igst(String igst) {
        this.setIgst(igst);
        return this;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getTotalTaxRate() {
        return this.totalTaxRate;
    }

    public Products totalTaxRate(String totalTaxRate) {
        this.setTotalTaxRate(totalTaxRate);
        return this;
    }

    public void setTotalTaxRate(String totalTaxRate) {
        this.totalTaxRate = totalTaxRate;
    }

    public ProductUnit getProductUnit() {
        return this.productUnit;
    }

    public void setProductUnit(ProductUnit productUnit) {
        this.productUnit = productUnit;
    }

    public Products productUnit(ProductUnit productUnit) {
        this.setProductUnit(productUnit);
        return this;
    }

    public InvoiceItems getInvoiceItems() {
        return this.invoiceItems;
    }

    public void setInvoiceItems(InvoiceItems invoiceItems) {
        if (this.invoiceItems != null) {
            this.invoiceItems.setProducts(null);
        }
        if (invoiceItems != null) {
            invoiceItems.setProducts(this);
        }
        this.invoiceItems = invoiceItems;
    }

    public Products invoiceItems(InvoiceItems invoiceItems) {
        this.setInvoiceItems(invoiceItems);
        return this;
    }

    public OrdersItems getOrdersItems() {
        return this.ordersItems;
    }

    public void setOrdersItems(OrdersItems ordersItems) {
        if (this.ordersItems != null) {
            this.ordersItems.setProducts(null);
        }
        if (ordersItems != null) {
            ordersItems.setProducts(this);
        }
        this.ordersItems = ordersItems;
    }

    public Products ordersItems(OrdersItems ordersItems) {
        this.setOrdersItems(ordersItems);
        return this;
    }

    public Organization getOrgProducts() {
        return this.orgProducts;
    }

    public void setOrgProducts(Organization organization) {
        this.orgProducts = organization;
    }

    public Products orgProducts(Organization organization) {
        this.setOrgProducts(organization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Products)) {
            return false;
        }
        return id != null && id.equals(((Products) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", productId='" + getProductId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", productHsnCode='" + getProductHsnCode() + "'" +
            ", productTaxRate='" + getProductTaxRate() + "'" +
            ", unitCodeId='" + getUnitCodeId() + "'" +
            ", costPerQty=" + getCostPerQty() +
            ", cgst='" + getCgst() + "'" +
            ", sgst='" + getSgst() + "'" +
            ", igst='" + getIgst() + "'" +
            ", totalTaxRate='" + getTotalTaxRate() + "'" +
            "}";
    }
}
