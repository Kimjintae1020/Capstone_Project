package JMP.JMP.Enum;

public enum Tag {
    INDUSTRY_NEWS("업계소식"),
    INTRODUCTION("자기소개"),
    DISCUSSION("의견공유"),
    QUESTION("질문"),
    REVIEW("후기"),
    TIPS("팁공유"),
    DAILY_LIFE("일상"),
    COMMUNICATION("소통"),
    EMPATHY("공감"),
    RECOMMENDATION("추천"),
    INFORMATION("정보");

    private final String description;

    Tag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
