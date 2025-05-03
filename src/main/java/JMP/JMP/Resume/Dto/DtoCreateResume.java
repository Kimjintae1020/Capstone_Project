package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.DevPosition;
import JMP.JMP.Enum.RequiredSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCreateResume {

    private String title;
    private String intro;
    private List<RequiredSkill> skills;
    private String githubUrl;
    private boolean visible;
    private DevPosition devposition;

}
