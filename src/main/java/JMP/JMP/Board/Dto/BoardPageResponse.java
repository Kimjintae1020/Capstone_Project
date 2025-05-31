package JMP.JMP.Board.Dto;

import JMP.JMP.Board.Entity.Board;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardPageResponse {
    private int page;         // 현재 페이지
    private int total_pages;  // 전체 페이지 수
    private int total_items;  // 전체 아이템 개수
    private List<DtoBoardPage> postits;

    public BoardPageResponse(int page, int totalPages, int totalItems, List<Board> posts) {
        this.page = page;
        this.total_pages = totalPages;
        this.total_items = totalItems;
        this.postits = posts.stream()
                .map(DtoBoardPage::new)
                .collect(Collectors.toList());
    }
}
