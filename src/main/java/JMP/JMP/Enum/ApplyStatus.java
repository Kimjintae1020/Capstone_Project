package JMP.JMP.Enum;

public enum ApplyStatus {

    PENDING("대기"),
    ACCEPTED("승인"),
    REJECTED("거절"),
    CANCELLED("취소"),
    COMPLETED("완료");

    private final String description;

    ApplyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
