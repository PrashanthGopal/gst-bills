package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.religate.gstbills.domain.enumeration.InvoiceStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice_id")
    private String invoiceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status;

    @Column(name = "create_date_time")
    private ZonedDateTime createDateTime;

    @Column(name = "update_date_time")
    private ZonedDateTime updateDateTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "total_igst_rate")
    private String totalIgstRate;

    @Column(name = "total_cgst_rate")
    private String totalCgstRate;

    @Column(name = "total_sgst_rate")
    private String totalSgstRate;

    @Column(name = "total_tax_rate")
    private String totalTaxRate;

    @Column(name = "total_ass_amount")
    private String totalAssAmount;

    @Column(name = "total_no_items")
    private String totalNoItems;

    @Column(name = "grand_total")
    private Double grandTotal;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "mode_of_transaction")
    private String modeOfTransaction;

    @Column(name = "trans_type")
    private String transType;

    @Column(name = "vehicle_no")
    private String vehicleNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Transporter transporter;

    @JsonIgnoreProperties(value = { "stateCode", "organization", "clients", "transporter", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products", "invoice" }, allowSetters = true)
    @JoinColumn(unique = true)
    private Set<InvoiceItems> invoiceItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgUsers", "clients", "products" }, allowSetters = true)
    private Organization orgInvoices;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgClients" }, allowSetters = true)
    private Clients clients;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return this.invoiceId;
    }

    public Invoice invoiceId(String invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceStatus getStatus() {
        return this.status;
    }

    public Invoice status(InvoiceStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreateDateTime() {
        return this.createDateTime;
    }

    public Invoice createDateTime(ZonedDateTime createDateTime) {
        this.setCreateDateTime(createDateTime);
        return this;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public ZonedDateTime getUpdateDateTime() {
        return this.updateDateTime;
    }

    public Invoice updateDateTime(ZonedDateTime updateDateTime) {
        this.setUpdateDateTime(updateDateTime);
        return this;
    }

    public void setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Invoice createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Invoice updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getTotalIgstRate() {
        return this.totalIgstRate;
    }

    public Invoice totalIgstRate(String totalIgstRate) {
        this.setTotalIgstRate(totalIgstRate);
        return this;
    }

    public void setTotalIgstRate(String totalIgstRate) {
        this.totalIgstRate = totalIgstRate;
    }

    public String getTotalCgstRate() {
        return this.totalCgstRate;
    }

    public Invoice totalCgstRate(String totalCgstRate) {
        this.setTotalCgstRate(totalCgstRate);
        return this;
    }

    public void setTotalCgstRate(String totalCgstRate) {
        this.totalCgstRate = totalCgstRate;
    }

    public String getTotalSgstRate() {
        return this.totalSgstRate;
    }

    public Invoice totalSgstRate(String totalSgstRate) {
        this.setTotalSgstRate(totalSgstRate);
        return this;
    }

    public void setTotalSgstRate(String totalSgstRate) {
        this.totalSgstRate = totalSgstRate;
    }

    public String getTotalTaxRate() {
        return this.totalTaxRate;
    }

    public Invoice totalTaxRate(String totalTaxRate) {
        this.setTotalTaxRate(totalTaxRate);
        return this;
    }

    public void setTotalTaxRate(String totalTaxRate) {
        this.totalTaxRate = totalTaxRate;
    }

    public String getTotalAssAmount() {
        return this.totalAssAmount;
    }

    public Invoice totalAssAmount(String totalAssAmount) {
        this.setTotalAssAmount(totalAssAmount);
        return this;
    }

    public void setTotalAssAmount(String totalAssAmount) {
        this.totalAssAmount = totalAssAmount;
    }

    public String getTotalNoItems() {
        return this.totalNoItems;
    }

    public Invoice totalNoItems(String totalNoItems) {
        this.setTotalNoItems(totalNoItems);
        return this;
    }

    public void setTotalNoItems(String totalNoItems) {
        this.totalNoItems = totalNoItems;
    }

    public Double getGrandTotal() {
        return this.grandTotal;
    }

    public Invoice grandTotal(Double grandTotal) {
        this.setGrandTotal(grandTotal);
        return this;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getDistance() {
        return this.distance;
    }

    public Invoice distance(Double distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getModeOfTransaction() {
        return this.modeOfTransaction;
    }

    public Invoice modeOfTransaction(String modeOfTransaction) {
        this.setModeOfTransaction(modeOfTransaction);
        return this;
    }

    public void setModeOfTransaction(String modeOfTransaction) {
        this.modeOfTransaction = modeOfTransaction;
    }

    public String getTransType() {
        return this.transType;
    }

    public Invoice transType(String transType) {
        this.setTransType(transType);
        return this;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getVehicleNo() {
        return this.vehicleNo;
    }

    public Invoice vehicleNo(String vehicleNo) {
        this.setVehicleNo(vehicleNo);
        return this;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Transporter getTransporter() {
        return this.transporter;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    public Invoice transporter(Transporter transporter) {
        this.setTransporter(transporter);
        return this;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Invoice address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<InvoiceItems> getInvoiceItems() {
        return this.invoiceItems;
    }

    public void setInvoiceItems(Set<InvoiceItems> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public Invoice invoiceItems(Set<InvoiceItems> invoiceItems) {
        this.setInvoiceItems(invoiceItems);
        return this;
    }

    public Invoice addInvoiceItems(InvoiceItems invoiceItems) {
        this.invoiceItems.add(invoiceItems);
        return this;
    }

    public Invoice removeInvoiceItems(InvoiceItems invoiceItems) {
        this.invoiceItems.remove(invoiceItems);
        return this;
    }

    public Organization getOrgInvoices() {
        return this.orgInvoices;
    }

    public void setOrgInvoices(Organization organization) {
        this.orgInvoices = organization;
    }

    public Invoice orgInvoices(Organization organization) {
        this.setOrgInvoices(organization);
        return this;
    }

    public Clients getClients() {
        return this.clients;
    }

    public void setClients(Clients clients) {
        this.clients = clients;
    }

    public Invoice clients(Clients clients) {
        this.setClients(clients);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceId='" + getInvoiceId() + "'" +
            ", status='" + getStatus() + "'" +
            ", createDateTime='" + getCreateDateTime() + "'" +
            ", updateDateTime='" + getUpdateDateTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", totalIgstRate='" + getTotalIgstRate() + "'" +
            ", totalCgstRate='" + getTotalCgstRate() + "'" +
            ", totalSgstRate='" + getTotalSgstRate() + "'" +
            ", totalTaxRate='" + getTotalTaxRate() + "'" +
            ", totalAssAmount='" + getTotalAssAmount() + "'" +
            ", totalNoItems='" + getTotalNoItems() + "'" +
            ", grandTotal=" + getGrandTotal() +
            ", distance=" + getDistance() +
            ", modeOfTransaction='" + getModeOfTransaction() + "'" +
            ", transType='" + getTransType() + "'" +
            ", vehicleNo='" + getVehicleNo() + "'" +
            "}";
    }
}
