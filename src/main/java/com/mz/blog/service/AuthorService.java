package com.mz.blog.service;

import com.mz.blog.dto.request.CreateAuthorRequest;
import com.mz.blog.dto.request.UpdateAuthorRequest;
import com.mz.blog.dto.response.AuthorResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.entity.Author;
import com.mz.blog.exception.DuplicateResourceException;
import com.mz.blog.exception.ResourceNotFoundException;
import com.mz.blog.mapper.AuthorMapper;
import com.mz.blog.repository.AuthorRepository;
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
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Transactional
    public AuthorResponse createAuthor(CreateAuthorRequest request) {
        log.info("Creating Author with email: {}", request.getEmail());

        if (authorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Author","email", request.getEmail());
        }
        Author author = authorMapper.toEntity(request);

        Author savedAuthor = authorRepository.save(author);
        log.info("Author Created successfully with ID: {}", savedAuthor.getId());

        return authorMapper.toResponse(savedAuthor);
    }

    @Transactional(readOnly = true)
    public AuthorResponse getAuthorById(Long id) {
        log.info("Retrieving Author with ID: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Author", "id", id));

        return authorMapper.toResponse(author);
    }

    @Transactional(readOnly = true)
    public PageResponse<AuthorResponse> getAllAuthors(int page,int size,String sortBy,String sortDir){
        log.info("Retrieving all Authors - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch from DB
        Page<Author> authorPage = authorRepository.findAll(pageable);

        //convert to DTO
        List<AuthorResponse> authorResponses = authorPage.getContent()
                .stream()
                .map(authorMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<AuthorResponse>builder()
                .content(authorResponses)
                .pageNumber(authorPage.getNumber())
                .pageSize(authorPage.getSize())
                .totalElements(authorPage.getTotalElements())
                .totalPages(authorPage.getTotalPages())
                .last(authorPage.isLast())
                .first(authorPage.isFirst())
                .build();
    }

    @Transactional
    public AuthorResponse updateAuthor(Long id,UpdateAuthorRequest request) {
        log.info("Updating Author with ID: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Author", "id", id));

        if(request.getEmail()!=null && !request.getEmail().equals(author.getEmail())) {
            if(authorRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Author","email", request.getEmail());
            }
        }
        authorMapper.updateEntity(author, request);

        Author updatedAuthor = authorRepository.save(author);
        log.info("Author Updated successfully with ID: {}", updatedAuthor.getId());
        return authorMapper.toResponse(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        log.info("Deleting Author with ID: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(author);
        log.info("Author Deleted successfully with ID: {}", id);
    }
    @Transactional(readOnly = true)
    public Author getAuthorEntityById(Long id) {
        log.info("Retrieving Author with ID: {}", id);
        return authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Author", "id", id));
    }
}
