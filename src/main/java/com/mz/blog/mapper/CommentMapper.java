package com.mz.blog.mapper;

import com.mz.blog.dto.request.CreateAuthorRequest;
import com.mz.blog.dto.request.CreateCommentRequest;
import com.mz.blog.dto.request.UpdateAuthorRequest;
import com.mz.blog.dto.response.AuthorResponse;
import com.mz.blog.dto.response.CommentResponse;
import com.mz.blog.entity.Article;
import com.mz.blog.entity.Author;
import com.mz.blog.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CreateCommentRequest request, Article article) {
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthorName(request.getAuthorName());
        comment.setAuthorEmail(request.getAuthorEmail());
        comment.setContent(request.getContent());
        comment.setApproved(false);
        return comment;
    }

    public CommentResponse toResponse(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .articleId(comment.getArticle().getId())
                .authorName(comment.getAuthorName())
                .authorEmail(comment.getAuthorEmail())
                .content(comment.getContent())
                .approved(comment.getApproved())
                .createdOn(comment.getCreatedOn())
                .build();
    }
}
