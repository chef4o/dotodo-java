package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.NoteEditDto;
import com.pago.dotodo.model.error.ObjectNotFoundException;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.util.DateTimeUtil;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notes")
public class NoteController extends BaseController {

    private static final String PAGE_NAME = "notes";
    private final NoteService noteService;
    private final ModelAndViewParser attributeBuilder;
    private final DateTimeUtil dateTimeUtil;

    public NoteController(NoteService noteService, ModelAndViewParser attributeBuilder, DateTimeUtil dateTimeUtil) {
        this.noteService = noteService;
        this.attributeBuilder = attributeBuilder;
        this.dateTimeUtil = dateTimeUtil;
    }

    @GetMapping
    public ModelAndView getNotesPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                     @RequestParam(required = false) Long editNoteId,
                                     @RequestParam(required = false) Long viewNoteId) {

        List<NoteDto> byUserIdOrderByInsTimeDesc = noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId());

        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "editNoteId", editNoteId,
                "viewNoteId", viewNoteId,
                "notes", byUserIdOrderByInsTimeDesc)
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

        final String valueError;

        if (noteDto.getTitle().isBlank() || noteDto.getContent().isBlank()) {
            valueError = "Title and content are both mandatory";
        } else if (noteDto.getDueDate() == null || noteDto.getDueDate().isBlank()
                && (noteDto.getDueTime() != null & !noteDto.getDueTime().isBlank())) {
            valueError = "Due time cannot be added without a date";
        } else if (noteDto.getDueDate() != null && !noteDto.getDueDate().isBlank()
                && !dateTimeUtil.isInFuture(noteDto.getDueDate(), noteDto.getDueTime())) {
            valueError = "Due date must be in the future";
        } else {
            valueError = "";
        }

        if (!valueError.isBlank()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "valueError", valueError,
                    "noteData", noteDto,
                    "createNewNote", true,
                    "notes", noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
            );
        }

        noteService.addNote(noteDto, userDetails.getId());

        return super.redirect("/notes");
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @PathVariable Long id) {
        noteService.deleteById(id, userDetails.getId());

        return super.redirect("/notes");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditNotePage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                        @PathVariable Long id,
                                        @ModelAttribute NoteDto noteToEdit) {
        return super.redirect("/notes", attributeBuilder.build(
                "editNoteId", id,
                "noteToEdit", noteToEdit));
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                 @PathVariable Long id,
                                 @Valid @ModelAttribute NoteEditDto noteEditDto,
                                 BindingResult bindingResult) {

        Map<String, String> valueErrors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                valueErrors.put(error.getField(), error.getField() + " " + error.getDefaultMessage());
            });
        }

        loadCustomErrors(valueErrors, noteEditDto);

        if (!valueErrors.isEmpty()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "valueErrors", valueErrors,
                    "noteToEdit", noteEditDto,
                    "editNoteId", id,
                    "notes", noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
            );
        }

        noteService.editNote(id, noteEditDto, userDetails.getId());

        return super.redirect("/notes");
    }


    @GetMapping("/view/{id}")
    public ModelAndView getViewNoteDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable Long id) {

        NoteDto detailedNote = noteService.getById(id, userDetails.getId());

        return super.redirect("/notes", attributeBuilder.build(
                "viewNoteId", id,
                "detailedNote", detailedNote));
    }

    @ModelAttribute("noteData")
    public NoteDto noteData() {
        return new NoteDto();
    }

    @ModelAttribute("noteEditDto")
    public NoteEditDto noteEditDto() {
        return new NoteEditDto();
    }

    @ModelAttribute("noteToEdit")
    public NoteDto noteToEdit(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                              @RequestParam(required = false) Long editNoteId) {

        if (editNoteId == null) {
            return null;
        }

        NoteDto noteToEdit = noteService.getById(editNoteId, userDetails.getId());

        if (noteToEdit.getDueDate() != null && !noteToEdit.getDueDate().isEmpty()) {
            noteToEdit.setDueDate(
                    dateTimeUtil.formatToISODate(noteToEdit.getDueDate(), "dd/MM/yyyy")
            );
        }

        return noteToEdit;
    }

    @ModelAttribute("detailedNote")
    public NoteDto detailedNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @RequestParam(required = false) Long viewNoteId) {
        return viewNoteId != null ? noteService.getById(viewNoteId, userDetails.getId()) : null;
    }

    private void loadCustomErrors(Map<String, String> errors, NoteEditDto noteEditDto) {
        if (noteEditDto.getDueDate() != null && !noteEditDto.getDueDate().isBlank()
                && !dateTimeUtil.isInFuture(noteEditDto.getDueDate(), noteEditDto.getDueTime())) {
            errors.put("date", "Due date must be in the future");
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException e) {
        return new ModelAndView("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "errorCode", "404",
                "serverError", e.getMessage())
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        return new ModelAndView("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "errorCode", "403",
                "serverError", e.getMessage())
        );
    }
}