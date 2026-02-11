package com.mz.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

    @NotNull(message = "Article Id is required")
    private Long articleId;

    @NotBlank(message = "Author name is required")
    @Size(min = 2,message = "name should be atleast 2 character")
    private String authorName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String authorEmail;

    @NotBlank(message = "Comment content is required")
    @Size(min=5, message = "Content should be atleast 5 characters")
    private String content;

}
