package com.pago.dotodo.util;

import com.pago.dotodo.service.InitService;
import com.pago.dotodo.service.LayoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ModelAndViewParserTest {

    @Mock
    private LayoutService layoutService;

    @InjectMocks
    private ModelAndViewParser modelAndViewParser;

    @MockBean
    private InitService initService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuildWithInvalidAttributes() {
        try {
            modelAndViewParser.build("title", "Test Title", "description");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid number of arguments. Key-value pairs are required.", e.getMessage());
        }
    }

}
