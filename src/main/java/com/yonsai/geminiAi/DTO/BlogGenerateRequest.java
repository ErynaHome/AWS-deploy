package com.yonsai.geminiAi.DTO;

import com.yonsai.geminiAi.entity.BlogType;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogGenerateRequest {
    
    @NotBlank(message = "주제를 입력하세요")
    @Size(min=2 ,max=100, message = "주제는 2~100자 여야합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\\\s,.-]+$",
             message = "특수문자는 사용할 수 없습니다(하이폰,점,쉼표 제외)"
    )
    private String topic;

    @NotNull(message = "글 타입을 선택하세요!")
    private BlogType type;

    /**
     * 태그(선택사항)
     *  Docker, spring boot, AI ...
     */
    @Size(max = 200, message = "태그는 200자 이하여야 합니다.")
    private String tag;

    @Min(value= 0,message = "Temperature 0 이상이어야 합니다.")
    @Max(value= 2,message = "Temperature 2 이하여야 합니다.")
    private Float customTemperature;

}
