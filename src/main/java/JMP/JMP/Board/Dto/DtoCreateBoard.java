package JMP.JMP.Board.Dto;

import JMP.JMP.Enum.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCreateBoard {

    private String title;           // 제목
    private String description;     // 내용
    private BoardCategory boardCategory; // 카테고리
}
