package JMP.JMP.Account.Dto;

import JMP.JMP.Account.Entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Account account;

    // Role 값을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add((new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return account.getRole().name();
            }
        }));


        return collection;
    }

    @Override
    public String getPassword() {

        return account.getPassword();
    }

    @Override
    public String getUsername() {

        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}