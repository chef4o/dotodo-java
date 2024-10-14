package com.pago.dotodo.event.repository;

import com.pago.dotodo.event.model.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findEventsByOwnerId(Long ownerId);
}
