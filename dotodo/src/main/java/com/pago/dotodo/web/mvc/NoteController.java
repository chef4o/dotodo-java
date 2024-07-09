package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/notes")
public class NoteController extends BaseController {

    private static final String PAGE_NAME = "notes";
    private final NoteService noteService;
    private final ModelAndViewParser attributeBuilder;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public NoteController(NoteService noteService, ModelAndViewParser attributeBuilder) {
        this.noteService = noteService;
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getNotesPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                     @RequestParam(required = false) Long editNoteId,
                                     @RequestParam(required = false) Long viewNoteId) {

        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "editNoteId", editNoteId,
                "viewNoteId", viewNoteId,
                "notes", noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
        );
    }

    @GetMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @ModelAttribute NoteDto noteDto,
                                @RequestParam(required = false) String emptyValueError) {

        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "emptyValueError", emptyValueError,
                "noteData", noteDto,
                "createNewNote", true,
                "notes", noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
        );
    }

    @PostMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @ModelAttribute NoteDto noteDto) {

        if (noteDto.getTitle().isBlank() || noteDto.getContent().isBlank()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "emptyValueError", "Title and content are both mandatory",
                    "noteData", noteDto,
                    "createNewNote", true,
                    "notes", noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
            );
        }

        try {
            noteService.addNote(noteDto, userDetails.getId());

            return super.redirect("/notes");
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "error", e.getMessage(),
                    "note", noteDto)
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @PathVariable Long id) {

        if (noteService.getById(id).get().getOwnerId().equals(userDetails.getId())) {
            noteService.deleteById(id);
        }
        return super.redirect("/notes");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditNotePage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                        @PathVariable Long id,
                                        @ModelAttribute NoteDto noteToEdit) {

        if (noteService.getById(id).get().getOwnerId().equals(userDetails.getId())) {
            return super.redirect("/notes", attributeBuilder.build(
                    "editNoteId", id,
                    "noteToEdit", noteService.getById(id)));
        }
        return super.redirect("/notes");
    }

    @GetMapping("/view/{id}")
    public ModelAndView getViewNoteDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                        @PathVariable Long id) {

        if (noteService.getById(id).get().getOwnerId().equals(userDetails.getId())) {
            return super.redirect("/notes", attributeBuilder.build(
                    "viewNoteId", id,
                    "detailedNote", noteService.getById(id)));
        }
        return super.redirect("/notes");
    }

    @ModelAttribute("noteData")
    public NoteDto noteData() {
        return new NoteDto();
    }

    @ModelAttribute("noteToEdit")
    public NoteDto noteToEdit(@RequestParam(required = false) Long editNoteId) {
        return editNoteId != null ? noteService.getById(editNoteId).orElse(null) : null;
    }

    @ModelAttribute("detailedNote")
    public NoteDto detailedNote(@RequestParam(required = false) Long viewNoteId) {
        return viewNoteId != null ? noteService.getById(viewNoteId).orElse(null) : null;
    }
}