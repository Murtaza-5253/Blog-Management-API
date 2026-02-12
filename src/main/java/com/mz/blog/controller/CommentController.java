package com.mz.blog.controller;

import com.mz.blog.dto.request.CreateCommentRequest;
import com.mz.blog.dto.response.CommentResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comment Management",description = "APIs for managing article comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CreateCommentRequest request){
        log.info("Creating comment for article {}", request.getArticleId());
        CommentResponse commentResponse = commentService.createComment(request);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id){
        log.info("Getting comment for article {}", id);
        CommentResponse commentResponse = commentService.getCommentById(id);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<PageResponse<CommentResponse>> getCommentsByArticle(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("Getting comment for article {}", articleId);
        PageResponse<CommentResponse> response = commentService.getCommentsByArticle(articleId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}/approved")
    public ResponseEntity<PageResponse<CommentResponse>> getApprovedCommentsByArticle(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("Getting approved comment for article {}", articleId);
        PageResponse<CommentResponse> response = commentService.getApprovedCommentsByArticle(articleId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<CommentResponse> approveComment(@PathVariable Long id)
    {
        log.info("approve comment with id: {}",id);
        CommentResponse response = commentService.approveComment(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long id)
    {
        log.info("Deleting comment with id: {}",id);
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
