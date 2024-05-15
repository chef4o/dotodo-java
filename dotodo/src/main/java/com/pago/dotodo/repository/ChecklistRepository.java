package com.pago.dotodo.repository;

import com.pago.dotodo.model.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findChecklistByOwnerId(Long ownerId);
}
