package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.RequiredSkill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoResumeProject {

    @NotBlank
    private String name;

    @Size(max = 500)
    private String description;

    private List<RequiredSkill> techStack;
    private String githubLink;
}

