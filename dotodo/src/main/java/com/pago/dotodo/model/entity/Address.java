package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {
    private String street;
    private String zipCode;
    private Town town;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    @ManyToOne
    public Town getTown() {
        return town;
    }

    public Address setTown(Town town) {
        this.town = town;
        return this;
    }
}
