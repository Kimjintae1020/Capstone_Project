package JMP.JMP.Account.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private final int status;
    private final String message;

    public static <T> SuccessResponse<T> of(int status, String message) {
        return new SuccessResponse<>(status, message);
    }
}