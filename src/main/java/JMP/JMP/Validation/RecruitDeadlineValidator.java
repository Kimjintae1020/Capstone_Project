package JMP.JMP.Validation;

import JMP.JMP.Project.Dto.DtoCreateProject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RecruitDeadlineValidator implements ConstraintValidator<ValidRecruitDeadline, DtoCreateProject> {

    @Override
    public boolean isValid(DtoCreateProject dto, ConstraintValidatorContext context) {
        if (dto.getRecruitDeadline() == null || dto.getStartDate() == null) return true;
        return dto.getRecruitDeadline().isBefore(dto.getStartDate());
    }
}