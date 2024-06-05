package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class AddressEntity extends BaseEntity {
    private String street;
    private String zipCode;
    private String town;
    private String country;

    public AddressEntity() {
    }

    public String getStreet() {
        return street;
    }

    public AddressEntity setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public AddressEntity setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public String getTown() {
        return town;
    }

    public AddressEntity setTown(String town) {
        this.town = town;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public AddressEntity setCountry(String country) {
        this.country = country;
        return this;
    }
}
