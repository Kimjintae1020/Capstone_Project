package JMP.JMP.Error;

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
        COMPANY_NOT_FOUND(404, "COMPANY-004", "찾을 수 없는 기업 담당자입니다."),

        // 권한 관련
        INVALID_ROLE(400, "ROLE-001", "잘못된 권한 입니다."),
        NO_POST_PERMISSION(403, "ROLE-002", "프로젝트 공고를 작성할 권한이 없습니다."),

        // 인증 관련
        NOT_AUTHENTICATED(401, "AUTH-001", "로그인 되어 있지 않습니다."),
        TOKEN_EXPIRED(401, "AUTH-002", "토큰이 만료되었습니다."),
        INVALID_TOKEN(401, "AUTH-003", "유효하지 않은 토큰입니다."),
        UNAUTHORIZED_ACCESS(403, "AUTH-003", "접근 권한이 없습니다."),


        // 지원 관련
        APPLICATION_CLOSED(400, "APPLY-001", "지원 마감된 프로젝트입니다."),
        ALREADY_APPLIED(400, "APPLY-002", "이미 지원한 공고입니다."),
        APPLY_NOT_FOUND(404, "APPLY-003", "찾을 수 없는 지원처리입니다."),
        INVALID_ACCESS(403, "APPLY-004", "접근 권한이 없습니다."),
        INVALID_STATUS(400, "APPLY-005", "지원 상태 값이 유효하지 않습니다."),
        ALREADY_SET_STATUS(400, "APPLY-006", "이미 해당 상태로 처리된 지원자입니다."),


        // 기업 공고글 관련
        PROJECT_NOT_FOUND(404, "PROJECT_001", "프로젝트가 존재하지 않습니다."),
        INVALID_PROJECT_DEADLINE(400, "PROJECT-002", "모집 마감일은 프로젝트 시작일보다 이전이어야 합니다."),

        // JWT 관련
        JWT_EXPIRED(401, "JWT-001", "로그인이 만료되었습니다. 다시 로그인 해주세요."),

        // 이력서 관련
        RESUME_NOT_FOUND(404, "RESUME-001", "이력서를 찾을 수 없습니다."),
        RESUME_NOT_OWNED(403, "RESUME-002", "해당 이력서에 접근할 수 없습니다."),

        // 관리자 관련
        INVALID_ADMIN_ROLE(400, "ADMIN-001", "잘못된 권한 입니다."),
        ALREADY_SET_POST_STATUS(400, "ADMIN-002", "이미 승인된 기업 담당자입니다.");




        private final int status;
        private final String code;
        private final String description;

        ErrorCode(int status, String code, String description) {
            this.status = status;
            this.code = code;
            this.description = description;
        }

}
