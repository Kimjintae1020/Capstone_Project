package JMP.JMP.Enum;

import lombok.Getter;

@Getter
public enum ErrorCode {

        // 공통 오류
        INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우"),
        INTERNAL_SERVER_ERROR(500, "COMMON-002", "서버에서 처리할 수 없는 경우"),

        // 계정 관련 (사용자)
        DUPLICATE_EMAIL(409, "ACCOUNT-001", "이미 사용 중인 이메일입니다."),
        DUPLICATE_PHONE(409, "ACCOUNT-002", "이미 사용 중인 핸드폰입니다."),
        EMAIL_NOT_FOUND(404, "ACCOUNT-003", "해당 이메일로 등록된 사용자가 없습니다."),
        INVALID_PASSWORD(409, "ACCOUNT-004", "비밀번호가 일치하지 않습니다."),

        // 계정 관련 (기업 담당자)
        DUPLICATE_COMPANY_EMAIL(409, "COMPANY-001", "이미 사용 중인 기업 담당자 이메일입니다."),
        DUPLICATE_COMPANY_PHONE(409, "COMPANY-002", "이미 사용 중인 기업 담당자 핸드폰입니다."),
        DUPLICATE_BUSSINESS_NUMBER(409, "COMPANY-003", "이미 등록되어 있는 사업자 번호입니다."),

        // 권한 관련
        INVALID_ROLE(400, "ROLE-001", "잘못된 권한 입니다."),
        NO_POST_PERMISSION(403, "ROLE-002", "프로젝트 공고를 작성할 권한이 없습니다."),



        // 인증 관련
        NOT_AUTHENTICATED(401, "AUTH-001", "로그인 되어 있지 않습니다."),
        TOKEN_EXPIRED(401, "AUTH-002", "토큰이 만료되었습니다."),
        INVALID_TOKEN(401, "AUTH-003", "유효하지 않은 토큰입니다.");



        private final int status;
        private final String code;
        private final String description;

        ErrorCode(int status, String code, String description) {
            this.status = status;
            this.code = code;
            this.description = description;
        }

}
