package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class CountryEntity extends BaseEntity {
    private String isoCode;
    private String name;
    private String continent;
    private String region;
    private Long population;
}
