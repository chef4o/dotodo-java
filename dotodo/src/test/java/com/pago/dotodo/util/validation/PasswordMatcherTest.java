package com.pago.dotodo.util.validation;

import com.pago.dotodo.service.InitService;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
class PasswordMatcherTest {

    private PasswordMatcher passwordMatcher;

    @MockBean
    private InitService initService;

    @BeforeEach
    void setUp() {
        passwordMatcher = new PasswordMatcher();

        PasswordMatch annotation = mock(PasswordMatch.class);
        Mockito.when(annotation.rawPassword()).thenReturn("password");
        Mockito.when(annotation.rePassword()).thenReturn("confirmPassword");
        Mockito.when(annotation.message()).thenReturn("The repeated password should match");

        passwordMatcher.initialize(annotation);
    }

    @Test
    void testPasswordsMatch() {
        TestObject testObject = new TestObject("password123", "password123");
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        boolean result = passwordMatcher.isValid(testObject, context);

        assertTrue(result, "Passwords should match");
    }

    static class TestObject {
        private final String password;
        private final String confirmPassword;

        public TestObject(String password, String confirmPassword) {
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        public String getPassword() {
            return password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }
    }
}
