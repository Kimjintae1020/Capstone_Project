package JMP.JMP.Project.Dto;

import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Enum.DevPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DtoProjectApplicants {

    private String name;   // 지원자 이름
    private String photo; // 이력서 사진
    private DevPosition devposition; // 지원 포지션
    private ApplyStatus status; // 상태 (예: 검토중, 합격, 불합격)
    private LocalDate appliedAt;  // 지원 날짜


}
