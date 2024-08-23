package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.UserProfileAttribute;
import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.error.CustomErrorHandler;
import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.service.UserService;
import com.pago.dotodo.util.DateTimeUtil;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Controller to handle user profile related operations.
 */
@Controller
@RequestMapping("/profile")
public class UserProfileController extends BaseController {

    private final ModelAndViewParser attributeBuilder;
    private final UserService userService;
    private final NoteService noteService;
    private final DateTimeUtil dateTimeUtil;
    private final CustomErrorHandler customErrorHandler;

    /**
     * Constructs the UserProfileController.
     *
     * @param attributeBuilder   Utility for building model and view attributes.
     * @param userService        Service for user-related operations.
     * @param noteService        Service for note-related operations.
     * @param dateTimeUtil       Utility for date and time operations.
     * @param customErrorHandler Handler for custom validation errors.
     */
    public UserProfileController(ModelAndViewParser attributeBuilder,
                                 UserService userService,
                                 NoteService noteService,
                                 DateTimeUtil dateTimeUtil,
                                 CustomErrorHandler customErrorHandler) {
        this.attributeBuilder = attributeBuilder;
        this.userService = userService;
        this.noteService = noteService;
        this.dateTimeUtil = dateTimeUtil;
        this.customErrorHandler = customErrorHandler;
    }

    /**
     * Retrieves the user profile page.
     *
     * @param userDetails The authenticated user's details.
     * @return ModelAndView with user profile data.
     */
    @GetMapping
    public ModelAndView getUserProfile(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        UserProfileView profileDetails = userService.getProfileDetails(userDetails.getId());

        return this.view(UserProfileAttribute.GLOBAL_VIEW, attributeBuilder.build(
                UserProfileAttribute.PAGE_NAME, UserProfileAttribute.LOCAL_VIEW,
                UserProfileAttribute.PROFILE_USER_ID, userDetails.getId(),
                UserProfileAttribute.PROFILE_DETAILS, profileDetails
        ));
    }

    /**
     * Retrieves the edit profile page.
     *
     * @param userDetails The authenticated user's details.
     * @return ModelAndView with attributes for the edit page.
     */
    @GetMapping("/edit")
    public ModelAndView getEditPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        UserProfileView profileDetails = userService.getProfileDetails(userDetails.getId());

        String dateToEdit = profileDetails.getDob() != null
                ? dateTimeUtil.formatToISODate(profileDetails.getDob(), "d MMMM yyyy")
                : "";

        return this.view(UserProfileAttribute.GLOBAL_VIEW, attributeBuilder.build(
                UserProfileAttribute.PAGE_NAME, UserProfileAttribute.LOCAL_VIEW,
                UserProfileAttribute.UPDATE_USER, true,
                UserProfileAttribute.DATE_TO_EDIT, dateToEdit,
                UserProfileAttribute.PROFILE_DETAILS, profileDetails
        ));
    }

    /**
     * Handles the submission of edited profile details.
     *
     * @param userDetails        The authenticated user's details.
     * @param profileEditDetails The details submitted for editing.
     * @param bindingResult      BindingResult for validation errors.
     * @return ModelAndView with either success redirect or error messages.
     */
    @PostMapping("/edit")
    public ModelAndView editNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                 @Valid @ModelAttribute UserProfileView profileEditDetails,
                                 BindingResult bindingResult) {

        Map<String, String> valueErrors = customErrorHandler
                .loadUserProfileErrors(bindingResult, profileEditDetails, userDetails.getId());

        String dateToEdit = profileEditDetails.getDob() != null
                ? dateTimeUtil.formatToISODate(profileEditDetails.getDob(), "yyyy-MM-dd")
                : "";

        if (!valueErrors.isEmpty()) {
            return this.view("index", attributeBuilder.build(
                    UserProfileAttribute.PAGE_NAME, UserProfileAttribute.LOCAL_VIEW,
                    UserProfileAttribute.UPDATE_USER, true,
                    UserProfileAttribute.VALUE_ERROR, valueErrors,
                    UserProfileAttribute.PROFILE_DETAILS, profileEditDetails,
                    UserProfileAttribute.DATE_TO_EDIT, dateToEdit,
                    UserProfileAttribute.BLOCK_DOB_EDIT, valueErrors.get("dob") != null
            ));
        }

        userService.editUserDetails(profileEditDetails, userDetails);
        return super.redirect("/" + UserProfileAttribute.PAGE_NAME);
    }

    /**
     * Retrieves the detailed view of a note.
     *
     * @param userDetails The authenticated user's details.
     * @param id          The ID of the note to view.
     * @return ModelAndView with detailed note information.
     */
    @GetMapping("/notes/{id}")
    public ModelAndView getViewNoteDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable Long id) {

        NoteDto detailedNote = noteService.getById(id, userDetails.getId());

        return super.redirect("/" + UserProfileAttribute.LOCAL_VIEW, attributeBuilder.build(
                UserProfileAttribute.VIEW_NOTE_ID, id,
                UserProfileAttribute.DETAILED_NOTE, detailedNote
        ));
    }

    /**
     * Deletes a note by its ID.
     *
     * @param userDetails The authenticated user's details.
     * @param id          The ID of the note to delete.
     * @return ModelAndView redirecting to the profile page.
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @PathVariable Long id) {
        noteService.deleteById(id, userDetails.getId());

        return super.redirect("/profile");
    }

    /**
     * Model attribute method for detailed note.
     *
     * @param userDetails The authenticated user's details.
     * @param viewNoteId  The ID of the note to view, if any.
     * @return The detailed note or null if not available.
     */
    @ModelAttribute(UserProfileAttribute.DETAILED_NOTE)
    public NoteDto detailedNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @RequestParam(required = false) Long viewNoteId) {
        return viewNoteId != null ? noteService.getById(viewNoteId, userDetails.getId()) : null;
    }

    /**
     * Model attribute method for profile details.
     *
     * @param userDetails The authenticated user's details.
     * @return The user's profile details.
     */
    @ModelAttribute(UserProfileAttribute.PROFILE_DETAILS)
    public UserProfileView profileDetails(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return userService.getProfileDetails(userDetails.getId());
    }

    /**
     * Model attribute method for expiring notes.
     *
     * @param userDetails The authenticated user's details.
     * @return List of notes that are expiring soon.
     */
    @ModelAttribute(UserProfileAttribute.EXPIRING_NOTES)
    public List<NoteDto> expiringNotes(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return dateTimeUtil.addDueDaysHours(noteService.getExpiringNotes(userDetails.getId(), 5));
    }
}