package JMP.JMP.Enum;

public enum ApplyStatus {

    PENDING("검토중"),
    ACCEPTED("합격"),
    REJECTED("불합격");

    private final String description;

    ApplyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
