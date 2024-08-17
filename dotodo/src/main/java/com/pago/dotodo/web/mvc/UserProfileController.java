package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.NoteDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class UserProfileController extends BaseController {
    private static final String PAGE_NAME = "profile";

    private final ModelAndViewParser attributeBuilder;
    private final UserService userService;
    private final NoteService noteService;
    private final DateTimeUtil dateTimeUtil;

    public UserProfileController(ModelAndViewParser attributeBuilder,
                                 UserService userService,
                                 NoteService noteService, DateTimeUtil dateTimeUtil) {
        this.attributeBuilder = attributeBuilder;
        this.userService = userService;
        this.noteService = noteService;
        this.dateTimeUtil = dateTimeUtil;
    }

    @GetMapping
    public ModelAndView getUserProfile(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        UserProfileView profileDetails = userService.getProfileDetails(userDetails.getId());

        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "profileUserId", userDetails.getId(),
                "profileDetails", profileDetails
        ));
    }

    @GetMapping("/edit")
    public ModelAndView getEditPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        UserProfileView profileDetails = userService.getProfileDetails(userDetails.getId());

        String dateToEdit = profileDetails.getDob() != null
                ? dateTimeUtil.formatToISODate(profileDetails.getDob(), "d MMMM yyyy")
                : "";

        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "updateUser", true,
                "dateDoEdit", dateToEdit,
                "profileDetails", profileDetails
        ));
    }

    @PostMapping("/edit")
    public ModelAndView editNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                 @Valid @ModelAttribute UserProfileView profileEditDetails,
                                 BindingResult bindingResult) {

        Map<String, String> valueErrors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                valueErrors.put(error.getField(), error.getField() + " " + error.getDefaultMessage());
            });
        }

        loadCustomErrors(valueErrors, profileEditDetails, userDetails.getId());

        String dateToEdit = profileEditDetails.getDob() != null
                ? dateTimeUtil.formatToISODate(profileEditDetails.getDob(), "d MMMM yyyy")
                : "";

        if (!valueErrors.isEmpty()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "updateUser", true,
                    "valueErrors", valueErrors,
                    "profileDetails", profileEditDetails,
                    "dateDoEdit", dateToEdit,
                    "blockDobEdit", valueErrors.get("dob") != null
            ));
        }

        userService.editUserDetails(profileEditDetails, userDetails);

        return super.redirect("/profile");
    }

    @GetMapping("/notes/{id}")
    public ModelAndView getViewNoteDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                              @PathVariable Long id) {

        NoteDto detailedNote = noteService.getById(id, userDetails.getId());

        return super.redirect("/profile", attributeBuilder.build(
                "viewNoteId", id,
                "detailedNote", detailedNote
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @PathVariable Long id) {
        noteService.deleteById(id, userDetails.getId());

        return super.redirect("/profile");
    }

    @ModelAttribute("detailedNote")
    public NoteDto detailedNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @RequestParam(required = false) Long viewNoteId) {
        return viewNoteId != null ? noteService.getById(viewNoteId, userDetails.getId()) : null;
    }

    @ModelAttribute("profileDetails")
    public UserProfileView profileDetails(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return userService.getProfileDetails(userDetails.getId());
    }

    /**
     * Fetches expiring notes and makes them available as a model attribute.
     */
    @ModelAttribute("expiringNotes")
    public List<NoteDto> expiringNotes(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {
        return dateTimeUtil.addDueDaysHours(noteService.getExpiringNotes(userDetails.getId(), 5));
    }

    private void loadCustomErrors(Map<String, String> valueErrors, UserProfileView profileEditDetails, Long userId) {
        if (userService.existsOnOtherAccount("email", profileEditDetails, userId)) {
            valueErrors.put("email", "Email exists on other account");
        }

        if (userService.existsOnOtherAccount("username", profileEditDetails, userId)) {
            valueErrors.put("username", "Username exists on other account");
        }

        if (userService.dateOfBirthMismatch(profileEditDetails, userId)) {
            valueErrors.put("dob", "You need to contact support to change the date of birth");
        }
    }
}
