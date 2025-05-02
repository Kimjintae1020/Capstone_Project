package JMP.JMP.Enum;

public enum DevPosition {
    BACKEND("서버/백엔드 개발자"),
    FRONTEND("프론트엔드 개발자"),
    FULLSTACK("웹 풀스택 개발자"),
    ANDROID("안드로이드 개발자"),
    IOS("iOS 개발자"),
    CROSS_PLATFORM("크로스플랫폼 앱개발자"),
    GAME_CLIENT("게임 클라이언트 개발자"),
    GAME_SERVER("게임 서버 개발자"),
    DBA("DBA"),
    BIGDATA("빅데이터 엔지니어"),
    AI_ML("인공지능/머신러닝"),
    DEVOPS("DevOps/시스템 엔지니어"),
    SECURITY("정보보안 담당자"),
    QA("QA 엔지니어"),
    PM("개발 PM"),
    EMBEDDED("HW/임베디드"),
    SOLUTION("SW/솔루션"),
    WEB_PUBLISHER("웹퍼블리셔"),
    VR_AR("VR/AR/3D"),
    BLOCKCHAIN("블록체인"),
    TECH_SUPPORT("기술지원");

    private final String displayName;

    DevPosition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}