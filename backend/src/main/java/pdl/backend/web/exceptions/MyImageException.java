package pdl.backend.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MyImageException extends RuntimeException {
    public MyImageException(String s) {
        super(s);
    }
}
