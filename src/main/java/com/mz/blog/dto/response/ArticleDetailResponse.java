package com.mz.blog.dto.response;

import com.mz.blog.entity.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailResponse {

    private Long id;
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private ArticleStatus status;

    private AuthorResponse author;
    private CategoryResponse category;

    private List<CommentResponse> comments;

    private Integer viewCount;
    private LocalDateTime publishedOn;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
