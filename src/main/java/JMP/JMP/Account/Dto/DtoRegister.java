package JMP.JMP.Account.Dto;

import JMP.JMP.Enum.Education;
import JMP.JMP.Enum.Gender;
import JMP.JMP.Enum.Major;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoRegister {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String birthYear;
    private Gender gender;
    private String phone;
    private Major major;
    private Education education;

}
