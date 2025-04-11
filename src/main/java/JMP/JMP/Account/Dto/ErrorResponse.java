package JMP.JMP.Account.Dto;

import JMP.JMP.Enum.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String errorCode;
    private final String message;

    // ErrorCode enum 기반으로 ErrorResponse 생성하는 static 메서드
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getDescription()
        );
    }
}
