package JMP.JMP.Resume.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DtoResumeList {

    private Long resumeId;
    private String title;               // 이력서 제목
    private String intro;               // 이력서 한줄 소개
    private boolean visible;                // 공개 범위
    private LocalDate createdAt;                // 생성일
    private LocalDate updatedAt;                // 수정일


}
