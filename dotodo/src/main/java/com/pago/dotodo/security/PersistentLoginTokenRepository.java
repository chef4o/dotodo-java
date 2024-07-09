package com.pago.dotodo.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistentLoginTokenRepository extends JpaRepository<PersistentLoginToken, String> {
}
