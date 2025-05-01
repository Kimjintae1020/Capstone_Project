package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.RequiredSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoResume {

    private String title;
    private String content;
    private RequiredSkill skill;
    private String resumeFileUrl;
    private boolean visible;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
