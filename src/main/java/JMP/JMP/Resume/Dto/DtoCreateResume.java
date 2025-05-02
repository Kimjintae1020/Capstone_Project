package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.DevPosition;
import JMP.JMP.Enum.Education;
import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Resume.Entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCreateResume {

    private String title;
    private String intro;
    private RequiredSkill skill;
    private String githubUrl;
    private boolean visible;
    private DevPosition devposition;

}
