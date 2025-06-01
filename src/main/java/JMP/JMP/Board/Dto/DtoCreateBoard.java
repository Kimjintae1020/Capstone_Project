package JMP.JMP.Board.Dto;

import JMP.JMP.Enum.Tag;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.RequiredSkill;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DtoCreateBoard {

    // 공통
    private BoardType boardType;
    private String title;
    private String description;

    // 일반 게시글
    private List<Tag> tags;

    // 프로젝트 게시글 / 스터디 게시글 공용
    private Integer recruitCount;
    private List<RequiredSkill> requiredSkills;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String applyMethod;

    // 프로젝트 게시글
    private String projectWarning;

    // 스터디 게시글
    private LocalDate studyStartDate;
    private LocalDate studyEndDate;
    private String studyCurriculum;
    private String studyWarning;
}
