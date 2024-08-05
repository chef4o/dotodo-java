package com.pago.dotodo.util.interceptor;

import com.pago.dotodo.model.dto.NoteDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
public class TitleInterceptor implements HandlerInterceptor {
    private final int TITLE_VIEW_MAX_LENGTH = 14;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null && modelAndView.getModel().containsKey("notes")) {

            List<?> rawNotes = (List<?>) modelAndView.getModel().get("notes");

            List<NoteDto> modifiedNotes = rawNotes.stream()
                    .filter(NoteDto.class::isInstance)
                    .map(NoteDto.class::cast)
                    .peek(note -> {
                        String title = note.getTitle();
                        if (title.length() > TITLE_VIEW_MAX_LENGTH) {
                            note.setTitle(title.substring(0, TITLE_VIEW_MAX_LENGTH) + "...");
                        }
                    })
                    .toList();

            modelAndView.addObject("notes", modifiedNotes);
        }
    }
}
