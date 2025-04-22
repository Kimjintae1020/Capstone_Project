package JMP.JMP.Auth.Dto;


import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Enum.Education;
import JMP.JMP.Enum.Gender;
import JMP.JMP.Enum.Major;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoMypageAccount {

    private String email;
    private String name;
    private String birthYear;
    private Gender gender;
    private String phone;
    private Major major;
    private Education education;
    private LocalDate createdAt;

    public DtoMypageAccount(Account account) {
        this.email = account.getEmail();
        this.name = account.getName();
        this.birthYear = account.getBirthYear();
        this.gender = account.getGender();
        this.phone = account.getPhone();
        this.birthYear = account.getBirthYear();
        this.major = account.getMajor();
        this.education = account.getEducation();
        this.createdAt = account.getCreatedAt();

    }

}
