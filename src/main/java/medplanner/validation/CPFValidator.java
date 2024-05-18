package medplanner.validation;

import medplanner.validation.annotations.CPF;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public void initialize(CPF cpf) {
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            return false;
        }

        return isCPFValid(cpf);
    }

    private boolean isCPFValid(String cpf) {
        int[] cpfArray = cpf.chars().map(c -> c - '0').toArray();

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += cpfArray[i] * (10 - i);
        }

        int firstDigit = 11 - (sum % 11);
        if (firstDigit > 9) {
            firstDigit = 0;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += cpfArray[i] * (11 - i);
        }

        int secondDigit = 11 - (sum % 11);
        if (secondDigit > 9) {
            secondDigit = 0;
        }

        return cpfArray[9] == firstDigit && cpfArray[10] == secondDigit;
    }
}