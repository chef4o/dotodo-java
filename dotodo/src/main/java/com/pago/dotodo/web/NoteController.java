package com.pago.dotodo.web;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserTokenDto loggedUser;

    public NoteController(NoteService noteService, UserTokenDto loggedUser) {
        this.noteService = noteService;
        this.loggedUser = loggedUser;
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAll() {
        return ResponseEntity.ok(noteService.getAll(this.loggedUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getById(@PathVariable("id") Long id) {
        Optional<NoteDto> currentNote = noteService.getById(id);

        return currentNote.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoteDto> deleteById(@PathVariable("id") Long id) {
        noteService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto,
                                              UriComponentsBuilder uriComponentsBuilder) {
        long noteId = noteService.addNote(noteDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/api/books/{id}").build(noteId))
                .build();
    }
}
