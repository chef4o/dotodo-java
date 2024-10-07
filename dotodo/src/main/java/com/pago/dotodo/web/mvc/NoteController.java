package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.NoteAttribute;
import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.NoteEditDto;
import com.pago.dotodo.model.error.CustomErrorHandler;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.util.DateTimeUtil;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Controller for handling all note-related operations.
 * This controller manages the creation, editing, deletion, and viewing of notes.
 */
@Controller
@RequestMapping("/notes")
public class NoteController extends BaseController {

    private final NoteService noteService;
    private final ModelAndViewParser attributeBuilder;
    private final DateTimeUtil dateTimeUtil;
    private final CustomErrorHandler customErrorHandler;

    /**
     * Constructs the NoteController with the necessary services and utilities.
     *
     * @param noteService        Service for note-related operations.
     * @param attributeBuilder   Utility for building model and view attributes.
     * @param dateTimeUtil       Utility for date and time operations.
     * @param customErrorHandler Handler for custom validation errors.
     */
    @Autowired
    public NoteController(NoteService noteService,
                          ModelAndViewParser attributeBuilder,
                          DateTimeUtil dateTimeUtil, CustomErrorHandler customErrorHandler) {
        this.noteService = noteService;
        this.attributeBuilder = attributeBuilder;
        this.dateTimeUtil = dateTimeUtil;
        this.customErrorHandler = customErrorHandler;
    }

    /**
     * Displays the notes page, optionally allowing editing or viewing of a specific note.
     *
     * @param userDetails The authenticated user's details.
     * @param editNoteId  Optional ID of the note to be edited.
     * @param viewNoteId  Optional ID of the note to be viewed.
     * @return ModelAndView for the notes page.
     */
    @GetMapping
    public ModelAndView getNotesPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                     @RequestParam(required = false) Long editNoteId,
                                     @RequestParam(required = false) Long viewNoteId) {

        List<NoteDto> byUserIdOrderByInsTimeDesc = noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId());

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                NoteAttribute.EDIT_NOTE_ID, editNoteId,
                NoteAttribute.VIEW_NOTE_ID, viewNoteId,
                NoteAttribute.NOTES, byUserIdOrderByInsTimeDesc)
        );
    }

    /**
     * Displays the page for creating a new note.
     *
     * @param userDetails The authenticated user's details.
     * @param noteDto     The DTO for the note being created.
     * @return ModelAndView for the note creation page.
     */
    @GetMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @ModelAttribute NoteDto noteDto) {

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                NoteAttribute.NOTE_DATA, noteDto,
                NoteAttribute.CREATE_NEW_NOTE, true,
                NoteAttribute.NOTES, noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
        );
    }

    /**
     * Handles the submission of a new note.
     *
     * @param userDetails   The authenticated user's details.
     * @param noteDto       The DTO for the note being created.
     * @param bindingResult BindingResult for validation errors.
     * @return Redirects to the notes page or re-displays the note creation page with errors.
     */
    @PostMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @Valid @ModelAttribute NoteDto noteDto,
                                BindingResult bindingResult) {

        Map<String, String> valueErrors = customErrorHandler
                .loadNoteErrors(bindingResult, noteDto);

        if (!valueErrors.isEmpty()) {
            return this.globalView(attributeBuilder.build(
                    CommonAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                    CommonAttribute.VALUE_ERRORS, valueErrors,
                    NoteAttribute.NOTE_DATA, noteDto,
                    NoteAttribute.CREATE_NEW_NOTE, true,
                    NoteAttribute.NOTES, noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
            );
        }

        noteService.addNote(noteDto, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW);
    }

    /**
     * Handles the deletion of a note by its ID.
     *
     * @param userDetails The authenticated user's details.
     * @param id          The ID of the note to be deleted.
     * @return Redirects to the notes page.
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @PathVariable Long id) {
        noteService.deleteById(id, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW);
    }

    /**
     * Displays the page for editing an existing note.
     *
     * @param id         The ID of the note to be edited.
     * @param noteToEdit The DTO for the note being edited.
     * @return Redirects to the notes page with the note in edit mode.
     */
    @GetMapping("/edit/{id}")
    public ModelAndView getEditNotePage(@PathVariable Long id,
                                        @ModelAttribute NoteDto noteToEdit) {
        return super.redirect("/" + NoteAttribute.LOCAL_VIEW, attributeBuilder.build(
                NoteAttribute.EDIT_NOTE_ID, id,
                NoteAttribute.NOTE_TO_EDIT, noteToEdit));
    }

    /**
     * Handles the submission of an edited note.
     *
     * @param userDetails   The authenticated user's details.
     * @param id            The ID of the note being edited.
     * @param noteEditDto   The DTO for the note being edited.
     * @param bindingResult BindingResult for validation errors.
     * @return Redirects to the notes page or re-displays the edit page with errors.
     */
    @PostMapping("/edit/{id}")
    public ModelAndView editNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                 @PathVariable Long id,
                                 @Valid @ModelAttribute NoteEditDto noteEditDto,
                                 BindingResult bindingResult) {

        Map<String, String> valueErrors = customErrorHandler
                .loadNoteErrors(bindingResult, noteEditDto);

        if (!valueErrors.isEmpty()) {
            return this.globalView(attributeBuilder.build(
                    CommonAttribute.PAGE_NAME, NoteAttribute.LOCAL_VIEW,
                    CommonAttribute.VALUE_ERRORS, valueErrors,
                    NoteAttribute.NOTE_TO_EDIT, noteEditDto,
                    NoteAttribute.EDIT_NOTE_ID, id,
                    NoteAttribute.NOTES, noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId()))
            );
        }

        noteService.editNote(id, noteEditDto, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW);
    }

    /**
     * Displays the details of a specific note.
     *
     * @param userDetails The authenticated user's details.
     * @param id          The ID of the note to be viewed.
     * @return Redirects to the notes page with the note details.
     */
    @GetMapping("/view/{id}")
    public ModelAndView getViewNoteDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable Long id) {

        NoteDto detailedNote = noteService.getById(id, userDetails.getId());

        return super.redirect("/" + NoteAttribute.LOCAL_VIEW, attributeBuilder.build(
                NoteAttribute.VIEW_NOTE_ID, id,
                NoteAttribute.DETAILED_NOTE, detailedNote));
    }

    /**
     * Provides a new instance of NoteDto for the model.
     *
     * @return A new instance of NoteDto.
     */
    @ModelAttribute(NoteAttribute.NOTE_DATA)
    public NoteDto noteData() {
        return new NoteDto();
    }

    /**
     * Provides a new instance of NoteEditDto for the model.
     *
     * @return A new instance of NoteEditDto.
     */
    @ModelAttribute(NoteAttribute.NOTE_TO_EDIT_DTO)
    public NoteEditDto noteEditDto() {
        return new NoteEditDto();
    }

    /**
     * Loads the note to be edited, formatting the due date if necessary.
     *
     * @param userDetails The authenticated user's details.
     * @param editNoteId  The ID of the note to be edited, if available.
     * @return The NoteDto for the note being edited, or null if not applicable.
     */
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

    /**
     * Loads the detailed note for viewing, if applicable.
     *
     * @param userDetails The authenticated user's details.
     * @param viewNoteId  The ID of the note to be viewed, if available.
     * @return The NoteDto for the note being viewed, or null if not applicable.
     */
    @ModelAttribute(NoteAttribute.DETAILED_NOTE)
    public NoteDto detailedNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @RequestParam(required = false) Long viewNoteId) {
        return viewNoteId != null ? noteService.getById(viewNoteId, userDetails.getId()) : null;
    }
}