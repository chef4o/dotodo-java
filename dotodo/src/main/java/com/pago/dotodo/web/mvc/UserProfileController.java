package com.pago.dotodo.web.mvc;

import com.pago.dotodo.configuration.constraint.modelAttribute.CommonAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.NoteAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.UserProfileAttribute;
import com.pago.dotodo.model.dto.EditUserProfile;
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
 * Controller to handle user profile-related operations.
 * This includes viewing and editing profile information, and managing user notes.
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
     * Retrieves the user's profile page.
     *
     * @param userDetails The authenticated user's details (retrieved from security context).
     * @return ModelAndView containing user profile data.
     */
    @GetMapping
    public ModelAndView getUserProfile(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        UserProfileView profileDetails = userService.getProfileDetails(userDetails.getId());

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, UserProfileAttribute.LOCAL_VIEW,
                UserProfileAttribute.PROFILE_USER_ID, userDetails.getId(),
                UserProfileAttribute.PROFILE_DETAILS, profileDetails
        ));
    }

    /**
     * Retrieves the profile edit page.
     *
     * @param userDetails The authenticated user's details.
     * @return ModelAndView with attributes to edit the profile.
     */
    @GetMapping("/edit")
    public ModelAndView getEditProfile(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        EditUserProfile editProfileDetails = userService.getEditProfileDetails(userDetails.getId());

        String dateToEdit = editProfileDetails.getDob() != null
                ? dateTimeUtil.formatToISODate(editProfileDetails.getDob(), "d MMMM yyyy")
                : "";

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, UserProfileAttribute.LOCAL_VIEW,
                UserProfileAttribute.UPDATE_USER, true,
                CommonAttribute.DATE_TO_EDIT, dateToEdit,
                UserProfileAttribute.EDIT_PROFILE_DETAILS, editProfileDetails
        ));
    }

    /**
     * Handles the submission of edited profile details.
     *
     * @param userDetails        The authenticated user's details.
     * @param editProfileDetails The DTO containing edited profile details.
     * @param bindingResult      Contains validation errors (if any) during form submission.
     * @return ModelAndView with either success redirect or error messages displayed on the form.
     */
    @PostMapping("/edit")
    public ModelAndView editProfile(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                    @Valid @ModelAttribute EditUserProfile editProfileDetails,
                                    BindingResult bindingResult) {

        Map<String, String> valueErrors = customErrorHandler
                .loadUserProfileErrors(bindingResult, editProfileDetails, userDetails.getId());

        String dateToEdit = editProfileDetails.getDob() != null
                ? dateTimeUtil.formatToISODate(editProfileDetails.getDob(), "yyyy-MM-dd")
                : "";

        if (!valueErrors.isEmpty()) {
            return this.globalView(attributeBuilder.build(
                    CommonAttribute.PAGE_NAME, UserProfileAttribute.LOCAL_VIEW,
                    UserProfileAttribute.UPDATE_USER, true,
                    CommonAttribute.VALUE_ERRORS, valueErrors,
                    UserProfileAttribute.EDIT_PROFILE_DETAILS, editProfileDetails,
                    CommonAttribute.DATE_TO_EDIT, dateToEdit
            ));
        }

        userService.editUserDetails(editProfileDetails, userDetails);
        return super.redirect("/" + UserProfileAttribute.LOCAL_VIEW);
    }

    /**
     * Displays the detailed view of a specific note.
     *
     * @param userDetails The authenticated user's details.
     * @param id          The ID of the note to view.
     * @return Redirect to the profile page with the note details.
     */
    @GetMapping("/notes/{id}")
    public ModelAndView getViewNoteDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable Long id) {

        NoteDto detailedNote = noteService.getById(id, userDetails.getId());

        return super.redirect("/" + UserProfileAttribute.LOCAL_VIEW, attributeBuilder.build(
                NoteAttribute.VIEW_NOTE_ID, id,
                NoteAttribute.DETAILED_NOTE, detailedNote
        ));
    }

    /**
     * Deletes a specific note by its ID.
     *
     * @param userDetails The authenticated user's details.
     * @param id          The ID of the note to delete.
     * @return Redirects to the profile page after the note is deleted.
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @PathVariable Long id) {
        noteService.deleteById(id, userDetails.getId());

        return super.redirect("/profile");
    }

    /**
     * Provides the user's edit profile details as a model attribute for views.
     *
     * @param userDetails The authenticated user's details.
     * @return The user's profile details for editing.
     */
    @ModelAttribute(UserProfileAttribute.EDIT_PROFILE_DETAILS)
    public EditUserProfile editProfileDetails(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return userService.getEditProfileDetails(userDetails.getId());
    }

    /**
     * Provides a detailed note to the view model if a note is selected for viewing.
     *
     * @param userDetails The authenticated user's details.
     * @param viewNoteId  The ID of the note to view, if any.
     * @return The detailed note DTO or null if no note is selected.
     */
    @ModelAttribute(NoteAttribute.DETAILED_NOTE)
    public NoteDto detailedNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @RequestParam(required = false) Long viewNoteId) {
        return viewNoteId != null ? noteService.getById(viewNoteId, userDetails.getId()) : null;
    }

    /**
     * Provides the user's profile details as a model attribute for views.
     *
     * @param userDetails The authenticated user's details.
     * @return The user's profile details.
     */
    @ModelAttribute(UserProfileAttribute.PROFILE_DETAILS)
    public UserProfileView profileDetails(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return userService.getProfileDetails(userDetails.getId());
    }

    /**
     * Provides a list of the user's expiring notes as a model attribute for views.
     *
     * @param userDetails The authenticated user's details.
     * @return List of notes that are expiring soon.
     */
    @ModelAttribute(NoteAttribute.EXPIRING_NOTES)
    public List<NoteDto> expiringNotes(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return dateTimeUtil.addDueDaysHours(noteService.getExpiringNotes(userDetails.getId(), 5));
    }
}
