package com.pago.dotodo.web;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController extends BaseController {

    private final NoteService noteService;
    private final ModelAndViewParser attributeBuilder;

    public NoteController(NoteService noteService, ModelAndViewParser attributeBuilder) {
        this.noteService = noteService;
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAll() {
        return ResponseEntity.ok(noteService.getAll(1L)); //TODO: change ID
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
                        .path("/api/notes/{id}").build(noteId))
                .build();
    }
}
