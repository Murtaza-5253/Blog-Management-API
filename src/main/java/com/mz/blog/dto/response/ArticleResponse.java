package com.mz.blog.dto.response;

import com.mz.blog.entity.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {

    private Long id;
    private String title;
    private String slug;
    private String excerpt;
    private ArticleStatus status;

    private Long authorId;
    private String authorName;

    private Long categoryId;
    private String categoryName;

    private Integer viewCount;
    private Integer commentCount;
    private LocalDateTime publishedOn;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
