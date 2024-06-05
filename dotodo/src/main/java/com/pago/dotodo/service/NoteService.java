package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.entity.NoteEntity;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.repository.NoteRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public NoteService(NoteRepository noteRepository,
                       UserService userService,
                       ModelMapper modelMapper) {
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<NoteDto> getAll(Long userId) {
        return this.noteRepository
                .findNotesByOwnerId(userId)
                .stream()
                .map(note -> modelMapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    public Optional<NoteDto> getById(Long noteId) {
        return Optional.ofNullable(modelMapper.map(this.noteRepository
                .findById(noteId), NoteDto.class));
    }

    public void deleteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    public long addNote(NoteDto noteDto) {
        NoteEntity newNote = new NoteEntity()
                .setTitle(noteDto.getTitle())
                .setContent(noteDto.getContent())
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setTrackProgress("New")
                .setOwner(modelMapper
                        .map(userService.getUserById(1L), UserEntity.class)) //TODO: change ID
                .setPeers(new HashSet<>());

        return noteRepository.save(newNote).getId();
    }
}
