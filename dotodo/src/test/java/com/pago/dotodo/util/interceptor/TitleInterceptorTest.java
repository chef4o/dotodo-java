package com.pago.dotodo.util.interceptor;

import com.pago.dotodo.main.service.InitService;
import com.pago.dotodo.common.util.interceptor.TitleInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TitleInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @MockBean
    private InitService initService;

    @InjectMocks
    private TitleInterceptor titleInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostHandle_ModifyTitles() throws Exception {
        // Arrange
        List<MockItem> items = new ArrayList<>();
        items.add(new MockItem("A very long title that exceeds the limit"));
        items.add(new MockItem("Short title"));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("items", items);

        // Act
        titleInterceptor.postHandle(request, response, null, modelAndView);

        // Assert
        List<MockItem> modifiedItems = (List<MockItem>) modelAndView.getModel().get("items");
        assertEquals("A very long t...", modifiedItems.get(0).getTitle());
        assertEquals("Short title", modifiedItems.get(1).getTitle());
    }

    @Test
    void testPostHandle_NoTitleModification() throws Exception {
        // Arrange
        List<MockItem> items = new ArrayList<>();
        items.add(new MockItem("Short title"));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("items", items);

        // Act
        titleInterceptor.postHandle(request, response, null, modelAndView);

        // Assert
        List<MockItem> modifiedItems = (List<MockItem>) modelAndView.getModel().get("items");
        assertEquals("Short title", modifiedItems.get(0).getTitle());
    }

    static class MockItem {
        private String title;

        public MockItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
