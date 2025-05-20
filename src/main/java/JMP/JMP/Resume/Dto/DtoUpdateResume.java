package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.DevPosition;
import JMP.JMP.Enum.RequiredSkill;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoUpdateResume {

    private Long resumeId;
    private String title;
    private String intro;
    private List<RequiredSkill> skills;
    private String githubUrl;
    private boolean visible;
    private DevPosition devposition;

    @Size(max = 500, message = "자기소개서는 500자 이내로 작성해주세요.")
    private String introduce;

    private List<DtoResumeProject> projects;


}
