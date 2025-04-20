package JMP.JMP.Enum;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),            // 관리자

    USER("ROLE_USER"),              // 사용자

    COMPANY("ROLE_COMPANY");        // 기업 담당자

    private String value;
    Role(String value) {
        this.value = value;
    }

}

