package com.pago.dotodo.web.mvc;

import com.cloudinary.api.exceptions.NotAllowed;
import com.pago.dotodo.model.dto.ArticleDto;
import com.pago.dotodo.model.error.ObjectNotFoundException;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.NewsService;
import com.pago.dotodo.util.ModelAndViewParser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/news")
public class NewsController extends BaseController {
    private static final String PAGE_NAME = "news";
    private final ModelAndViewParser attributeBuilder;
    private final NewsService newsService;

    public NewsController(ModelAndViewParser attributeBuilder, NewsService newsService) {
        this.attributeBuilder = attributeBuilder;
        this.newsService = newsService;
    }

    @GetMapping
    public ModelAndView getNewsPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                    @RequestParam(required = false) Long viewArticleId) {

        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "viewArticleId", viewArticleId,
                "articles", newsService.getAll())
        );
    }

    @GetMapping("/new")
    public ModelAndView addArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @ModelAttribute ArticleDto articleDto,
                                   @RequestParam(required = false) String emptyValueError) {

        return this.view("index", attributeBuilder.build(
                "pageName", "news/news-new",
                "emptyValueError", emptyValueError,
                "articleDto", articleDto,
                "createNewNote", true,
                "articles", newsService.getAll())
        );
    }

    @PostMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @ModelAttribute ArticleDto articleDto) throws NotAllowed {

        String valueError;

        if (articleDto.getHeader().isBlank() || articleDto.getContent().isBlank()) {
            valueError = "Header and content are both mandatory";
        } else {
            valueError = "";
        }

        if (!valueError.isBlank()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "valueError", valueError,
                    "articleDto", articleDto)
            );
        }

        newsService.addArticle(articleDto, userDetails);

        return super.redirect("/news");
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                      @PathVariable Long id) throws NotAllowed {
        newsService.deleteById(id, userDetails);

        return super.redirect("/notes");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditArticlePage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                           @PathVariable Long id,
                                           @ModelAttribute ArticleDto articleToEdit) {
        return this.view("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "articleToEdit", articleToEdit,
                "editArticleId", id)
        );
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                    @PathVariable Long id,
                                    @Valid @ModelAttribute ArticleDto articleEditDto,
                                    BindingResult bindingResult) throws NotAllowed {

        Map<String, String> valueErrors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                valueErrors.put(error.getField(), error.getField() + " " + error.getDefaultMessage());
            });
        }

        if (!valueErrors.isEmpty()) {
            return this.view("index", attributeBuilder.build(
                    "pageName", PAGE_NAME,
                    "valueErrors", valueErrors,
                    "articleEditDto", articleEditDto,
                    "editArticleId", id)
            );
        }

        newsService.editArticle(id, articleEditDto, userDetails);

        return super.redirect("/news");
    }

    @GetMapping("/view/{id}")
    public ModelAndView getViewArticleDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                                 @PathVariable Long id) {

        ArticleDto detailedArticle = newsService.getById(id);

        return new ModelAndView("index", attributeBuilder.build(
                "pageName", "/news/article-details",
                "detailedArticle", detailedArticle));
    }

    @ModelAttribute("articleDto")
    public ArticleDto articleDto() {
        return new ArticleDto();
    }

    @ModelAttribute("articleToEdit")
    public ArticleDto articleToEdit(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                    @RequestParam(required = false) Long editArticleId) {

        if (editArticleId == null) {
            return null;
        }

        return newsService.getById(editArticleId);
    }

    @ModelAttribute("detailedArticle")
    public ArticleDto detailedArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                      @RequestParam(required = false) Long viewArticleId) {
        return viewArticleId != null ? newsService.getById(viewArticleId) : null;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException e) {
        return new ModelAndView("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "errorCode", "404",
                "serverError", e.getMessage())
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        return new ModelAndView("index", attributeBuilder.build(
                "pageName", PAGE_NAME,
                "errorCode", "403",
                "serverError", e.getMessage())
        );
    }
}
