package JMP.JMP.Admin.Dto;

import JMP.JMP.Enum.Gender;
import JMP.JMP.Enum.PostRole;
import JMP.JMP.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DtoPendingCompanyList {

    private Long companyId;
    private String email;
    private String name;
    private Gender gender;
    private String phone;
    private String businessNumber;
    private String position;
    private String companyName;
    private String companyLocation;
    private LocalDate createdAt;
    private PostRole postRole;
}