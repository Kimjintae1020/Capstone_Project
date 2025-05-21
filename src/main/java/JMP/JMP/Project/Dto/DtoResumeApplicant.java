package JMP.JMP.Project.Dto;

import JMP.JMP.Enum.DevPosition;
import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Resume.Dto.DtoResumeProject;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoResumeApplicant {

    private Long resumeId;
    private String title;               // 이력서 제목
    private String intro;               // 이력서 한줄 소개
    private DevPosition devposition;        // 개발 직무, 포지션
    private List<RequiredSkill> skills;     // 기술스택
    private String photo;                   // 사진
    private boolean visible;                // 공개 범위
    private List<DtoResumeProject> projects;       // 프로젝트
    private String githubUrl;               // 깃허브 프로필 링크
    private String introduce;               // 자기소개서 500자 이내
}
