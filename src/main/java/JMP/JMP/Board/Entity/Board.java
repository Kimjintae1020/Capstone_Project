package JMP.JMP.Board.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Board.Dto.DtoCreateBoard;
import JMP.JMP.Board.Dto.DtoUpdateBoard;
import JMP.JMP.Comment.Entity.Comment;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Enum.Tag;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.CustomException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

@Entity
@Table(name = "BOARD")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    // 작성자 ID -> Account 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private Account writer;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;  // GENERAL, PROJECT_RECRUIT, STUDY_RECRUIT

    @Column(name = "BOARD_TITLE", nullable = false)
    private String title;           // 제목

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;     // 내용

    @Column(name = "VIEW_COUNT")
    private int viewCount;          // 조회수

    @Column(name = "LIKE_COUNT", nullable = false)
    private int likeCount;                                  // 좋아요

    // 일반 글 전용
    @ElementCollection(targetClass = Tag.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "BOARD_TAGS", joinColumns = @JoinColumn(name = "BOARD_ID"))
    @Column(name = "TAG")
    @Enumerated(EnumType.STRING)
    private List<Tag> tags;

    // 프로젝트/스터디 공용
    @Column(name = "RECRUITCOUNT")
    private Integer recruitCount;  // 모집인원 (프로젝트, 스터디)

    @ElementCollection(targetClass = RequiredSkill.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "BOARD_SKILLS", joinColumns = @JoinColumn(name = "BOARD_ID"))
    @Column(name = "REQUIRED_SKILL")
    @Enumerated(EnumType.STRING)
    private List<RequiredSkill> requiredSkills;

    @Column(name = "PROJECT_START_DATE")
    private LocalDate projectStartDate;

    @Column(name = "PROJECT_END_DATE")
    private LocalDate projectEndDate;

    // 프로젝트 전용
    @Column(name = "PROJECT_WARNING", columnDefinition = "TEXT")
    private String projectWarning;

    // 스터디 전용
    @Column(name = "STUDY_START_DATE")
    private LocalDate studyStartDate;   // 시작일 (스터디)

    @Column(name = "STUDY_END_DATE")
    private LocalDate studyEndDate;    // 마감일 (스터디)

    @Column(name = "STUDY_CURRICULM", columnDefinition = "TEXT")
    private String studyCurriculum;   // 에상 커리큘럼 (스터디)

    @Column(name = "STUDY_WARNING", columnDefinition = "TEXT")
    private String studyWarning;    // 관련 주의사항 (스터디)

    // 공통
    @Column(name = "APPLY_METHOD")
    private String applyMethod;    // 지원 방법

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();         // 댓글



    public static Board createBoard(Account writer, DtoCreateBoard dto) {
        Board board = new Board();
        board.writer = writer;
        board.boardType = dto.getBoardType();
        board.title = dto.getTitle();
        board.description = dto.getDescription();
        board.viewCount = 0;

        if (dto.getBoardType() == BoardType.GENERAL) {
            board.tags = dto.getTags();
        } else if (dto.getBoardType() == BoardType.PROJECT_RECRUIT) {
            board.recruitCount = dto.getRecruitCount();
            board.requiredSkills = dto.getRequiredSkills();
            board.projectStartDate = dto.getProjectStartDate();
            board.projectEndDate = dto.getProjectEndDate();
            board.projectWarning = dto.getProjectWarning();
            board.applyMethod = dto.getApplyMethod();
        } else if (dto.getBoardType() == BoardType.STUDY_RECRUIT) {
            board.recruitCount = dto.getRecruitCount();
            board.requiredSkills = dto.getRequiredSkills();
            board.studyStartDate = dto.getStudyStartDate();
            board.studyEndDate = dto.getStudyEndDate();
            board.studyCurriculum = dto.getStudyCurriculum();
            board.studyWarning = dto.getStudyWarning();
            board.applyMethod = dto.getApplyMethod();
        }

        return board;
    }


    public void updateBoard(Account findAccount, DtoUpdateBoard dto) {
        if (!this.writer.getId().equals(findAccount.getId())){
            throw new CustomException(ErrorCode.INVALID_ACCESS);
        }
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.updatedAt = LocalDateTime.now();

        if (dto.getBoardType() == BoardType.GENERAL) {
            this.tags = dto.getTags();
        } else if (dto.getBoardType() == BoardType.PROJECT_RECRUIT) {
            this.recruitCount = dto.getRecruitCount();
            this.requiredSkills = dto.getRequiredSkills();
            this.projectStartDate = dto.getProjectStartDate();
            this.projectEndDate = dto.getProjectEndDate();
            this.projectWarning = dto.getProjectWarning();
            this.applyMethod = dto.getApplyMethod();
        } else if (dto.getBoardType() == BoardType.STUDY_RECRUIT) {
            this.recruitCount = dto.getRecruitCount();
            this.requiredSkills = dto.getRequiredSkills();
            this.studyStartDate = dto.getStudyStartDate();
            this.studyEndDate = dto.getStudyEndDate();
            this.studyCurriculum = dto.getStudyCurriculum();
            this.studyWarning = dto.getStudyWarning();
            this.applyMethod = dto.getApplyMethod();
        }

    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}