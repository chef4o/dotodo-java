package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.ErrorPageAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.NoteAttribute;
import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.NoteEditDto;
import com.pago.dotodo.model.error.CustomErrorHandler;
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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notes")
public class NoteController extends BaseController {

    private final NoteService noteService;
    private final ModelAndViewParser attributeBuilder;
    private final DateTimeUtil dateTimeUtil;
    private final CustomErrorHandler customErrorHandler;

    public NoteController(NoteService noteService,
                          ModelAndViewParser attributeBuilder,
                          DateTimeUtil dateTimeUtil, CustomErrorHandler customErrorHandler) {
        this.noteService = noteService;
        this.attributeBuilder = attributeBuilder;
        this.dateTimeUtil = dateTimeUtil;
        this.customErrorHandler = customErrorHandler;
    }

    @GetMapping
    public ModelAndView getNotesPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                     @RequestParam(required = false) Long editNoteId,
                                     @RequestParam(required = false) Long viewNoteId) {

        List<NoteDto> byUserIdOrderByInsTimeDesc = noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId());

        return this.view(NoteAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NoteAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                NoteAttribute.EDIT_NOTE_ID, editNoteId,
                NoteAttribute.VIEW_NOTE_ID, viewNoteId,
                NoteAttribute.NOTES, byUserIdOrderByInsTimeDesc)
        );
    }

    @GetMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @ModelAttribute NoteDto noteDto,
                                @RequestParam(required = false) String emptyValueError) {

        return this.view(NoteAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NoteAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                NoteAttribute.EMPTY_ERROR_VALUE, emptyValueError,
                NoteAttribute.NOTE_DATA, noteDto,
                NoteAttribute.CREATE_NEW_NOTE, true,
                NoteAttribute.NOTES, noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
        );
    }

    @PostMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @Valid @ModelAttribute NoteDto noteDto,
                                BindingResult bindingResult) {

        Map<String, String> valueErrors = customErrorHandler
                .loadNoteErrors(bindingResult, noteDto);

        if (!valueErrors.isEmpty()) {
            return this.view(NoteAttribute.GLOBAL_VIEW, attributeBuilder.build(
                    NoteAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                    NoteAttribute.VALUE_ERRORS, valueErrors,
                    NoteAttribute.NOTE_DATA, noteDto,
                    NoteAttribute.CREATE_NEW_NOTE, true,
                    NoteAttribute.NOTES, noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
            );
        }

        noteService.addNote(noteDto, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW);
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @PathVariable Long id) {
        noteService.deleteById(id, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditNotePage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                        @PathVariable Long id,
                                        @ModelAttribute NoteDto noteToEdit) {
        return super.redirect("/" + NoteAttribute.LOCAL_VIEW, attributeBuilder.build(
                NoteAttribute.EDIT_NOTE_ID, id,
                NoteAttribute.NOTE_TO_EDIT, noteToEdit));
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                 @PathVariable Long id,
                                 @Valid @ModelAttribute NoteEditDto noteEditDto,
                                 BindingResult bindingResult) {

        Map<String, String> valueErrors = customErrorHandler
                .loadNoteErrors(bindingResult, noteEditDto);

        if (!valueErrors.isEmpty()) {
            return this.view(NoteAttribute.GLOBAL_VIEW, attributeBuilder.build(
                    NoteAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                    NoteAttribute.VALUE_ERRORS, valueErrors,
                    NoteAttribute.NOTE_TO_EDIT, noteEditDto,
                    NoteAttribute.EDIT_NOTE_ID, id,
                    NoteAttribute.NOTES, noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
            );
        }

        noteService.editNote(id, noteEditDto, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW);
    }


    @GetMapping("/view/{id}")
    public ModelAndView getViewNoteDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable Long id) {

        NoteDto detailedNote = noteService.getById(id, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW, attributeBuilder.build(
                NoteAttribute.VIEW_NOTE_ID, id,
                NoteAttribute.DETAILED_NOTE, detailedNote));
    }

    @ModelAttribute(NoteAttribute.NOTE_DATA)
    public NoteDto noteData() {
        return new NoteDto();
    }

    @ModelAttribute(NoteAttribute.NOTE_TO_EDIT_DTO)
    public NoteEditDto noteEditDto() {
        return new NoteEditDto();
    }

    @ModelAttribute(NoteAttribute.NOTE_TO_EDIT)
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

    @ModelAttribute(NoteAttribute.DETAILED_NOTE)
    public NoteDto detailedNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @RequestParam(required = false) Long viewNoteId) {
        return viewNoteId != null ? noteService.getById(viewNoteId, userDetails.getId()) : null;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException e) {
        return new ModelAndView(NoteAttribute.GLOBAL_VIEW, attributeBuilder.build(
                ErrorPageAttribute.PAGE_NAME, ErrorPageAttribute.LOCAL_VIEW,
                ErrorPageAttribute.ERROR_CODE, ErrorPageAttribute.ERR_404,
                ErrorPageAttribute.SERVER_ERROR, e.getMessage())
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        return new ModelAndView(NoteAttribute.GLOBAL_VIEW, attributeBuilder.build(
                ErrorPageAttribute.PAGE_NAME, ErrorPageAttribute.LOCAL_VIEW,
                ErrorPageAttribute.ERROR_CODE, ErrorPageAttribute.ERR_403,
                ErrorPageAttribute.SERVER_ERROR, e.getMessage())
        );
    }
}