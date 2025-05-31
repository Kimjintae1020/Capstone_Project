package JMP.JMP.Board.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Board.Dto.DtoCreateBoard;
import JMP.JMP.Enum.BoardCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "BOARD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    // 작성자 ID -> Account 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private Account writer;

    @Column(name = "BOARD_TITLE", nullable = false)
    private String title;           // 제목

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;     // 내용


    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory; // 카테고리

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

    public static Board createBoard(Account writer, DtoCreateBoard dto) {
        Board board = new Board();
        board.writer = writer;
        board.title = dto.getTitle();
        board.description = dto.getDescription();
        board.boardCategory = dto.getBoardCategory();
        return board;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDate.now();
    }

}
