package com.mz.blog.mapper;

import com.mz.blog.dto.request.CreateAuthorRequest;
import com.mz.blog.dto.request.UpdateAuthorRequest;
import com.mz.blog.dto.response.AuthorResponse;
import com.mz.blog.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author toEntity(CreateAuthorRequest request){
        Author author = new Author();
        author.setName(request.getName());
        author.setEmail(request.getEmail());
        author.setBio(request.getBio());
        return author;
    }

    public void updateEntity(Author author, UpdateAuthorRequest request){
        if(request.getName() != null){
            author.setName(request.getName());
        }
        if(request.getEmail() != null){
            author.setEmail(request.getEmail());
        }
        if(request.getBio() != null){
            author.setBio(request.getBio());
        }
    }

    public AuthorResponse toResponse(Author author){
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .bio(author.getBio())
                .articleCount((long) author.getArticles().size())
                .createdOn(author.getCreatedOn())
                .build();
    }
}
