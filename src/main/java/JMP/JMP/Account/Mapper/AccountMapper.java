package JMP.JMP.Account.Mapper;

import JMP.JMP.Account.Dto.DtoRegister;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.Role;

public class AccountMapper {

    public static Account toEntity(String encodedPassword, DtoRegister dtoRegister) {
        return Account.builder()
                .email(dtoRegister.getEmail())
                .password(encodedPassword)
                .name(dtoRegister.getName())
                .birthYear(dtoRegister.getBirthYear())
                .gender(dtoRegister.getGender())
                .phone(dtoRegister.getPhone())
                .major(dtoRegister.getMajor())
                .education(dtoRegister.getEducation())
                .role(Role.USER)
                .build();
    }
}
