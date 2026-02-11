package com.mz.blog.service;

import com.mz.blog.dto.request.CreateArticleRequest;
import com.mz.blog.dto.request.CreateCategoryRequest;
import com.mz.blog.dto.request.UpdateArticleRequest;
import com.mz.blog.dto.response.ArticleDetailResponse;
import com.mz.blog.dto.response.ArticleResponse;
import com.mz.blog.dto.response.CategoryResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.entity.Article;
import com.mz.blog.entity.ArticleStatus;
import com.mz.blog.entity.Author;
import com.mz.blog.entity.Category;
import com.mz.blog.exception.DuplicateResourceException;
import com.mz.blog.exception.ResourceNotFoundException;
import com.mz.blog.mapper.ArticleMapper;
import com.mz.blog.mapper.CategoryMapper;
import com.mz.blog.repository.ArticleRepository;
import com.mz.blog.repository.CategoryRepository;
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
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Transactional
    public ArticleResponse createArticle(CreateArticleRequest request) {
        log.info("Create Article with title {}", request.getTitle());

        Author author = authorService.getAuthorEntityById(request.getAuthorId());
        Category category = categoryService.getCategoryEntityById(request.getCategoryId());

        Article article = articleMapper.toEntity(request,author,category);

        if(articleRepository.existsBySlug(article.getSlug())){
            throw new DuplicateResourceException("Article","slug",article.getSlug());
        }

        Article savedArticle = articleRepository.save(article);

        log.info("Saved Article with ID: {}", savedArticle.getId());
        return articleMapper.toResponse(savedArticle);
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse getArticleById(Long id) {
        log.info("Get Article with ID: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article","id",id));

        return articleMapper.toDetailResponse(article);
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse getArticleBySlug(String slug) {
        log.info("Get Article with slug: {}", slug);

        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article","slug",slug));

        return articleMapper.toDetailResponse(article);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        log.info("Incrementing view count for article ID: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article","id",id));

        article.incrementViewCount();
        articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getAllArticles(int page,int size,String sortBy,String sortDir){
        log.info("Fetching all Articles - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch from DB
        Page<Article> articlePage = articleRepository.findAll(pageable);

        //convert to DTO
        List<ArticleResponse> articleResponses = articlePage.getContent()
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());

        return buildPageResponse(articlePage,articleResponses);
    }
    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getArticlesByStatus(ArticleStatus status,int page, int size){
        log.info("Fetching Articles by sttaus: {} - page: {}, size: {}",status, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

        // Fetch from DB
        Page<Article> articlePage = articleRepository.findByStatus(status,pageable);

        //convert to DTO
        List<ArticleResponse> articleResponses = articlePage.getContent()
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());

        return buildPageResponse(articlePage,articleResponses);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getArticlesByAuthor(Long authorId,int page, int size){
        log.info("Fetching Articles by author id: {} - page: {}, size: {}",authorId, page, size);

        authorService.getAuthorEntityById(authorId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

        // Fetch from DB
        Page<Article> articlePage = articleRepository.findByAuthorId(authorId,pageable);

        //convert to DTO
        List<ArticleResponse> articleResponses = articlePage.getContent()
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());

        return buildPageResponse(articlePage,articleResponses);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getArticlesByCategory(Long categoryId,int page, int size){
        log.info("Fetching Articles by category id: {} - page: {}, size: {}",categoryId, page, size);

        categoryService.getCategoryEntityById(categoryId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

        // Fetch from DB
        Page<Article> articlePage = articleRepository.findByCategoryId(categoryId,pageable);

        //convert to DTO
        List<ArticleResponse> articleResponses = articlePage.getContent()
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());

        return buildPageResponse(articlePage,articleResponses);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> searchArticles(String keyword,int page, int size){
        log.info("Searching Articles with keyword: {} - page: {}, size: {}",keyword, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

        // Fetch from DB
        Page<Article> articlePage = articleRepository.searchArticles(keyword,pageable);

        //convert to DTO
        List<ArticleResponse> articleResponses = articlePage.getContent()
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());

        return buildPageResponse(articlePage,articleResponses);
    }

    @Transactional
    public ArticleResponse updateArticle(Long id,UpdateArticleRequest request){
        log.info("Updating Article with ID: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article","id",id));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.getCategoryEntityById(request.getCategoryId());
        }

        articleMapper.updateEntity(article,request,category);

        Article updatedArticle = articleRepository.save(article);
        log.info("Updated Article with ID: {}", updatedArticle.getId());
        return articleMapper.toResponse(updatedArticle);
    }

    public ArticleResponse publishArticle(Long id){
        log.info("Publishing Article with ID: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article","id",id));
        article.publish();
        Article publishedArticle = articleRepository.save(article);
        log.info("Published Article with ID: {}", publishedArticle.getId());
        return articleMapper.toResponse(publishedArticle);
    }

    @Transactional
    public void deleteArticle(Long id) {
        log.info("Deleting Article with ID: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Article", "id", id));
        articleRepository.delete(article);
        log.info("Article Deleted successfully with ID: {}", id);
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
