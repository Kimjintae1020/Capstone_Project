package JMP.JMP.Enum;

public enum BoardType {

    GENERAL("일반 게시글"),
    PROJECT_RECRUIT("프로젝트 모집"),
    STUDY_RECRUIT("스터디 모집");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}