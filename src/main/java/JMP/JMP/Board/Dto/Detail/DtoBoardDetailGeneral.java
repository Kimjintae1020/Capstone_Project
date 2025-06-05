package JMP.JMP.Board.Dto.Detail;

import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardDetailGeneral {
    private Long boardId;
    private String title;
    private String description;
    private BoardType boardType; // 카테고리
    private List<Tag> tags;
    private int viewCount;
    private LocalDateTime createdAt;
}

