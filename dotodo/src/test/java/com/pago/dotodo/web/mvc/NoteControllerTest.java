package com.pago.dotodo.web.mvc;

import com.pago.dotodo.note.model.dto.NoteDto;
import com.pago.dotodo.note.model.dto.NoteEditDto;
import com.pago.dotodo.common.security.CustomAuthUserDetails;
import com.pago.dotodo.main.service.InitService;
import com.pago.dotodo.note.service.NoteService;
import com.pago.dotodo.common.util.DateTimeUtil;
import com.pago.dotodo.common.util.ModelAndViewParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private InitService initService;

    @MockBean
    private DateTimeUtil dateTimeUtil;

    @Mock
    private ModelAndViewParser attributeBuilder;

    private CustomAuthUserDetails userDetails;
    private NoteDto noteDto;

    @BeforeEach
    void setUp() {
        userDetails = new CustomAuthUserDetails(1L, "John", "johnwick", "password", true, true, true, true, Collections.emptyList());

        noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setTitle("Sample Note");
        noteDto.setContent("Sample Content");
    }

    @Test
    void testGetNotesPage() throws Exception {
        when(noteService.getByUserIdOrderByInsTimeDesc(userDetails.getId())).thenReturn(Collections.singletonList(noteDto));

        mockMvc.perform(get("/notes")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notes"))
                .andExpect(model().attribute("notes", Collections.singletonList(noteDto)));
    }

    @Test
    void testAddNotePage() throws Exception {
        mockMvc.perform(get("/notes/new")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("createNewNote"))
                .andExpect(model().attribute("createNewNote", true));
    }

    @Test
    void testAddNote_WithErrors() throws Exception {
        NoteDto invalidNoteDto = new NoteDto();
        invalidNoteDto.setTitle("");  // Invalid, empty title
        invalidNoteDto.setContent("Content");

        mockMvc.perform(post("/notes/new")
                        .with(user(userDetails))
                        .flashAttr("noteDto", invalidNoteDto)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("valueError"))
                .andExpect(model().attribute("valueError", "Title and content are both mandatory"));
    }

    @Test
    void testAddNote_Success() throws Exception {
        when(dateTimeUtil.isInFuture(noteDto.getDueDate(), noteDto.getDueTime())).thenReturn(true);

        mockMvc.perform(post("/notes/new")
                        .with(user(userDetails))
                        .flashAttr("noteDto", noteDto)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testDeleteNote_Success() throws Exception {
        mockMvc.perform(delete("/notes/delete/1")
                        .with(user(userDetails))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    void testEditNotePage() throws Exception {
        when(noteService.getById(1L, userDetails.getId())).thenReturn(noteDto);

        mockMvc.perform(get("/notes/edit/1")
                        .with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes?editNoteId=1"));
    }

    @Test
    void testEditNote_WithErrors() throws Exception {
        NoteEditDto noteEditDto = new NoteEditDto();
        noteEditDto.setTitle("");

        Map<String, String> errors = new HashMap<>();
        errors.put("title", "title must not be blank");
        errors.put("content", "content must not be blank");

        mockMvc.perform(post("/notes/edit/1")
                        .with(user(userDetails))
                        .flashAttr("noteEditDto", noteEditDto)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("valueErrors"))
                .andExpect(model().attribute("valueErrors", errors));
    }

    @Test
    void testEditNote_Success() throws Exception {
        NoteEditDto noteEditDto = new NoteEditDto();
        noteEditDto.setTitle("Updated Title");
        noteEditDto.setContent("Updated Content");

        mockMvc.perform(post("/notes/edit/1")
                        .with(user(userDetails))
                        .flashAttr("noteEditDto", noteEditDto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    void testGetViewNoteDetailPage() throws Exception {
        when(noteService.getById(1L, userDetails.getId())).thenReturn(noteDto);

        mockMvc.perform(get("/notes/view/1")
                        .with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes?viewNoteId=1"));
    }
}
