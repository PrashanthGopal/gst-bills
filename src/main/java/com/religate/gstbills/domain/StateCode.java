package com.religate.gstbills.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A StateCode.
 */
@Entity
@Table(name = "state_code")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StateCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "state_code_id")
    private String stateCodeId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties(value = { "stateCode", "organization", "clients", "transporter", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "stateCode")
    private Address address;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StateCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStateCodeId() {
        return this.stateCodeId;
    }

    public StateCode stateCodeId(String stateCodeId) {
        this.setStateCodeId(stateCodeId);
        return this;
    }

    public void setStateCodeId(String stateCodeId) {
        this.stateCodeId = stateCodeId;
    }

    public String getCode() {
        return this.code;
    }

    public StateCode code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public StateCode name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        if (this.address != null) {
            this.address.setStateCode(null);
        }
        if (address != null) {
            address.setStateCode(this);
        }
        this.address = address;
    }

    public StateCode address(Address address) {
        this.setAddress(address);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StateCode)) {
            return false;
        }
        return id != null && id.equals(((StateCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StateCode{" +
            "id=" + getId() +
            ", stateCodeId='" + getStateCodeId() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
