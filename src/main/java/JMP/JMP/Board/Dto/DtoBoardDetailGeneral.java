package JMP.JMP.Board.Dto;

import JMP.JMP.Enum.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardDetailGeneral {
    private Long boardId;
    private String title;
    private String description;
    private List<Tag> tags;
    private int viewCount;
    private LocalDate createdAt;
}

