package JMP.JMP.Company.Entity;

import JMP.JMP.Enum.PostRole;
import JMP.JMP.Enum.Role;
import JMP.JMP.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "COMPANY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "COMPANY_ID")
        private Long id;

        @Column(name = "EMAIL", nullable = false, unique = true)
        private String email;

        @Column(name = "PASSWORD", nullable = false)
        private String password;

        @Column(name = "NAME", nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(name = "GENDER", nullable = false)
        private Gender gender;

        @Column(name = "PHONE", nullable = false, unique = true)
        private String phone;

        @Column(name = "BUSSINESS_NUMBER", nullable = false, unique = true)
        private String businessNumber;

        @Column(name = "POSITION", nullable = false)
        private String position;

        @Column(name = "COMPANY_NAME", nullable = false)
        private String companyName;        // 회사명

        @Column(name = "COMPANY_LOCATION", nullable = false)
        private String companyLocation;    // 회사 주소

        @Column(name = "POST_ROLE", nullable = false)
        @Enumerated(EnumType.STRING)
        private PostRole postRole = PostRole.PENDING;    // 공고 작성 권한

        @Column(name = "CREATED_AT")
        private LocalDate createdAt;

        @Column(name = "ROLE", nullable = false)
        @Enumerated(EnumType.STRING)
        private Role role = Role.COMPANY;

        @PrePersist
        protected void onCreate() {
                this.createdAt = LocalDate.now();
        }
 }
