package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.DevPosition;
import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Resume.Entity.Resume;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DtoResumeDetail {

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

    public DtoResumeDetail(Resume resume) {
        this.resumeId = resume.getResumeId();
        this.title = resume.getTitle();
        this.intro = resume.getIntro();
        this.devposition = resume.getDevposition();
        this.skills = resume.getSkills();
        this.photo = resume.getPhoto();
        this.visible = resume.isVisible();

        this.projects = resume.getProjects().stream()
                .map(DtoResumeProject::new)
                .collect(Collectors.toList());

        this.githubUrl = resume.getGithuburl();
        this.introduce = resume.getIntroduce();
    }
}
