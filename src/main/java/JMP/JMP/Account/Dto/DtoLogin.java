package JMP.JMP.Account.Dto;


import JMP.JMP.Enum.Role;
import lombok.Getter;

@Getter
public class DtoLogin {

    private String email;
    private String password;
    private Role role;
}
