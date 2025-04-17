package JMP.JMP.Company.Dto;

import JMP.JMP.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCompanyRegister {

    private Long id;
    private String email;
    private String password;
    private String name;
    private Gender gender;
    private String phone;
    private String businessNumber;
    private String position;
    private String companyName;        // 회사명
    private String companyLocation;    // 회사 주소
    private LocalDate createdAt;

}
