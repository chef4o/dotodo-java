package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="addresses")
public class AddressEntity extends BaseEntity{
    private String street;
    @ManyToOne
    private TownEntity town;
}
