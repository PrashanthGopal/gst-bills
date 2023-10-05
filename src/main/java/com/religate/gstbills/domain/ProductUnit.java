package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ProductUnit.
 */
@Entity
@Table(name = "product_unit")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "unit_code_id")
    private String unitCodeId;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "unit_code_description")
    private String unitCodeDescription;

    @JsonIgnoreProperties(value = { "productUnit", "invoiceItems", "ordersItems", "orgProducts" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "productUnit")
    private Products products;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductUnit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitCodeId() {
        return this.unitCodeId;
    }

    public ProductUnit unitCodeId(String unitCodeId) {
        this.setUnitCodeId(unitCodeId);
        return this;
    }

    public void setUnitCodeId(String unitCodeId) {
        this.unitCodeId = unitCodeId;
    }

    public String getUnitCode() {
        return this.unitCode;
    }

    public ProductUnit unitCode(String unitCode) {
        this.setUnitCode(unitCode);
        return this;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitCodeDescription() {
        return this.unitCodeDescription;
    }

    public ProductUnit unitCodeDescription(String unitCodeDescription) {
        this.setUnitCodeDescription(unitCodeDescription);
        return this;
    }

    public void setUnitCodeDescription(String unitCodeDescription) {
        this.unitCodeDescription = unitCodeDescription;
    }

    public Products getProducts() {
        return this.products;
    }

    public void setProducts(Products products) {
        if (this.products != null) {
            this.products.setProductUnit(null);
        }
        if (products != null) {
            products.setProductUnit(this);
        }
        this.products = products;
    }

    public ProductUnit products(Products products) {
        this.setProducts(products);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductUnit)) {
            return false;
        }
        return id != null && id.equals(((ProductUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductUnit{" +
            "id=" + getId() +
            ", unitCodeId='" + getUnitCodeId() + "'" +
            ", unitCode='" + getUnitCode() + "'" +
            ", unitCodeDescription='" + getUnitCodeDescription() + "'" +
            "}";
    }
}
