package com.pago.dotodo.web.mvc;

import com.pago.dotodo.service.InitService;
import com.pago.dotodo.util.ModelAndViewParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AboutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InitService initService;

    @SpyBean
    private ModelAndViewParser attributeBuilder;

    @Test
    void testGetAboutUs() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("pageName", "about"));
    }
}
