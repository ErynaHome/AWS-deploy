package com.yonsai.geminiAi.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GeminiController {
	
	@Autowired
	private ChatModel chatModel; //자동 주입!

	
	/**
	 * spring ai의 온도(창의성)를 조절하면서 답변 받기 
	 * Get요청 
	 *   url = /temp
	 *   파라미터 = ?prefix=food
	 *           &input=질문
	 * @return
	 */
	@GetMapping("/temp")
	public Map<String , String > temp(
			@RequestParam String prefix,
			@RequestParam String input){
		
		System.out.println("넘어온 prefix: "+ prefix);
		System.out.println("넘어온 prefix: "+ input);
		
		
		//1. 엔진 객체 생성!
		ChatClient chatClient = ChatClient
								.builder(chatModel)
								.build();
		//2. 실험 종류별 메시지 구성				
		String system = getSystem(prefix);
		String user = getUser(prefix,input);
		
		//3. 0.0
		String cold = chatClient
							.prompt()
							.system(system)
							.user(user)
							.options(ChatOptions.builder()
												.temperature(0.0)
												.build())
							.call()
							.content();
		//   1.0
		String warm = chatClient
							.prompt()
							.system(system)
							.user(user)
							.options(ChatOptions.builder()
												.temperature(1.0)
												.build())
							.call()
							.content();
		//   2.0
		String hot = chatClient
							.prompt()
							.system(system)
							.user(user)
							.options(ChatOptions.builder()
												.temperature(2.0)
												.build())
							.call()
							.content();
		System.out.println(cold);
		System.out.println("=============================");
		System.out.println();
		System.out.println(warm);
		
		System.out.println("=============================");
		System.out.println();
		
		System.out.println(hot);
			
					
		return Map.of("cold",cold,
					  "warm",warm
					  ,"hot",hot);
	}
	
	
	@GetMapping("/")
	public String gemini() {
		
		ChatClient chatClient = ChatClient
								.builder(chatModel)
								.build();
		String result = chatClient
				.prompt()
				.system("당신은 AI분야 전문가 입니다.")
				.user("스티브 잡스 명언 한 가지 알려줘")
				.call()
				.content();
		
		return result;
		
	}
	
	/**
	 * AI 역할 지정 
	 *  - prefix로 들어온 값을 이용해서 역할을 지정
	 * @param prefix  - 키워드 저장 
	 * @return String - 역할에 대한 내용을 보낸다.
	 */
	private String getSystem(String prefix) {
		// 자바 JDK 17버전에 부터 사용되는 switch문법!
		// 값을 반환하는 스위치 문법! 
		//  장점 : break가 없음 , 바로 값을 반환 , 휠씬 짧음
		//  -> 경우가 선택되면 이 값을 반환해라! 
		return switch(prefix) {
		case "food" -> "당신은 요리 전문가 입니다. 레시피를 알려주세요";
		case "ad"   -> "당신은 광고 카피라이터입니다. 광고문구를 만들주세요!";
		case "latter"  -> "당신은 작가입니다. 편지를 써주세요!";
		default -> "당신은 AI 어시스턴트 입니다!";		
		};
	}
	
	/**
	 *  User 질문을 정리해서 답변 받을 수있도록 설정하는 메서드!
	 * @param input  - 질문을 받는다.
	 * @return String - 질문의 줄 수 및 정리하는 내용들 추가한다.
	 */
	private String getUser(String prefix,String input) {
	
		return switch(prefix) {
		case "food"    ->  input + " 레피시를 3줄로 짧게 알려줘";
		case "ad"      ->  input + " 광고 카피를 한 문장으로 만들어줘";
		case "latter"  ->  input + " 5줄 이내로 짧게 써줘";
		default 	   -> input;		
		};
	}
	
	
	
	
	
}
