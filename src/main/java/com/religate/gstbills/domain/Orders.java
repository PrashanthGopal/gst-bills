package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.religate.gstbills.domain.enumeration.OrdersEnum;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Orders.
 */
@Entity
@Table(name = "orders")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrdersEnum status;

    @Column(name = "orders_items_id")
    private String ordersItemsId;

    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgClients" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Clients clients;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orders")
    @JsonIgnoreProperties(value = { "products", "orders" }, allowSetters = true)
    private Set<OrdersItems> ordersItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "address", "invoices", "orders", "orgUsers", "clients", "products" }, allowSetters = true)
    private Organization orgOrders;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public Orders orderId(String orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return this.title;
    }

    public Orders title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Orders description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrdersEnum getStatus() {
        return this.status;
    }

    public Orders status(OrdersEnum status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrdersEnum status) {
        this.status = status;
    }

    public String getOrdersItemsId() {
        return this.ordersItemsId;
    }

    public Orders ordersItemsId(String ordersItemsId) {
        this.setOrdersItemsId(ordersItemsId);
        return this;
    }

    public void setOrdersItemsId(String ordersItemsId) {
        this.ordersItemsId = ordersItemsId;
    }

    public Clients getClients() {
        return this.clients;
    }

    public void setClients(Clients clients) {
        this.clients = clients;
    }

    public Orders clients(Clients clients) {
        this.setClients(clients);
        return this;
    }

    public Set<OrdersItems> getOrdersItems() {
        return this.ordersItems;
    }

    public void setOrdersItems(Set<OrdersItems> ordersItems) {
        if (this.ordersItems != null) {
            this.ordersItems.forEach(i -> i.setOrders(null));
        }
        if (ordersItems != null) {
            ordersItems.forEach(i -> i.setOrders(this));
        }
        this.ordersItems = ordersItems;
    }

    public Orders ordersItems(Set<OrdersItems> ordersItems) {
        this.setOrdersItems(ordersItems);
        return this;
    }

    public Orders addOrdersItems(OrdersItems ordersItems) {
        this.ordersItems.add(ordersItems);
        ordersItems.setOrders(this);
        return this;
    }

    public Orders removeOrdersItems(OrdersItems ordersItems) {
        this.ordersItems.remove(ordersItems);
        ordersItems.setOrders(null);
        return this;
    }

    public Organization getOrgOrders() {
        return this.orgOrders;
    }

    public void setOrgOrders(Organization organization) {
        this.orgOrders = organization;
    }

    public Orders orgOrders(Organization organization) {
        this.setOrgOrders(organization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orders)) {
            return false;
        }
        return id != null && id.equals(((Orders) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orders{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", ordersItemsId='" + getOrdersItemsId() + "'" +
            "}";
    }
}
