package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.NoteEditDto;
import com.pago.dotodo.model.entity.NoteEntity;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.error.ObjectNotFoundException;
import com.pago.dotodo.repository.NoteRepository;
import com.pago.dotodo.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DateTimeUtil dateTimeUtil;

    @Mock
    private AuthService authService;

    @InjectMocks
    private NoteService noteService;

    private NoteEntity noteEntity;
    private NoteDto noteDto;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("user@example.com");

        noteEntity = new NoteEntity();
        noteEntity.setId(1L);
        noteEntity.setTitle("Sample Note");
        noteEntity.setContent("Sample Content");
        noteEntity.setOwner(userEntity);
        noteEntity.setDueDate(LocalDateTime.now());
        noteEntity.setDueDateOnly(true);

        noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setTitle("Sample Note");
        noteDto.setTitle("Sample Content");
        noteDto.setDueDate(LocalDateTime.now().toString());
        noteDto.setDueDateOnly(false);
    }

    @Test
    void testGetAllNotes() {
        when(noteRepository.findNotesByOwnerId(anyLong())).thenReturn(List.of(noteEntity));
        when(modelMapper.map(any(NoteEntity.class), eq(NoteDto.class))).thenReturn(noteDto);

        List<NoteDto> result = noteService.getAll(1L);

        assertEquals(1, result.size());
        assertEquals(noteDto.getId(), result.get(0).getId());
    }

    @Test
    void testGetAllNotesReturnsEmptyListWhenNoNotes() {
        when(noteRepository.findNotesByOwnerId(anyLong())).thenReturn(Collections.emptyList());

        List<NoteDto> result = noteService.getAll(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetById_Success() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(noteEntity));
        doNothing().when(authService).validateOwnerAccess(anyLong(), anyLong());
        when(modelMapper.map(any(NoteEntity.class), eq(NoteDto.class))).thenReturn(noteDto);

        NoteDto result = noteService.getById(1L, 1L);

        assertEquals(noteDto.getId(), result.getId());
    }

    @Test
    void testGetById_ThrowsObjectNotFoundException() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> noteService.getById(1L, 1L));
    }

    @Test
    void testGetById_ChecksAccessControl() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(noteEntity));

        noteService.getById(1L, 1L);

        verify(authService, times(1)).validateOwnerAccess(anyLong(), anyLong());
    }

    @Test
    void testGetByUserIdOrderByInsTimeDesc() {
        when(noteRepository.findByOwnerIdOrderByDueDateDesc(anyLong())).thenReturn(List.of(noteEntity));
        when(modelMapper.map(any(NoteEntity.class), eq(NoteDto.class))).thenReturn(noteDto);
        when(dateTimeUtil.addDueDaysHours(anyList())).thenReturn(List.of(noteDto));

        List<NoteDto> result = noteService.getByUserIdOrderByInsTimeDesc(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByUserIdOrderByInsTimeDesc_EmptyList() {
        when(noteRepository.findByOwnerIdOrderByDueDateDesc(anyLong())).thenReturn(Collections.emptyList());

        List<NoteDto> result = noteService.getByUserIdOrderByInsTimeDesc(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteById_Success() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(noteEntity));
        when(modelMapper.map(any(NoteEntity.class), eq(NoteDto.class))).thenReturn(noteDto);
        doNothing().when(authService).validateOwnerAccess(noteEntity.getOwner().getId(), 1L);
        doNothing().when(noteRepository).deleteById(anyLong());

        noteService.deleteById(1L, 1L);

        verify(authService, times(1)).validateOwnerAccess(noteEntity.getOwner().getId(), 1L);
        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ThrowsObjectNotFoundException() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> noteService.deleteById(1L, 1L));
    }

    @Test
    void testEditNote_ThrowsObjectNotFoundException() {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> noteService.editNote(1L, new NoteEditDto(), 1L));
    }

    @Test
    void testGetExpiringNotesEmails_ReturnsEmptyList() {
        when(noteRepository.findAll()).thenReturn(Collections.emptyList());

        List<String> emails = noteService.getExpiringNotesEmails();

        assertTrue(emails.isEmpty());
    }

    @Test
    void testIsExpiring_True() {
        LocalDateTime dueDate = LocalDateTime.now();
        boolean result = noteService.isExpiring(dueDate);

        assertTrue(result);
    }

    @Test
    void testIsExpiring_False() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        boolean result = noteService.isExpiring(dueDate);

        assertFalse(result);
    }

    @Test
    void testIsExpiring_NullDate() {
        boolean result = noteService.isExpiring(null);

        assertFalse(result);
    }

    @Test
    void testGetExpiringNotes_ReturnsNotes() {
        when(noteRepository.findNotesByOwnerIdOrderByDueDate(anyLong(), any(PageRequest.class))).thenReturn(List.of(noteEntity));
        when(modelMapper.map(any(NoteEntity.class), eq(NoteDto.class))).thenReturn(noteDto);

        List<NoteDto> result = noteService.getExpiringNotes(1L, 1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetExpiringNotes_ReturnsEmptyList() {
        when(noteRepository.findNotesByOwnerIdOrderByDueDate(anyLong(), any(PageRequest.class))).thenReturn(Collections.emptyList());

        List<NoteDto> result = noteService.getExpiringNotes(1L, 1);

        assertTrue(result.isEmpty());
    }
}
