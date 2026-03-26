package com.yonsai.geminiAi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Flux;

@Controller
public class StreamController {
	//실시간으로 답변이 보이는 프로그램 만들기 
	// 스트리밍이란?
	//  - 데이터를 한번에 다 받지 않고 생성되는 즉시 조각조각 받는 
	//    방식 
	
	// Server-Sent-Events
	//  - 서버에서 클라이언트로 데이터를 실시간으로 밀어주는 기술
	//  - 요청 -> 응답,응답,응답(계속)
	
	// Flux 
	//  - 여러 개의 데이터를 순서대로 흘려보내는 타입!
	//  - 한 개 String
	//    여러 개 Flux<String>
	
	@Autowired
	private ChatModel chatModel; //자동 주입!

	@GetMapping("/page")
	public String page() {
		return "gemini";
	}
	
	
	
	/**
	 * 
	 * 실시간 스트리밍 API
	 *   produces = MediaType.TEXT_EVENT_STREAM_VALUE
	 *   SSE 방식으로 응답(글자를 조각조각 내서 실시간 전송!)핵심!
	 *   
	 * GET /stream?msg=질문내용
	 * 
	 * @param msg 질문 내용!
	 * @return Flux<String> 여러 글자 조각을 순서대로 보낸다.
	 */
	
	
//	타임리프를 거치지 말고~ 바로 gemini.html로 이동해라!
	@GetMapping(value="/stream",
			    produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public Flux<String> gemini(@RequestParam String msg){
		
		return ChatClient
					.builder(chatModel)
					.build()
					.prompt()
					.system("당신은 친절한 AI 어시스턴트입니다.")
					.user(msg)
					.stream() // 생성 즉시 조각조각 전송!
					.content();
	}
	
	
	
}
