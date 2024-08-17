package com.pago.dotodo.model.entity;

import com.pago.dotodo.service.InitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoteEntityTest {

    private Validator validator;
    private NoteEntity note;

    @MockBean
    private InitService initService;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        note = new NoteEntity();
    }

    @Test
    void testValidNoteEntity() {
        note.setTitle("Test Title")
                .setContent("Test Content")
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setDueDate(LocalDateTime.now().plusDays(1))
                .setTrackProgress("In Progress")
                .setOwner(new UserEntity().setUsername("owner").setEmail("owner@example.com"));

        Set<ConstraintViolation<NoteEntity>> violations = validator.validate(note);
        assertTrue(violations.isEmpty(), "There should be no constraint violations");
    }

    @Test
    void testTitleCannotBeBlank() {
        note.setTitle("")
                .setContent("Test Content")
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setTrackProgress("In Progress");

        Set<ConstraintViolation<NoteEntity>> violations = validator.validate(note);
        assertFalse(violations.isEmpty(), "There should be a constraint violation for blank title");
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testContentCannotBeBlank() {
        note.setTitle("Test Title")
                .setContent("")
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setTrackProgress("In Progress");

        Set<ConstraintViolation<NoteEntity>> violations = validator.validate(note);
        assertFalse(violations.isEmpty(), "There should be a constraint violation for blank content");
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testStartDateCannotBeNull() {
        note.setTitle("Test Title")
                .setContent("Test Content")
                .setArchived(false)
                .setStartDate(null)
                .setTrackProgress("In Progress");

        Set<ConstraintViolation<NoteEntity>> violations = validator.validate(note);
        assertFalse(violations.isEmpty(), "There should be a constraint violation for null startDate");
        assertEquals(1, violations.size());
        assertEquals("must not be null", violations.iterator().next().getMessage());
    }

    @Test
    void testTrackProgressCannotBeNull() {
        note.setTitle("Test Title")
                .setContent("Test Content")
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setTrackProgress(null);

        Set<ConstraintViolation<NoteEntity>> violations = validator.validate(note);
        assertFalse(violations.isEmpty(), "There should be a constraint violation for null trackProgress");
        assertEquals(1, violations.size());
        assertEquals("must not be null", violations.iterator().next().getMessage());
    }

    @Test
    void testRelationshipWithOwner() {
        UserEntity owner = new UserEntity().setUsername("owner").setEmail("owner@example.com");
        note.setOwner(owner);

        assertEquals(owner, note.getOwner());
    }

    @Test
    void testRelationshipWithPeers() {
        UserEntity peer = new UserEntity().setUsername("peer").setEmail("peer@example.com");
        note.setPeers(Collections.singleton(peer));

        assertTrue(note.getPeers().contains(peer));
    }
}
