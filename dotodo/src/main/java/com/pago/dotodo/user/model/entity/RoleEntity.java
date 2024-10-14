package com.pago.dotodo.user.model.entity;

import com.pago.dotodo.common.model.BaseEntity;
import com.pago.dotodo.user.model.enums.RoleEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="roles")
public class RoleEntity extends BaseEntity {

    private RoleEnum role;

    @NotNull
    @Enumerated(EnumType.STRING)
    public RoleEnum getRole() {
        return role;
    }

    public RoleEntity setRole(RoleEnum role) {
        this.role = role;
        return this;
    }
}
