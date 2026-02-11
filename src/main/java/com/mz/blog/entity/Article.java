package com.mz.blog.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="articles" , indexes = {
        @Index(name = "idx_author",columnList = "author_id"),
        @Index(name = "idx_category",columnList = "category_id"),
        @Index(name = "idx_status",columnList = "status"),
        @Index(name = "idx_published",columnList = "published_on")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,unique = true)
    private String slug;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    private String excerpt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status = ArticleStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id",nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "view_count")
    private Integer viewCount=0;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @CreationTimestamp
    @Column(name = "created_on",nullable = false,updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void publish(){
        this.status=ArticleStatus.PUBLISHED;
        this.publishedOn=LocalDateTime.now();
    }
}
