package com.pago.dotodo.util.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TitleInterceptor implements HandlerInterceptor {
    private static final int TITLE_VIEW_MAX_LENGTH = 13;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            modelAndView.getModel().forEach((key, value) -> {
                if (value instanceof List<?>) {
                    List<?> rawItems = (List<?>) value;
                    List<?> modifiedItems = rawItems.stream()
                            .map(this::modifyTitleIfApplicable)
                            .collect(Collectors.toList());

                    modelAndView.addObject(key, modifiedItems);
                }
            });
        }
    }

    // General method to modify titles if applicable
    private Object modifyTitleIfApplicable(Object item) {
        try {
            // Reflectively check for getTitle and setTitle methods
            Method getTitleMethod = item.getClass().getMethod("getTitle");
            Method setTitleMethod = item.getClass().getMethod("setTitle", String.class);

            // Invoke methods to get and set the title
            String title = (String) getTitleMethod.invoke(item);
            String modifiedTitle = modifyTitle(title);
            setTitleMethod.invoke(item, modifiedTitle);

            return item;
        } catch (NoSuchMethodException e) {
            // Handle case where methods are not present
            // For example, log the issue or handle it according to your requirements
            return item;
        } catch (Exception e) {
            // Handle other reflection or invocation exceptions
            // For example, log the issue or handle it according to your requirements
            return item;
        }
    }

    // Method to modify title length
    private String modifyTitle(String title) {
        if (title != null && title.length() > TITLE_VIEW_MAX_LENGTH) {
            return title.substring(0, TITLE_VIEW_MAX_LENGTH) + "...";
        }
        return title;
    }
}
