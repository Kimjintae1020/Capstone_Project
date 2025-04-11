package JMP.JMP.Enum;

public enum Education {

    HIGH_SCHOOL("고졸"),
    COLLEGE_FRESHMAN("대학교 1학년"),
    COLLEGE_SOPHOMORE("대학교 2학년"),
    COLLEGE_JUNIOR("대학교 3학년"),
    COLLEGE_SENIOR("대학교 4학년"),
    COLLEGE_GRADUATE("대졸");

    private final String displayName;

    Education(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
