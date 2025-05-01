package JMP.JMP.Auth.Dto;

import JMP.JMP.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginSuccessResponse<T> {
    private final int status;
    private final String message;
    private Role role;

    public static <T> LoginSuccessResponse<T> of(int status, String message, Role role) {
        return new LoginSuccessResponse<>(status, message,role);
    }
}