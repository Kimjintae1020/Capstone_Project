package JMP.JMP.Auth.Service.util;

public class TokenConst {

    // 기본 토큰 타입
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    // 엑세스 토큰, 리프레시 토큰
    public static final String TOKEN_TYPE_ROLE = "role";
    public static final long ACCESS_TOKEN_EXPIRATION = 1800000L; // 30분
    public static final long REFRESH_TOKEN_EXPIRATION = 86400000L; // 24시간
    public static final long ACCESS_TOKEN_EXPRIRED_MS = 15 * 60 * 1000L;
}
