package JMP.JMP.AI.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectEvaluationResult {

    private Long id;
    private String title;
    private List<String> requiredSkill;
    private int score;
    private String details;
}
