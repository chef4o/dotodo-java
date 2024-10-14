package com.pago.dotodo.repository;

import com.pago.dotodo.note.model.entity.NoteEntity;
import com.pago.dotodo.user.model.entity.UserEntity;
import com.pago.dotodo.note.repository.NoteRepository;
import com.pago.dotodo.user.repository.UserRepository;
import com.pago.dotodo.main.service.InitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @MockBean
    private InitService initService;

    @Autowired
    private UserRepository userRepository;  // Assuming you have a UserRepository

    private UserEntity testUser;
    private UserEntity otherUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        testUser = new UserEntity();
        testUser.setUsername("testUser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        otherUser = new UserEntity();
        otherUser.setUsername("otherUser");
        otherUser.setEmail("otheruser@example.com");
        otherUser.setPassword("password123");
        otherUser = userRepository.save(otherUser);

        NoteEntity note1 = new NoteEntity();
        note1.setOwner(testUser);
        note1.setDueDate(LocalDateTime.of(2024, 8, 20, 12, 0));
        note1.setTitle("Note 1");
        note1.setContent("Content of Note 1");
        note1.setArchived(false);
        note1.setTrackProgress("In Progress");
        note1.setStartDate(LocalDateTime.now());
        noteRepository.save(note1);

        NoteEntity note2 = new NoteEntity();
        note2.setOwner(testUser);
        note2.setDueDate(LocalDateTime.of(2024, 8, 22, 12, 0));
        note2.setTitle("Note 2");
        note2.setContent("Content of Note 2");
        note2.setArchived(false);
        note2.setTrackProgress("In Progress");
        note2.setStartDate(LocalDateTime.now());
        noteRepository.save(note2);

        NoteEntity note3 = new NoteEntity();
        note3.setOwner(testUser);
        note3.setDueDate(LocalDateTime.of(2024, 8, 21, 12, 0));
        note3.setTitle("Note 3");
        note3.setContent("Content of Note 3");
        note3.setArchived(false);
        note3.setTrackProgress("In Progress");
        note3.setStartDate(LocalDateTime.now());
        noteRepository.save(note3);

        NoteEntity note4 = new NoteEntity();
        note4.setOwner(otherUser);
        note4.setDueDate(LocalDateTime.of(2024, 8, 23, 12, 0));
        note4.setTitle("Note 4");
        note4.setContent("Content of Note 4");
        note4.setArchived(false);
        note4.setTrackProgress("In Progress");
        note4.setStartDate(LocalDateTime.now());
        noteRepository.save(note4);
    }

    @Test
    void testFindNotesByOwnerIdOrderByDueDate() {
        Pageable pageable = PageRequest.of(0, 10);

        List<NoteEntity> notes = noteRepository.findNotesByOwnerIdOrderByDueDate(testUser.getId(), pageable);

        assertNotNull(notes);
        assertEquals(3, notes.size());
        assertEquals(LocalDateTime.of(2024, 8, 20, 12, 0), notes.get(0).getDueDate());
        assertEquals(LocalDateTime.of(2024, 8, 21, 12, 0), notes.get(1).getDueDate());
        assertEquals(LocalDateTime.of(2024, 8, 22, 12, 0), notes.get(2).getDueDate());
    }
}
