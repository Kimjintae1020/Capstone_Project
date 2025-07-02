package JMP.JMP.Resume.Mapper;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Resume.Dto.DtoCreateResume;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Entity.ResumeProject;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ResumeMapper {

    public static Resume toEntity(Account account, DtoCreateResume dtoCreateResume, String savedPath){
        Resume resume = new Resume();

        resume.setAccount(account);
        resume.setTitle(dtoCreateResume.getTitle());
        resume.setIntro(dtoCreateResume.getIntro());
        resume.setSkills(dtoCreateResume.getSkills());
        resume.setGithuburl(dtoCreateResume.getGithubUrl());
        resume.setVisible(dtoCreateResume.isVisible());
        resume.setDevposition(dtoCreateResume.getDevposition());
        resume.setPhoto(savedPath);
        resume.setIntroduce(dtoCreateResume.getIntroduce());

        List<ResumeProject> resumeProjects = dtoCreateResume.getProjects().stream()
                .map(dtoProject -> ResumeProject.builder()
                        .name(dtoProject.getName())
                        .description(dtoProject.getDescription())
                        .techStack(dtoProject.getTechStack())
                        .githubLink(dtoProject.getGithubLink())
                        .startDate(dtoProject.getStartDate())
                        .endDate(dtoProject.getEndDate())
                        .resume(resume)
                        .build())
                .collect(Collectors.toList());

        resume.setProjects(resumeProjects);
        resume.setCreatedAt(LocalDate.now());
        resume.setUpdatedAt(LocalDate.now());

        return resume;
    }
}
