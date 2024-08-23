package com.pago.dotodo.web.mvc;

import com.cloudinary.api.exceptions.NotAllowed;
import com.pago.dotodo.configuration.constraint.modelAttribute.ErrorPageAttribute;
import com.pago.dotodo.configuration.constraint.modelAttribute.NewsAttribute;
import com.pago.dotodo.model.dto.ArticleDto;
import com.pago.dotodo.model.error.CustomErrorHandler;
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

import java.util.Map;

@Controller
@RequestMapping("/news")
public class NewsController extends BaseController {
    private final ModelAndViewParser attributeBuilder;
    private final NewsService newsService;

    public NewsController(ModelAndViewParser attributeBuilder, NewsService newsService) {
        this.attributeBuilder = attributeBuilder;
        this.newsService = newsService;
    }

    @GetMapping
    public ModelAndView getNewsPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                    @RequestParam(required = false) Long viewArticleId) {

        return this.view(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NewsAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                NewsAttribute.VIEW_ARTICLE_ID, viewArticleId,
                NewsAttribute.ARTICLES, newsService.getAll())
        );
    }

    @GetMapping("/new")
    public ModelAndView addArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                   @ModelAttribute ArticleDto articleDto,
                                   @RequestParam(required = false) String emptyValueError) {

        return this.view(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NewsAttribute.PAGE_NAME, NewsAttribute.NEW_ARTICLE_VIEW,
                NewsAttribute.EMPTY_VALUE_ERROR, emptyValueError,
                NewsAttribute.ARTICLES, articleDto,
                newsService.getAll())
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
            return this.view(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                    NewsAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                    NewsAttribute.VALUE_ERROR, valueError,
                    NewsAttribute.ARTICLE_DTO, articleDto)
            );
        }

        newsService.addArticle(articleDto, userDetails);

        return super.redirect("/" + NewsAttribute.LOCAL_VIEW);
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                      @PathVariable Long id) throws NotAllowed {
        newsService.deleteById(id, userDetails);

        return super.redirect("/" + NewsAttribute.LOCAL_VIEW);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditArticlePage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                           @PathVariable Long id,
                                           @ModelAttribute ArticleDto articleToEdit) {
        return this.view(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NewsAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                NewsAttribute.ARTICLE_TO_EDIT, articleToEdit,
                NewsAttribute.EDIT_ARTICLE_ID, id)
        );
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                    @PathVariable Long id,
                                    @Valid @ModelAttribute ArticleDto articleEditDto,
                                    BindingResult bindingResult) throws NotAllowed {

        Map<String, String> valueErrors = CustomErrorHandler.loadBindingErrors(bindingResult);

        if (!valueErrors.isEmpty()) {
            return this.view(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                    NewsAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                    NewsAttribute.VALUE_ERRORS, valueErrors,
                    NewsAttribute.ARTICLE_TO_EDIT, articleEditDto,
                    NewsAttribute.EDIT_ARTICLE_ID, id)
            );
        }

        newsService.editArticle(id, articleEditDto, userDetails);

        return super.redirect("/" + NewsAttribute.LOCAL_VIEW);
    }

    @GetMapping("/view/{id}")
    public ModelAndView getViewArticleDetailPage(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                                 @PathVariable Long id) {

        ArticleDto detailedArticle = newsService.getById(id);

        return new ModelAndView(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NewsAttribute.PAGE_NAME, NewsAttribute.NEW_ARTICLE_VIEW,
                NewsAttribute.DETAILED_ARTICLE, detailedArticle));
    }

    @ModelAttribute(NewsAttribute.ARTICLE_DTO)
    public ArticleDto articleDto() {
        return new ArticleDto();
    }

    @ModelAttribute(NewsAttribute.ARTICLE_TO_EDIT)
    public ArticleDto articleToEdit(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                    @RequestParam(required = false) Long editArticleId) {

        if (editArticleId == null) {
            return null;
        }

        return newsService.getById(editArticleId);
    }

    @ModelAttribute(NewsAttribute.DETAILED_ARTICLE)
    public ArticleDto detailedArticle(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                      @RequestParam(required = false) Long viewArticleId) {
        return viewArticleId != null ? newsService.getById(viewArticleId) : null;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(ObjectNotFoundException e) {
        return new ModelAndView(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NewsAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                ErrorPageAttribute.ERROR_CODE, ErrorPageAttribute.ERR_404,
                ErrorPageAttribute.SERVER_ERROR, e.getMessage())
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        return new ModelAndView(NewsAttribute.GLOBAL_VIEW, attributeBuilder.build(
                NewsAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                ErrorPageAttribute.ERROR_CODE, ErrorPageAttribute.ERR_403,
                ErrorPageAttribute.SERVER_ERROR, e.getMessage())
        );
    }
}
