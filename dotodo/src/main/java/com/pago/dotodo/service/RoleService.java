package com.pago.dotodo.service;

import com.pago.dotodo.model.entity.RoleEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RoleService {

    public RoleEnum getHighestRole(List<RoleEntity> roles) {
        return roles
                .stream()
                .map(RoleEntity::getRole)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public boolean hasLowerRole(List<RoleEntity> roles, RoleEnum roleToCompare) {
        RoleEnum highestCurrent = getHighestRole(roles);

        return highestCurrent != null
                && roleToCompare != null
                && highestCurrent.ordinal() < roleToCompare.ordinal();
    }
}
