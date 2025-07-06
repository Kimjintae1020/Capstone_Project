package JMP.JMP.Auth.Dto;

import JMP.JMP.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class LoginSuccessResponse<T> {
    private final int status;
    private final String message;
    private Role role;
    private long expiresIn;
    private ZonedDateTime expiresAt;

    public static <T> LoginSuccessResponse<T> of(int status, String message, Role role, long expiresIn, ZonedDateTime expiresAt) {
        return new LoginSuccessResponse<>(status, message,role, expiresIn, expiresAt);
    }
}