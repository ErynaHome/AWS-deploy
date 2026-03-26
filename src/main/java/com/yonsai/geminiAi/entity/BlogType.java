package com.yonsai.geminiAi.entity;

import lombok.Data;

/**
 * 각 블로그나 SNS마다 타입이 혹은 AI창의성 레벨
 * 최대 글자 수 차이가 있을 수 있다.
 */

public enum BlogType {
    // 블로그 타입 마다 (SNS 플랫폼별 )
    /**
     * 블로그 정확하고 상세한 기술 가이드!
     */
    BLOG("블로그",0.5f,1500),

    /**
     * 트위터 - 짧고 임팩트있는 바이럴 글 
     *       - 글 제한
     */
    TWITTER("트위터",1.3f,250),
    /**
     * 인스타그램 - 감성적이고 공감되는 스토리 
     *          - 글제한 (2000)
     */
    INSTAGRAM("인스타그램",1.0f,2000),

    /**
     * 페이스북 - 친근한 커뮤니트 토론 글
     *        - 글 제한 (일기 쉽게 1000)
     */
    FACEBOOK("페이스북",0.7f,1000);

    /**
     * 한글명 표시 
     */
    private final String displayName;

    /**
     * 온도 AI
     */
    private final float temperature; 

    /**
     * 최대 글자 수 
     */
    private final int maxLength;

    BlogType(String displayName,float temperature,int maxLength){
        this.temperature = temperature;
        this.displayName = displayName;
        this.maxLength = maxLength;
    }

    // 문자열로 BlogType을 찾기 
    public static BlogType fromString(String name) {
        if (name == null) {
            return null;
        }
        
        try {
            return BlogType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Double getTemperature() {
            return (double)temperature;
    }
    

}

/**
     * AI 창의성 레벨
     * - 0.0~0.3: 매우 보수적 (정확성 최우선)
     * - 0.4~0.7: 균형잡힌 (일반적 용도)
     * - 0.8~1.3: 창의적 (SNS, 바이럴)
     * - 1.4~2.0: 매우 창의적 (실험용)
     */