package com.pago.dotodo.web.api;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.web.mvc.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteApiController extends BaseController {

    private final NoteService noteService;

    public NoteApiController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAll(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return ResponseEntity.ok(noteService.getAll(userDetails.getId()));
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
                                              @AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              UriComponentsBuilder uriComponentsBuilder) {
        long noteId = noteService.addNote(noteDto, userDetails.getId());

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/api/notes/{id}").build(noteId))
                .build();
    }
}
