package com.pago.dotodo.user.model.entity;

import com.pago.dotodo.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public String toString() {
        return Stream.of(street, zipCode, town, country)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));
    }
}
