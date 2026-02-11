package com.mz.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private Long articleId;
    private String authorName;
    private String authorEmail;
    private String content;
    private Boolean approved;
    private LocalDateTime createdOn;
}
