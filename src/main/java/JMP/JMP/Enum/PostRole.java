package JMP.JMP.Enum;

import lombok.Getter;

@Getter
public enum PostRole {

    PENDING("ROLE_PENDING"),    // 대기
    APPROVED("ROLE_APPROVED");    // 대기

    private String value;

    PostRole(String value) {
            this.value = value;
        }
    }


