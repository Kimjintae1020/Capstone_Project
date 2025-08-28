package JMP.JMP.Auth.Dto;

import JMP.JMP.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class ReussueSuccessResponse<T> {
    private final int status;
    private final String message;
    private final String accessToken;
    private long expiresIn;
    private ZonedDateTime expiresAt;

    public static <T> ReussueSuccessResponse<T> of(int status, String message,String accessToken, long expiresIn, ZonedDateTime expiresAt) {
        return new ReussueSuccessResponse<>(status, message,accessToken, expiresIn, expiresAt);
    }
}