package JMP.JMP.Project.Dto;

import JMP.JMP.Project.Entity.Project;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProjectPageResponse {
    private int page;         // 현재 페이지
    private int total_pages;  // 전체 페이지 수
    private int total_items;  // 전체 아이템 개수
    private List<DtoProjectPage> postits;

    public ProjectPageResponse(int page, int totalPages, int totalItems, List<Project> posts) {
        this.page = page;
        this.total_pages = totalPages;
        this.total_items = totalItems;
        this.postits = posts.stream()
                .map(DtoProjectPage::new)
                .collect(Collectors.toList());
    }
}
