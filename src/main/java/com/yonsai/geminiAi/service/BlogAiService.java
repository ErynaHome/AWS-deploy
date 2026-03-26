package com.yonsai.geminiAi.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonsai.geminiAi.entity.Blog;
import com.yonsai.geminiAi.entity.BlogType;
import com.yonsai.geminiAi.repository.BlogReposiotry;

@Service
public class BlogAiService {
    
    // AI 머리(엔진 선택!)
    @Autowired
	private ChatModel chatModel; //자동 주입!


	@Autowired
	private BlogReposiotry blogReposiotry;

	public String generateBlog(String topic,BlogType type){
		System.out.println(">>> AI 생성 시작: " + topic + " (" + type + ")");
        
		// 1. 타입별ㄹ 프롬프트 생성
		String prompt = createPromptByType(topic,type);
		System.out.println(">>> 프롬프트 길이: " + prompt.length() + "자");
       
		// 2. Gemini AI호출 온도적용!
		System.out.println(">>> Gemini API 호출 중... (temp=" + type.getTemperature() + ")");
        
		ChatClient chatClient = ChatClient
								.builder(chatModel)
								.build();
		String aiResp = chatClient
							.prompt()  // 질문 시작!
							.user(prompt) //사용자 메시지!
							.options(ChatOptions.builder()
												.temperature(type.getTemperature())
												.build())
							.call()
							.content();

		System.out.println(">>> AI 응답 받음! 길이: " + aiResp.length() + "자");
        
		//3. blog 엔티티 생성
		Blog blog = new Blog();
		blog.setTitle(topic);
		blog.setContent(aiResp);
		blog.setType(type);
		blog.setTags(extractTags(topic));

		// 4. 저장
		blogReposiotry.save(blog);
		System.out.println("블로그 글 DB저장 완료!");

		return aiResp;
	}

	/**
	 * SNS 타입별 프롬프트 생성
	 */

	private String createPromptByType(String topic,BlogType type){

		return switch(type){
			case BLOG -> createBlogPrompt(topic);
			case TWITTER -> createTwitterPrompt(topic);
			case INSTAGRAM -> createInstagramPrompt(topic);
			 case FACEBOOK -> createFacebookPrompt(topic);
		};
	}

	  private String createBlogPrompt(String topic) {
        return """
            당신은 10년 경력의 IT 기술 블로거입니다.
            
            주제: %s
            
            **목표: 정확하고 신뢰할 수 있는 기술 가이드 작성**
            
            형식:
            ## 서론
            - 2-3문장
            - 왜 이 기술이 중요한지
            
            ## 본문
            - 3-5개 섹션
            - 각 섹션마다 ### 소제목
            - 코드 예제 (```언어 형식)
            - 실무 팁
            
            ## 결론
            - 핵심 요약
            - 다음 학습 방향
            
            작성 규칙:
            - 톤: 친근하지만 전문적
            - 대상: 초보자-중급자
            - 길이: 1500자 내외
            - 이모지: 섹션당 1-2개만
            - 정확성 최우선
            
            절대 하지 마세요:
            - JSON 형식으로 출력
            - 메타 설명 추가
            
            지금 바로 시작하세요!
            """.formatted(topic);
	  }

	   private String createTwitterPrompt(String topic) {
        return """
            당신은 IT 인플루언서입니다. 트위터에 올릴 짧고 임팩트 있는 글을 작성하세요!
            
            주제: %s
            
            **목표: 리트윗하고 싶게 만들기!**
            
            형식:
            1. 강렬한 첫 문장 (훅!)
            2. 핵심 인사이트 (2-3줄)
            3. 행동 유도 또는 질문
            
            제약:
            - 최대 280자 (공백 포함)
            - 해시태그 2-3개 포함
            - 이모지 적절히 사용
            
            톤:
            - 재치있고 트렌디하게
            - 밈 문화 활용 가능
            - 약간의 유머 OK
            
            예시 스타일:
            "🔥 %s 배우는데 3일 걸렸던 나...
            지금 알았으면 3시간이면 됐을 꿀팁 👇
            
            [핵심 내용 2줄]
            
            당신은 어떻게 배우셨나요?
            
            #개발자 #%s"
            
            **창의적으로 작성하되, 280자 엄수!**
            """.formatted(topic, topic, topic);
            // topic이 3번 들어감 (%s가 3개)
    }

	 private String createInstagramPrompt(String topic) {
        return """
            당신은 IT 크리에이터입니다. 인스타그램에 올릴 감성적인 글을 작성하세요!
            
            주제: %s
            
            **목표: 공감과 저장!**
            
            형식:
            1. 이모지로 시작하는 후킹 문구
            2. 개인적 경험/스토리 (공감 유도)
            3. 핵심 팁 3-5가지 (넘버링)
            4. 마무리 + 행동 유도
            5. 해시태그 (10-15개)
            
            톤:
            - 따뜻하고 친근하게
            - "우리", "함께" 같은 단어 사용
            - 개인적 경험 공유
            
            이모지:
            - 각 팁마다 관련 이모지
            - 감정 표현 이모지
            - 최소 15-20개 사용
            
            길이: 800-1200자
            
            예시 시작:
            "✨ %s 시작했을 때의 나...
            
            막막하고 어디서부터 시작해야 할지
            몰라서 며칠을 헤맸던 기억이 나요 😅
            
            그때 알았으면 좋았을 것들 👇
            
            1️⃣ [팁1] 💡
            2️⃣ [팁2] 🚀
            3️⃣ [팁3] ✅
            
            여러분도 이런 경험 있으신가요?
            댓글로 공유해주세요! 💬
            
            #개발 #%s #코딩 #개발자..."
            
            **감성적이고 공감 가능하게 작성!**
            """.formatted(topic, topic, topic);
    }

	private String createFacebookPrompt(String topic) {
        return """
            당신은 개발자 커뮤니티 리더입니다. 페이스북 그룹에 올릴 글을 작성하세요.
            
            주제: %s
            
            **목표: 토론과 참여 유도!**
            
            형식:
            1. 공감 가는 상황 묘사
            2. 질문 제기
            3. 본인 경험 공유
            4. 커뮤니티 의견 요청
            
            톤:
            - 친근하고 대화하듯
            - 질문 많이
            - 커뮤니티 참여 유도
            
            길이: 800-1000자
            이모지: 적당히 (5-10개)
            
            예시 시작:
            "%s 관련해서 궁금한 게 있어요!
            
            여러분은 어떻게 생각하시나요?
            
            저는 이렇게 해봤는데...
            
            댓글로 의견 공유해주세요!"
            
            **토론을 유발하는 질문으로 마무리!**
            """.formatted(topic, topic);
    }

	// 주제에서 태그 추출
	//  공백으로 분리해서 쉼표로 연결 
	private String extractTags(String topic){
		String[] words = topic.split(" ");
		
		//  ["Docker","입문"] - Docker, 입문
		return String.join(", ",words);
	}





    //생성자를 이용해서 ChatClient의 객체를 

    public void write(){

        // 재미니엔진 객체를 먼저 선택한다.(빠른모델,오래생각모델)
        ChatClient chatClient = ChatClient
								.builder(chatModel)
								.build();

        // 재미니한테 질문을 할 수있는 핵심! 객체
        String hot = chatClient
							.prompt()
							.system("10년차 파워블로그 블로그글을 잘 쓰는 크리에이터야")
							.user("도커 컴포즈에 대한 설정 글을 5줄이네로 작성해줘!")
							.options(ChatOptions.builder()
												.temperature(2.0)
												.build())
							.call()
							.content();
    }
}
