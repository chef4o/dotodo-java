package com.pago.dotodo.service;

import com.cloudinary.api.exceptions.NotAllowed;
import com.pago.dotodo.model.dto.ArticleDto;
import com.pago.dotodo.model.entity.ArticleEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.model.error.ObjectNotFoundException;
import com.pago.dotodo.repository.ArticleRepository;
import com.pago.dotodo.repository.NoteRepository;
import com.pago.dotodo.security.CustomAuthUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final NoteRepository noteRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;

    @Autowired
    public NewsService(NoteRepository noteRepository,
                       UserService userService,
                       ModelMapper modelMapper,
                       ArticleRepository articleRepository) {
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
    }

    public List<ArticleDto> getAll() {
        return this.articleRepository.findAll()
                .stream()
                .map(artcile -> modelMapper.map(artcile, ArticleDto.class))
                .collect(Collectors.toList());
    }

    public ArticleDto getById(Long articleId) {
        ArticleEntity articleEntity = this.articleRepository.findById(articleId)
                .orElseThrow(() -> new ObjectNotFoundException("article", articleId));

        return modelMapper.map(articleEntity, ArticleDto.class);
    }

    public void deleteById(Long articleId, CustomAuthUserDetails userDetails) throws NotAllowed {

        this.articleRepository.deleteById(articleId);
    }

    public void addArticle(ArticleDto articleDto, CustomAuthUserDetails userDetails) throws NotAllowed {

        ArticleEntity newArticle = new ArticleEntity()
                .setHeader(articleDto.getHeader())
                .setContent(articleDto.getContent())
                .setUploadDate(LocalDateTime.now())
                .setOwner(userService.getUserById(userDetails.getId()));

        articleRepository.saveAndFlush(newArticle);
    }

    public void editArticle(Long articleId, ArticleDto editedArticle, CustomAuthUserDetails user) throws NotAllowed {
        ArticleEntity existingArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new ObjectNotFoundException("article", articleId));
        if (!existingArticle.getHeader().equals(editedArticle.getHeader())) {
            existingArticle.setHeader(editedArticle.getHeader());
        }

        if (!existingArticle.getContent().equals(editedArticle.getContent())) {
            existingArticle.setContent(editedArticle.getContent());
        }

        articleRepository.saveAndFlush(existingArticle);
    }
}
