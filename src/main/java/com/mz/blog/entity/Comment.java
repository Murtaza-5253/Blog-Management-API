package com.mz.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_article",columnList = "article_id"),
        @Index(name = "idx_approved",columnList = "approved")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id",nullable = false)
    private Article article;

    @Column(name = "author_name",nullable = false)
    private String authorName;

    @Column(name = "author_email",nullable = false)
    private String authorEmail;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean approved = false;

    @CreationTimestamp
    @Column(name="created_on",nullable = false)
    private LocalDateTime createdOn;
}
