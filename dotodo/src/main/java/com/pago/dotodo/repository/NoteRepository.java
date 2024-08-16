package com.pago.dotodo.repository;

import com.pago.dotodo.model.entity.NoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    List<NoteEntity> findNotesByOwnerId(Long ownerId);

    List<NoteEntity> findByOwnerIdOrderByDueDateDesc(Long ownerId);

    @Query("SELECT n FROM NoteEntity n WHERE n.owner.id = :ownerId AND n.dueDate IS NOT NULL ORDER BY n.dueDate")
    List<NoteEntity> findNotesByOwnerIdOrderByDueDate(@Param("ownerId") Long ownerId, Pageable pageable);

}
