package com.mz.blog.controller;

import com.mz.blog.dto.request.CreateArticleRequest;
import com.mz.blog.dto.request.UpdateArticleRequest;
import com.mz.blog.dto.response.ArticleDetailResponse;
import com.mz.blog.dto.response.ArticleResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.entity.ArticleStatus;
import com.mz.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Article Management", description = "APIs for managing articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@Valid @RequestBody CreateArticleRequest request){
        log.info("Creating article: {}", request.getTitle());
        ArticleResponse response = articleService.createArticle(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDetailResponse> getArticleById(@PathVariable Long id){
        log.info("Getting article by id: {}", id);
        ArticleDetailResponse response = articleService.getArticleById(id);
        articleService.incrementViewCount(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ArticleDetailResponse> getArticleBySlug(@PathVariable String slug){
        log.info("Getting article by slug: {}", slug);
        ArticleDetailResponse response = articleService.getArticleBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ArticleResponse>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        log.info("Getting all articles - page: {}, size: {}",page,size);
        PageResponse<ArticleResponse> response = articleService.getAllArticles(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<PageResponse<ArticleResponse>> getArticlesByStatus(
            @PathVariable ArticleStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("Getting all articles by status - page: {}, size: {}",page,size);
        PageResponse<ArticleResponse> response = articleService.getArticlesByStatus(status,page,size);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<PageResponse<ArticleResponse>> getArticlesByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("Getting all articles by author - page: {}, size: {}",page,size);
        PageResponse<ArticleResponse> response = articleService.getArticlesByAuthor(authorId,page,size);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<ArticleResponse>> getArticlesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("Getting all articles by category - page: {}, size: {}",page,size);
        PageResponse<ArticleResponse> response = articleService.getArticlesByCategory(categoryId,page,size);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ArticleResponse>> searchArticles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("Searching articles with keyword: {} - page: {}, size: {}",keyword,page,size);
        PageResponse<ArticleResponse> response = articleService.searchArticles(keyword,page,size);

        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateArticleRequest request
    ){
        log.info("Updating article with id: {}", id);
        ArticleResponse response = articleService.updateArticle(id, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<ArticleResponse> publishArticle(
            @PathVariable Long id
    ){
        log.info("Publishing article with id: {}", id);
        ArticleResponse response = articleService.publishArticle(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable Long id
    ){
        log.info("Deleting article with id: {}", id);
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }



}
