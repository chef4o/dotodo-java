package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name="towns")
public class Town extends BaseEntity {
    private String name;
    private Long population;
    private Integer isCapital;
    private Country country;

    public Town() {
    }

    public String getName() {
        return name;
    }

    public Town setName(String name) {
        this.name = name;
        return this;
    }

    public Long getPopulation() {
        return population;
    }

    public Town setPopulation(Long population) {
        this.population = population;
        return this;
    }

    public Integer getIsCapital() {
        return isCapital;
    }

    public Town setIsCapital(Integer isCapital) {
        this.isCapital = isCapital;
        return this;
    }

    @ManyToOne
    public Country getCountry() {
        return country;
    }

    public Town setCountry(Country country) {
        this.country = country;
        return this;
    }
}
