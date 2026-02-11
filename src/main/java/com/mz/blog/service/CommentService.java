package com.mz.blog.service;

import com.mz.blog.dto.request.CreateArticleRequest;
import com.mz.blog.dto.request.CreateCommentRequest;
import com.mz.blog.dto.request.UpdateArticleRequest;
import com.mz.blog.dto.response.ArticleDetailResponse;
import com.mz.blog.dto.response.ArticleResponse;
import com.mz.blog.dto.response.CommentResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.entity.*;
import com.mz.blog.exception.DuplicateResourceException;
import com.mz.blog.exception.ResourceNotFoundException;
import com.mz.blog.mapper.ArticleMapper;
import com.mz.blog.mapper.CommentMapper;
import com.mz.blog.repository.ArticleRepository;
import com.mz.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse createComment(CreateCommentRequest request) {
        log.info("Create Comment for article id: {}", request.getArticleId());

        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(()-> new ResourceNotFoundException("Article", "id", request.getArticleId()));

        Comment comment = commentMapper.toEntity(request, article);
        Comment savedComment = commentRepository.save(comment);
        log.info("Saved Comment with ID: {}", savedComment.getId());
        return commentMapper.toResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long id) {
        log.info("Fetching Comment with ID: {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment", "id", id));
        return commentMapper.toResponse(comment);
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getCommentsByArticle(Long articleId,int page, int size){
        log.info("Fetching Comments by Article: {} - page: {}, size: {}",articleId, page, size);

        if(!articleRepository.existsById(articleId)){
            throw new ResourceNotFoundException("Article", "id", articleId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

        // Fetch from DB
        Page<Comment> commentPage = commentRepository.findByArticleId(articleId,pageable);

        //convert to DTO
        List<CommentResponse> commentResponses = commentPage.getContent()
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());

        return buildPageResponse(commentPage,commentResponses);
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getApprovedCommentsByArticle(Long articleId,int page, int size){
        log.info("Fetching Approved Comments by Article: {} - page: {}, size: {}",articleId, page, size);

        if(!articleRepository.existsById(articleId)){
            throw new ResourceNotFoundException("Article", "id", articleId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

        // Fetch from DB
        Page<Comment> commentPage = commentRepository.findByArticleIdAndApproved(articleId,true,pageable);

        //convert to DTO
        List<CommentResponse> commentResponses = commentPage.getContent()
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());

        return buildPageResponse(commentPage,commentResponses);
    }

    @Transactional
    public CommentResponse approveComment(Long id){
        log.info("Approving Comment with ID: {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment","id",id));

        comment.setApproved(true);
        Comment approvedComment = commentRepository.save(comment);

        log.info("Comment Approved with ID: {}", approvedComment.getId());
        return commentMapper.toResponse(approvedComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        log.info("Deleting Comment with ID: {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment", "id", id));
        commentRepository.delete(comment);
        log.info("Comment Deleted successfully with ID: {}", id);
    }

    private <T> PageResponse<T> buildPageResponse(Page<?> page, List<T> content) {
        return PageResponse.<T>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }
}
