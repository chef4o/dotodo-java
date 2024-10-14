package com.pago.dotodo.news.web.mvc;

import com.cloudinary.api.exceptions.NotAllowed;
import com.pago.dotodo.common.web.constraint.CommonAttribute;
import com.pago.dotodo.news.web.mvc.constraint.NewsAttribute;
import com.pago.dotodo.common.web.BaseController;
import com.pago.dotodo.news.model.dto.ArticleDto;
import com.pago.dotodo.common.error.CustomErrorHandler;
import com.pago.dotodo.common.security.CustomAuthUserDetails;
import com.pago.dotodo.auth.service.AuthService;
import com.pago.dotodo.news.service.NewsService;
import com.pago.dotodo.common.util.ModelAndViewParser;
import jakarta.validation.Valid;
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
    private final CustomErrorHandler customErrorHandler;
    private final AuthService authService;

    public NewsController(ModelAndViewParser attributeBuilder,
                          NewsService newsService,
                          CustomErrorHandler customErrorHandler,
                          AuthService authService) {
        this.attributeBuilder = attributeBuilder;
        this.newsService = newsService;
        this.customErrorHandler = customErrorHandler;
        this.authService = authService;
    }

    @GetMapping
    public ModelAndView getNewsPage(@RequestParam(required = false) Long viewArticleId) {

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                NewsAttribute.VIEW_ARTICLE_ID, viewArticleId,
                NewsAttribute.ARTICLES, newsService.getAll())
        );
    }

    @GetMapping("/new")
    public ModelAndView addArticle(@ModelAttribute ArticleDto articleDto) {

//        authService.validateAdminAccess();

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, NewsAttribute.NEW_ARTICLE_VIEW,
                NewsAttribute.ARTICLE_DTO, articleDto,
                NewsAttribute.ARTICLES, newsService.getAll())
        );
    }

    @PostMapping("/new")
    public ModelAndView addNote(@AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                @Valid @ModelAttribute ArticleDto articleDto,
                                BindingResult bindingResult) throws NotAllowed {

//        authService.validateAdminAccess();

        Map<String, String> valueErrors = customErrorHandler.loadNewsErrors(bindingResult, articleDto);

        if (!valueErrors.isEmpty()) {
            return this.globalView(attributeBuilder.build(
                    CommonAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                    CommonAttribute.VALUE_ERRORS, valueErrors,
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
    public ModelAndView getEditArticlePage(@PathVariable Long id,
                                           @ModelAttribute ArticleDto articleEditDto) {
        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                NewsAttribute.ARTICLE_TO_EDIT, articleEditDto,
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
            return this.globalView(attributeBuilder.build(
                    CommonAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                    CommonAttribute.VALUE_ERRORS, valueErrors,
                    NewsAttribute.ARTICLE_TO_EDIT, articleEditDto,
                    NewsAttribute.EDIT_ARTICLE_ID, id)
            );
        }

        newsService.editArticle(id, articleEditDto, userDetails);

        return super.redirect("/" + NewsAttribute.LOCAL_VIEW);
    }

    @GetMapping("/view/{id}")
    public ModelAndView getViewArticleDetailPage(@PathVariable Long id) {

        ArticleDto detailedArticle = newsService.getById(id);

        return this.globalView(attributeBuilder.build(
                CommonAttribute.PAGE_NAME, NewsAttribute.LOCAL_VIEW,
                NewsAttribute.DETAILED_ARTICLE, detailedArticle));
    }

    @ModelAttribute(NewsAttribute.ARTICLE_DTO)
    public ArticleDto articleDto() {
        return new ArticleDto();
    }

    @ModelAttribute(NewsAttribute.ARTICLE_TO_EDIT)
    public ArticleDto articleToEdit(@RequestParam(required = false) Long editArticleId) {
        return editArticleId != null ? newsService.getById(editArticleId) : null;
    }

    @ModelAttribute(NewsAttribute.DETAILED_ARTICLE)
    public ArticleDto detailedArticle(@RequestParam(required = false) Long viewArticleId) {
        return viewArticleId != null ? newsService.getById(viewArticleId) : null;
    }
}