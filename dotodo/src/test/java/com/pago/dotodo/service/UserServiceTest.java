package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.entity.AddressEntity;
import com.pago.dotodo.model.entity.RoleEntity;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private UserEntity mockUser;
    private UserProfileView mockProfileView;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(2L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Wick");
        mockUser.setUsername("johnwick");
        mockUser.setEmail("john.wick@example.com");
        mockUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        mockUser.setPhoneNumber("+3592233445");
        AddressEntity mockAddress = new AddressEntity();
        mockAddress.setStreet("123 Main St");
        mockUser.setAddress(mockAddress);
        RoleEntity mockRole = new RoleEntity();
        mockRole.setRole(RoleEnum.NORMAL);
        mockUser.setRoles(List.of(mockRole));

        mockProfileView = new UserProfileView();
        mockProfileView.setFirstName("John");
        mockProfileView.setLastName("Wick");
        mockProfileView.setUsername("johnwick");
        mockProfileView.setEmail("john.wick@example.com");
        mockProfileView.setDob("1990-01-01");
        mockUser.setPhoneNumber("+3592233445");
        mockProfileView.setAddress("123 Main St");
    }

    @Test
    void testGetAll_ReturnsListOfUsers() {
        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(3L);
        anotherUser.setFirstName("Jane");
        anotherUser.setLastName("Smith");
        anotherUser.setEmail("jane@example.com");

        when(userRepository.findAll()).thenReturn(List.of(mockUser, anotherUser));
        when(modelMapper.map(any(UserEntity.class), eq(UserDto.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            return dto;
        });

        List<UserDto> result = userService.getAll();

        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals(2, result.size(), "getAll returns proper list of users");
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        UserEntity result = userService.getUserById(2L);

        assertNotNull(result);
        assertEquals("johnwick", result.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(20L);
        });

        assertEquals("User not found with id: 20", exception.getMessage());
    }

    @Test
    void testDateOfBirthMismatch_DifferentDate() {
        mockProfileView.setDob("2000-01-01");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        boolean mismatch = userService.dateOfBirthMismatch(mockProfileView, 2L);

        assertTrue(mismatch);
    }

    @Test
    void testGetProfileDetails() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockUser, UserProfileView.class)).thenReturn(mockProfileView);

        UserProfileView result = userService.getProfileDetails(userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(modelMapper, times(1)).map(mockUser, UserProfileView.class);
    }

    @Test
    void testGetProfileDetails_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.getProfileDetails(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));

        Optional<UserEntity> result = userService.getUserByEmail(mockUser.getEmail());

        assertTrue(result.isPresent());
        assertEquals(mockUser.getEmail(), result.get().getEmail());
        verify(userRepository, times(1)).findByEmail(mockUser.getEmail());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String email = "unknown@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.getUserByEmail(email);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));

        Optional<UserEntity> result = userService.getUserByUsername(mockUser.getUsername());

        assertTrue(result.isPresent());
        assertEquals(mockUser.getUsername(), result.get().getUsername());
        verify(userRepository, times(1)).findByUsername(mockUser.getUsername());
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        String username = "unknownuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.getUserByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }


    @Test
    void testGetUsersWithBirthday() {
        LocalDate birthday = LocalDate.now();
        mockUser.setDateOfBirth(birthday);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(mockUser));

        List<String> result = userService.getUsersWithBirthday();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockUser.getEmail(), result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUsersWithBirthday_NoUsersFound() {
        LocalDate today = LocalDate.now();
        if (mockUser.getDateOfBirth().equals(today)) {
            mockUser.setDateOfBirth(today.minusDays(1));
        }
        when(userRepository.findAll()).thenReturn(Collections.singletonList(mockUser));

        List<String> result = userService.getUsersWithBirthday();

        assertEquals(0, result.size(), "No users with birthday on mismatching date found");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testExistsOnOtherAccount_InvalidField() {
        // Arrange
        UserDto loggedUserDto = new UserDto();

        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockUser, UserDto.class)).thenReturn(loggedUserDto);
        when(modelMapper.map(mockProfileView, UserDto.class)).thenReturn(loggedUserDto);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.existsOnOtherAccount("invalidField", mockProfileView, 2L);
        });

        assertEquals("Cannot decide on method return result", exception.getMessage());
    }
}
