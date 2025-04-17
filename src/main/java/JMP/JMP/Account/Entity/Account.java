package JMP.JMP.Account.Entity;

import JMP.JMP.Enum.Education;
import JMP.JMP.Enum.Gender;
import JMP.JMP.Enum.Major;
import JMP.JMP.Account.Role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "BIRTH_YEAR", nullable = false)
    private String birthYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = false)
    private Gender gender;

    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "MAJOR", nullable = false)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(name = "EDUCATION", nullable = false)
    private Education education;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
