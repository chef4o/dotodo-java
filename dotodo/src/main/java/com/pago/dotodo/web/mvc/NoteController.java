package com.pago.dotodo.web.mvc;

import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/notes")
public class NoteController extends BaseController {

    private static final String PAGE_NAME = "notes";
    private final NoteService noteService;
    private final ModelAndViewParser attributeBuilder;

    public NoteController(NoteService noteService, ModelAndViewParser attributeBuilder) {
        this.noteService = noteService;
        this.attributeBuilder = attributeBuilder;
    }

    @GetMapping
    public ModelAndView getNotesPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails) {

        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "notes", noteService.getAll(userDetails.getId()))
        );
    }
}