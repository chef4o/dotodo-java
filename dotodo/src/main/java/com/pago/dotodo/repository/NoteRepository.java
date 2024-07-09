package com.pago.dotodo.repository;

import com.pago.dotodo.model.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    List<NoteEntity> findNotesByOwnerId(Long ownerId);
    List<NoteEntity> findByOwnerIdOrderByStartDateDesc(Long ownerId);
}
