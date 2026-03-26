package com.yonsai.geminiAi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonsai.geminiAi.DTO.BlogGenerateRequest;
import com.yonsai.geminiAi.service.BlogAiService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "*")
public class BlogAiController {

    @Autowired
    private BlogAiService blogAiService;
   
    @GetMapping("/blog-ai")
    public String blogAi(){

        blogAiService.write();
        return "블로그 AI";
    }

    //블로그 생성 API
    // @Valid  자동으로 검증 실행 
    @PostMapping("/blog/generate")
    public String blogGenerate(@Valid 
                               @RequestBody BlogGenerateRequest request) {
        System.out.println("=== AI 블로그 생성 요청 ===");
        System.out.println("주제: " + request.getTopic());
        System.out.println("타입: " + request.getType());
        
        blogAiService.generateBlog(request.getTopic(),
                                   request.getType());

        System.out.println("글쓰기 성공?");

        return "글 작성완료!";
    }
    



}
