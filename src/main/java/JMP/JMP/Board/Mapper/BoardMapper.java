package JMP.JMP.Board.Mapper;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Board.Dto.DtoCreateBoard;
import JMP.JMP.Board.Entity.Board;

public class BoardMapper {

    public static Board toEntity(Account writer, DtoCreateBoard dto) {
        Board.BoardBuilder builder = Board.builder()
                .writer(writer)
                .boardType(dto.getBoardType())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .viewCount(0)
                .likeCount(0);

        switch (dto.getBoardType()) {
            case GENERAL -> builder.tags(dto.getTags());
            case PROJECT_RECRUIT -> builder
                    .recruitCount(dto.getRecruitCount())
                    .requiredSkills(dto.getRequiredSkills())
                    .projectStartDate(dto.getProjectStartDate())
                    .projectEndDate(dto.getProjectEndDate())
                    .projectWarning(dto.getProjectWarning())
                    .applyMethod(dto.getApplyMethod());
            case STUDY_RECRUIT -> builder
                    .recruitCount(dto.getRecruitCount())
                    .requiredSkills(dto.getRequiredSkills())
                    .studyStartDate(dto.getStudyStartDate())
                    .studyEndDate(dto.getStudyEndDate())
                    .studyCurriculum(dto.getStudyCurriculum())
                    .studyWarning(dto.getStudyWarning())
                    .applyMethod(dto.getApplyMethod());
        }

        return builder.build();
    }

}
