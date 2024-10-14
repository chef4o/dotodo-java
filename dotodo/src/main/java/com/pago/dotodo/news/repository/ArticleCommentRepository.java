package com.pago.dotodo.news.repository;

import com.pago.dotodo.news.model.entity.ArticleCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleCommentEntity, Long> {
}
