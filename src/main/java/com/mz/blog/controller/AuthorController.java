package com.mz.blog.controller;

import com.mz.blog.dto.request.CreateAuthorRequest;
import com.mz.blog.dto.request.UpdateAuthorRequest;
import com.mz.blog.dto.response.AuthorResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Author Management", description = "APIs for managing blog authors")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @Operation(summary = "Create a new author",description = "Creates a new author with provider details")
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody CreateAuthorRequest request){
        log.info("Create Author request: {}", request.getEmail());
        AuthorResponse authorResponse = authorService.createAuthor(request);
        return new ResponseEntity<>(authorResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID",description = "Retrieves an author by their ID")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id){
        log.info("Get Author by id: {}", id);
        AuthorResponse authorResponse = authorService.getAuthorById(id);
        return new ResponseEntity<>(authorResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all authors",description = "Retrieves all authors")
    public ResponseEntity<PageResponse<AuthorResponse>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        log.info("Get All Authors - page: {}, size: {}", page, size);
        PageResponse<AuthorResponse> response = authorService.getAllAuthors(page,size,sortBy,sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "updated author by ID",description = "updates an author by their ID")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAuthorRequest request){
        log.info("Update Author request with id: {} and mail: {}",id, request.getEmail());
        AuthorResponse authorResponse = authorService.updateAuthor(id, request);
        return new ResponseEntity<>(authorResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete author by ID",description = "Deletes an author by their ID and all their articles")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        log.info("Delete Author request with id: {}",id);
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
