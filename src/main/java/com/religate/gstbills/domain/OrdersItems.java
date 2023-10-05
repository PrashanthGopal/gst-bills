package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A OrdersItems.
 */
@Entity
@Table(name = "orders_items")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdersItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "order_items_id")
    private String orderItemsId;

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
    @JsonIgnoreProperties(value = { "clients", "ordersItems", "orgOrders" }, allowSetters = true)
    private Orders orders;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrdersItems id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public OrdersItems orgId(String orgId) {
        this.setOrgId(orgId);
        return this;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrderItemsId() {
        return this.orderItemsId;
    }

    public OrdersItems orderItemsId(String orderItemsId) {
        this.setOrderItemsId(orderItemsId);
        return this;
    }

    public void setOrderItemsId(String orderItemsId) {
        this.orderItemsId = orderItemsId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrdersItems quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCgst() {
        return this.cgst;
    }

    public OrdersItems cgst(String cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return this.sgst;
    }

    public OrdersItems sgst(String sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getGst() {
        return this.gst;
    }

    public OrdersItems gst(String gst) {
        this.setGst(gst);
        return this;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public OrdersItems totalAmount(Double totalAmount) {
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

    public OrdersItems products(Products products) {
        this.setProducts(products);
        return this;
    }

    public Orders getOrders() {
        return this.orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public OrdersItems orders(Orders orders) {
        this.setOrders(orders);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdersItems)) {
            return false;
        }
        return id != null && id.equals(((OrdersItems) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdersItems{" +
            "id=" + getId() +
            ", orgId='" + getOrgId() + "'" +
            ", orderItemsId='" + getOrderItemsId() + "'" +
            ", quantity=" + getQuantity() +
            ", cgst='" + getCgst() + "'" +
            ", sgst='" + getSgst() + "'" +
            ", gst='" + getGst() + "'" +
            ", totalAmount=" + getTotalAmount() +
            "}";
    }
}
