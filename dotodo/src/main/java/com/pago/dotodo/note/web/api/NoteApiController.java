package com.pago.dotodo.note.web.api;

import com.pago.dotodo.note.model.dto.NoteDto;
import com.pago.dotodo.common.security.CustomAuthUserDetails;
import com.pago.dotodo.note.service.NoteService;
import com.pago.dotodo.common.web.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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
    public ResponseEntity<NoteDto> getById(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                           @PathVariable("id") Long id) {
        NoteDto currentNote = noteService.getById(id, userDetails.getId());

        return ResponseEntity.ok(currentNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoteDto> deleteById(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable("id") Long id) {
        noteService.deleteById(id, userDetails.getId());
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
