package com.pago.dotodo.checklist.repository;

import com.pago.dotodo.checklist.model.entity.ChecklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
    List<ChecklistEntity> findChecklistByOwnerId(Long ownerId);
}
