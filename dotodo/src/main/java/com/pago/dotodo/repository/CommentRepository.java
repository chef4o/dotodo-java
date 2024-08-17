package com.pago.dotodo.repository;

import com.pago.dotodo.model.entity.ArticleEntity;
import com.pago.dotodo.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
