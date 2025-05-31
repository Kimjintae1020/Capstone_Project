package JMP.JMP.Enum;

public enum BoardCategory {

    GENERAL("일반 게시글"),
    PROJECT_RECRUIT("프로젝트 모집"),
    STUDY_RECRUIT("스터디 모집");

    private final String description;

    BoardCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}