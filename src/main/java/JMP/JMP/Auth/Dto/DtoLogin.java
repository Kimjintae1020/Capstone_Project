package JMP.JMP.Auth.Dto;


import JMP.JMP.Enum.Role;
import lombok.Getter;

@Getter
public class DtoLogin {

    private String email;
    private String password;
    private Role role;
}
