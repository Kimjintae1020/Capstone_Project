package JMP.JMP.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RecruitDeadlineValidator.class) // 아래 구현할 Validator 클래스 연결
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRecruitDeadline {

    String message() default "모집 마감일은 프로젝트 시작일보다 이전이어야 합니다.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

