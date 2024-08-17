package com.pago.dotodo.model.entity;

import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.service.InitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserEntityTest {

    private Validator validator;

    @MockBean
    private InitService initService;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserEntityValid() {
        UserEntity user = new UserEntity()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setUsername("johndoe")
                .setPassword("password123")
                .setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setPhoneNumber("123-456-7890")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .setImageUrl("https://example.com/image.jpg");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Expected no validation violations, but found: " + violations);
    }

    @Test
    void testUserEntityInvalidEmail() {
        UserEntity user = new UserEntity()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("invalid-email")
                .setUsername("johndoe")
                .setPassword("password123");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Expected validation violations for invalid email");
    }

    @Test
    void testUserEntityNullEmail() {
        UserEntity user = new UserEntity()
                .setFirstName("John")
                .setLastName("Doe")
                .setUsername("johndoe")
                .setPassword("password123");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Expected validation violations for null email");
    }

    @Test
    void testUserEntityShortUsername() {
        UserEntity user = new UserEntity()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setUsername("john")
                .setPassword("password123");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Expected validation violations for short username");
    }

    @Test
    void testUserEntityInvalidImageUrl() {
        UserEntity user = new UserEntity()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setUsername("johndoe")
                .setPassword("password123")
                .setImageUrl("invalid-url");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Expected validation violations for invalid image URL");
    }

    @Test
    void testUserEntityRelationships() {
        UserEntity user = new UserEntity()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setUsername("johndoe")
                .setPassword("password123");

        RoleEntity role = new RoleEntity();
        role.setRole(RoleEnum.NORMAL);

        AddressEntity address = new AddressEntity();
        address.setStreet("123 Main St");
        address.setTown("Anytown");

        user.addRole(role);
        user.setAddress(address);

        assertEquals(1, user.getRoles().size(), "Expected one role assigned to user");
        assertNotNull(user.getAddress(), "Expected non-null address for user");
    }
}
