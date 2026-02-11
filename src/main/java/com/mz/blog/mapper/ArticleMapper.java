package com.mz.blog.mapper;

import com.mz.blog.dto.request.CreateArticleRequest;
import com.mz.blog.dto.request.CreateCommentRequest;
import com.mz.blog.dto.request.UpdateArticleRequest;
import com.mz.blog.dto.response.ArticleDetailResponse;
import com.mz.blog.dto.response.ArticleResponse;
import com.mz.blog.dto.response.CommentResponse;
import com.mz.blog.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleMapper {

    private final CommentMapper commentMapper;
    private final AuthorMapper authorMapper;
    private final CategoryMapper categoryMapper;


    public Article toEntity(CreateArticleRequest request, Author author, Category category) {
       Article article = new Article();
       article.setTitle(request.getTitle());
       article.setSlug(generateSlug(request.getTitle()));
       article.setContent(request.getContent());
       article.setExcerpt(request.getExcerpt());
       article.setAuthor(author);
       article.setCategory(category);
       article.setStatus(ArticleStatus.DRAFT);
       article.setViewCount(0);
       return article;
    }

    public void updateEntity(Article article, UpdateArticleRequest request, Category category) {

        if(request.getTitle() != null) {
            article.setTitle(request.getTitle());
            article.setSlug(generateSlug(request.getTitle()));
        }
        if (request.getExcerpt() != null) {
            article.setExcerpt(request.getExcerpt());
        }
        if (request.getContent() != null) {
            article.setContent(request.getContent());
        }

        if(category != null) {
            article.setCategory(category);
        }

        if(request.getStatus() != null) {
            article.setStatus(request.getStatus());
            if (request.getStatus() == ArticleStatus.PUBLISHED && article.getPublishedOn() == null) {
                article.publish();
            }
        }
    }

    public ArticleResponse toResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .excerpt(article.getExcerpt())
                .status(article.getStatus())
                .authorId(article.getAuthor().getId())
                .authorName(article.getAuthor().getName())
                .categoryId(article.getCategory().getId())
                .categoryName(article.getCategory().getName())
                .viewCount(article.getViewCount())
                .commentCount(article.getComments().size())
                .publishedOn(article.getPublishedOn())
                .createdOn(article.getCreatedOn())
                .updatedOn(article.getUpdatedOn())
                .build();
    }

    public ArticleDetailResponse toDetailResponse(Article article) {
        List<CommentResponse> comments = article.getComments()
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
        return ArticleDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .content(article.getContent())
                .excerpt(article.getExcerpt())
                .status(article.getStatus())
                .author(authorMapper.toResponse(article.getAuthor()))
                .category(categoryMapper.toResponse(article.getCategory()))
                .comments(comments)
                .viewCount(article.getViewCount())
                .publishedOn(article.getPublishedOn())
                .createdOn(article.getCreatedOn())
                .updatedOn(article.getUpdatedOn())
                .build();
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-zA-Z0-9]", "")
                .replaceAll("\\s+","-")
                .replaceAll("-+", "-")
        .trim();
    }
}
