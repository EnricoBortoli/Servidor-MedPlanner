package medplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CPFInvalidoException extends RuntimeException {

    public CPFInvalidoException(String message) {
        super(message);
    }
}
