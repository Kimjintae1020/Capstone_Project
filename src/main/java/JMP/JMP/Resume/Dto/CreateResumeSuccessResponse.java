package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateResumeSuccessResponse<T> {
    private final int status;
    private final String message;
    private Long resumeId;

    public static <T> CreateResumeSuccessResponse<T> of(int status, String message, Long resumeId) {
        return new CreateResumeSuccessResponse<>(status, message,resumeId);
    }
}