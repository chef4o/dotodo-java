package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="towns")
public class TownEntity extends BaseEntity {
    private String name;
    private Long population;
    private Integer isCapital;
    @ManyToOne
    private CountryEntity countryEntity;

}
