package com.mz.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthorRequest {

    @NotBlank(message = "Name is required")
    @Size(min =2,message = "Name must be greater than 2 character")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Format")
    private String email;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
}
