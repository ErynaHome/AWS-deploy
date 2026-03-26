package com.yonsai.geminiAi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    

    // Varchar(255) 
    // varchar(5000) 인덱스 비효율적!
    // TEXT - 최대 65,535자 
    // LONGTEXT - 최대 4GB

    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 200)
    private String tags;
    
    @Column(name = "blog_type")
    @Enumerated(EnumType.STRING)
    private BlogType type;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // insert 직전에 자동 실행 
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
