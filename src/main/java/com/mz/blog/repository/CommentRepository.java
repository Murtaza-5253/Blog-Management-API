package com.mz.blog.repository;

import com.mz.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByArticleId(Long articleId, Pageable pageable);
    Page<Comment> findByArticleIdAndApproved(Long articleId, Boolean approved, Pageable pageable);

    long countByArticleIdAndApproved(Long articleId, Boolean approved);
}
