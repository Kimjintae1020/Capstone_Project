package JMP.JMP.Company.Mapper;

import JMP.JMP.Company.Dto.DtoCompanyRegister;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Enum.PostRole;
import JMP.JMP.Enum.Role;

public class CompanyMapper {

    public static Company toEntity(String encodedPassword, DtoCompanyRegister dtoCompanyRegister){
        return Company.builder()
                .email(dtoCompanyRegister.getEmail())
                .password(encodedPassword)
                .name(dtoCompanyRegister.getName())
                .gender(dtoCompanyRegister.getGender())
                .phone(dtoCompanyRegister.getPhone())
                .businessNumber(dtoCompanyRegister.getBusinessNumber())
                .position(dtoCompanyRegister.getPosition())
                .companyLocation(dtoCompanyRegister.getCompanyLocation())
                .companyName(dtoCompanyRegister.getCompanyName())
                .postRole(PostRole.PENDING)
                .role(Role.COMPANY)
                .build();
    }
}
