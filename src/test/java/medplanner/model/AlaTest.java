package medplanner.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlaTest {

    private final Validator validator;

    public AlaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidAla() {
        Ala ala = new Ala();
        ala.setNome("Ala Norte");
        ala.setSigla("AN");
        ala.setAndar(1);

        Set<ConstraintViolation<Ala>> violations = validator.validate(ala);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidAla() {
        Ala ala = new Ala();
        ala.setNome(""); // Nome inválido
        ala.setSigla("AN123456789"); // Sigla inválida
        ala.setAndar(-1); // Andar inválido

        Set<ConstraintViolation<Ala>> violations = validator.validate(ala);
        assertEquals(3, violations.size());
    }
}

