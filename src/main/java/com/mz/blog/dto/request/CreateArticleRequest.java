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
public class CreateArticleRequest {

    @NotBlank(message = "Title is required")
    @Size(min =2,message = "Title must be atleast 2 character")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min =10,message = "Content must be atleast 10 character")
    private String content;

    @Size(max = 500, message = "Excerpt cannot exceed 500 characters")
    private String excerpt;

    @NotNull(message = "Author Id is required")
    private Long authorId;

    @NotNull(message = "Category Id is required")
    private Long categoryId;

}
