package com.yonsai.geminiAi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yonsai.geminiAi.entity.Blog;

public interface BlogReposiotry extends JpaRepository<Blog,Long>{
    
    
}
