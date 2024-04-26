package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity {
    private String isoCode;
    private String name;
    private String continent;
    private String region;
    private Long population;

    public Country() {
    }

    public String getIsoCode() {
        return isoCode;
    }

    public Country setIsoCode(String isoCode) {
        this.isoCode = isoCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public Country setName(String name) {
        this.name = name;
        return this;
    }

    public String getContinent() {
        return continent;
    }

    public Country setContinent(String continent) {
        this.continent = continent;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Country setRegion(String region) {
        this.region = region;
        return this;
    }

    public Long getPopulation() {
        return population;
    }

    public Country setPopulation(Long population) {
        this.population = population;
        return this;
    }
}
