package com.pago.dotodo.web.mvc;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.UserProfileView;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.InitService;
import com.pago.dotodo.service.NoteService;
import com.pago.dotodo.service.UserService;
import com.pago.dotodo.util.DateTimeUtil;
import com.pago.dotodo.util.ModelAndViewParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private InitService initService;

    @MockBean
    private NoteService noteService;

    @MockBean
    private DateTimeUtil dateTimeUtil;

    @Mock
    private ModelAndViewParser attributeBuilder;

    private CustomAuthUserDetails userDetails;
    private UserProfileView userProfileView;

    @BeforeEach
    void setUp() {
        userDetails = new CustomAuthUserDetails(1L,
                "John",
                "johnwick",
                "password", Collections.emptyList());

        userProfileView = new UserProfileView();
        userProfileView.setFirstName("John");
        userProfileView.setLastName("Wick");
        userProfileView.setUsername("johnwick");
        userProfileView.setEmail("john.wick@example.com");
        userProfileView.setDob("1990-01-01");
        userProfileView.setPhoneNumber("+3592233445");
        userProfileView.setAddress("123 Main St");
    }

    @Test
    void testGetUserProfile() throws Exception {
        when(userService.getProfileDetails(1L)).thenReturn(userProfileView);

        mockMvc.perform(get("/profile").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileDetails"))
                .andExpect(model().attribute("profileDetails", userProfileView));
    }

    @Test
    void testGetEditPage() throws Exception {
        when(userService.getProfileDetails(1L)).thenReturn(userProfileView);
        when(dateTimeUtil.formatToISODate("1990-01-01", "d MMMM yyyy")).thenReturn("01 January 1990");

        mockMvc.perform(get("/profile/edit").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileDetails"))
                .andExpect(model().attribute("profileDetails", userProfileView));
    }

    @Test
    void testEditProfile_Success() throws Exception {
        when(userService.existsOnOtherAccount("email", userProfileView, 1L)).thenReturn(false);
        when(userService.existsOnOtherAccount("username", userProfileView, 1L)).thenReturn(false);
        when(userService.dateOfBirthMismatch(userProfileView, 1L)).thenReturn(false);

        mockMvc.perform(post("/profile/edit")
                        .with(user(userDetails))
                        .param("firstName", "John")
                        .param("lastName", "Wick")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGetViewNoteDetailPage() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setTitle("Sample Note");
        noteDto.setContent("Sample Content");
        noteDto.setDueDate(LocalDateTime.now().toString());

        when(noteService.getById(anyLong(), anyLong())).thenReturn(noteDto);

        mockMvc.perform(get("/profile/notes/1").with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile?viewNoteId=1"));
    }
}
