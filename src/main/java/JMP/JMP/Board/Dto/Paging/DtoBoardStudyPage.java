package JMP.JMP.Board.Dto.Paging;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.RequiredSkill;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardStudyPage {

    private Long boardId;
    private String writer;
    private String title;           // 제목
    private String description;     // 내용
    private BoardType boardType; // 카테고리
    private int viewCount;
    private int likeCount;
    private Integer recruitCount;
    private List<RequiredSkill> skills;
    private LocalDate studyStartDate;   // 시작일 (스터디)
    private LocalDate studyEndDate;    // 마감일 (스터디)
    private String studyCurriculum;   // 에상 커리큘럼 (스터디)
    private String studyWarning;    // 관련 주의사항 (스터디)
    private String applyMethod;    // 지원 방법
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isMine;         // 본인이 작성한 글인지 여부를 알려주는 컬럼
    private int commentCount;       // 게시글 댓글 개수

    public DtoBoardStudyPage(Board board, Long currentAccountId) {
        this.boardId = board.getBoardId();
        this.writer = board.getWriter().getName();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.boardType = board.getBoardType();
        this.viewCount = board.getViewCount();
        this.likeCount = board.getLikeCount();
        this.recruitCount = board.getRecruitCount();
        this.skills = board.getRequiredSkills();
        this.studyStartDate = board.getStudyStartDate();
        this.studyEndDate = board.getStudyEndDate();
        this.studyCurriculum = board.getStudyCurriculum();
        this.studyWarning = board.getStudyWarning();
        this.applyMethod = board.getApplyMethod();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.isMine = board.getWriter().getId().equals(currentAccountId);
        this.commentCount = board.getComments().size();
    }


}
