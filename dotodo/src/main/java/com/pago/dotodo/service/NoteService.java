package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.model.entity.Note;
import com.pago.dotodo.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserService userService;
    private final UserTokenDto loggedUser;

    public NoteService(NoteRepository noteRepository, UserService userService, UserTokenDto loggedUser) {
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.loggedUser = loggedUser;
    }

    public List<NoteDto> getAll(Long userId) {
        return this.noteRepository
                .findNotesByOwnerId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<NoteDto> getById(Long noteId) {
        return this.noteRepository
                .findById(noteId)
                .map(this::mapToDto);
    }

    public void deleteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    public long addNote(NoteDto noteDto) {
        Note newNote = new Note()
                .setTitle(noteDto.getTitle())
                .setContent(noteDto.getContent())
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setTrackProgress("New")
                .setOwner(userService
                        .getUser(loggedUser.getId())
                        .orElseThrow())
                .setPeers(new HashSet<>());

        return noteRepository.save(newNote).getId();
    }

    private NoteDto mapToDto(Note note) {
        return new NoteDto()
                .setTitle(note.getTitle())
                .setContent(note.getContent())
                .setArchived(note.getArchived())
                .setStartDate(note.getStartDate())
                .setDueDate(note.getDueDate())
                .setCompletedOn(note.getCompletedOn())
                .setTrackProgress(note.getTrackProgress());
    }
}
