package com.mz.blog.repository;

import com.mz.blog.entity.Article;
import com.mz.blog.entity.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySlug(String slug);
    boolean existsBySlug(String slug);

    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    Page<Article> findByAuthorId(Long authorId, Pageable pageable);

    Page<Article> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("""
    select a from Article a where 
        lower(a.title) like lower(concat('%',:keyword,'%'))
        or lower(a.content) like lower(concat('%',:keyword,'%'))
    """)
    Page<Article> searchArticles(@Param("keyword") String keyword, Pageable pageable);
}
