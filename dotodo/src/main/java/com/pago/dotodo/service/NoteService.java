package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.NoteEditDto;
import com.pago.dotodo.model.entity.NoteEntity;
import com.pago.dotodo.model.error.ObjectNotFoundException;
import com.pago.dotodo.repository.NoteRepository;
import com.pago.dotodo.util.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;
    private final AuthService authService;

    @Autowired
    public NoteService(NoteRepository noteRepository,
                       UserService userService,
                       ModelMapper modelMapper,
                       DateTimeUtil dateTimeUtil,
                       AuthService authService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.dateTimeUtil = dateTimeUtil;
        this.authService = authService;
    }

    public List<NoteDto> getAll(Long userId) {
        return this.noteRepository
                .findNotesByOwnerId(userId)
                .stream()
                .map(note -> modelMapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    public NoteDto getById(Long noteId, Long currentUserId) {

        NoteEntity noteEntity = this.noteRepository.findById(noteId)
                .orElseThrow(() -> new ObjectNotFoundException("note", noteId));

        authService.validateOwnerAccess(noteEntity.getOwner().getId(), currentUserId);

        return modelMapper.map(noteEntity, NoteDto.class);
    }

    public List<NoteDto> getByUserIdOrderByInsTimeDesc(Long userId) {
        List<NoteDto> notes = this.noteRepository
                .findByOwnerIdOrderByDueDateDesc(userId)
                .stream()
                .map(note -> modelMapper.map(note, NoteDto.class))
                .collect(Collectors.toList());

        if (notes.isEmpty()) {
            return notes;
        }
        return dateTimeUtil.addDueDaysHours(notes);
    }

    public void deleteById(Long noteId, Long currentUserId) {
        NoteDto noteDto = this.getById(noteId, currentUserId);
        noteRepository.deleteById(noteDto.getId());
    }

    public long addNote(NoteDto noteDto, Long userId) {
        NoteEntity newNote = new NoteEntity()
                .setTitle(noteDto.getTitle())
                .setContent(noteDto.getContent())
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setDueDateOnly(noteDto.getDueTime().isBlank())
                .setDueDate(dateTimeUtil
                        .formatToDateTime(noteDto.getDueDate(), noteDto.getDueTime(),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setTrackProgress("New")
                .setOwner(userService.getUserById(userId))
                .setPeers(new HashSet<>());

        return noteRepository.saveAndFlush(newNote).getId();
    }

    public void editNote(Long noteId, NoteEditDto editedNote, Long userId) {
        NoteEntity existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new ObjectNotFoundException("note", noteId));

        authService.validateOwnerAccess(existingNote.getOwner().getId(), userId);

        if (!existingNote.getTitle().equals(editedNote.getTitle())) {
            existingNote.setTitle(editedNote.getTitle());
        }

        if (!existingNote.getContent().equals(editedNote.getContent())) {
            existingNote.setContent(editedNote.getContent());
        }

        if (dateTimeUtil.datesDiffer(dateTimeUtil.formatDateToString(existingNote.getDueDate()), editedNote.getDueDate())) {
            if (!editedNote.getDueDate().isBlank()) {
                existingNote.setDueDate(
                        dateTimeUtil.formatToDateTime(
                                editedNote.getDueDate(),
                                editedNote.getDueTime(),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                existingNote.setDueDate(null);
            }
        }

        if (existingNote.getDueDateOnly() && !editedNote.getDueTime().isBlank()) {
            existingNote.setDueDateOnly(false);
        }

        noteRepository.saveAndFlush(existingNote);
    }

    public List<String> getExpiringNotesEmails() {
        return noteRepository.findAll()
                .stream()
                .filter(note -> isExpiring(note.getDueDate()))
                .map(note -> note.getOwner().getEmail())
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean isExpiring(LocalDateTime dueDate) {
        if (dueDate == null) {
            return false;
        }
        return dueDate.toLocalDate().isEqual(LocalDate.now());
    }

    public List<NoteDto> getExpiringNotes(Long userId, int numberOfResults) {
        PageRequest pageRequest = PageRequest.of(0, numberOfResults);
        return this.noteRepository
                .findNotesByOwnerIdOrderByDueDate(userId, pageRequest)
                .stream()
                .map(event -> modelMapper.map(event, NoteDto.class))
                .collect(Collectors.toList());
    }
}
