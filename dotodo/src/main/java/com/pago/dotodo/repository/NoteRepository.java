package com.pago.dotodo.repository;

import com.pago.dotodo.model.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findNotesByOwnerId(Long ownerId);

}
