package JMP.JMP.Auth.Dto;

import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Enum.Gender;
import JMP.JMP.Enum.PostRole;
import JMP.JMP.Enum.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoMypageCompany {

    private String email;
    private String name;
    private Gender gender;
    private String phone;
    private String businessNumber;
    private String position;
    private String companyName;
    private String companyLocation;
    private PostRole postRole;
    private LocalDate createdAt;

    public DtoMypageCompany(Company company) {
        this.email = company.getEmail();
        this.name = company.getName();
        this.gender = company.getGender();
        this.phone = company.getPhone();
        this.businessNumber = company.getBusinessNumber();
        this.position = company.getPosition();
        this.companyName = company.getCompanyName();
        this.companyLocation = company.getCompanyLocation();
        this.postRole = company.getPostRole();
        this.createdAt = company.getCreatedAt();
    }
}
