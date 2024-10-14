package com.pago.dotodo.user.repository;

import com.pago.dotodo.user.model.entity.RoleEntity;
import com.pago.dotodo.user.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findRoleEntityByRole(RoleEnum role);
}
