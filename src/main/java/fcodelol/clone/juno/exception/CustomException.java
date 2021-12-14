package fcodelol.clone.juno.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private int code;
    private String message;

    public CustomException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException() {
    }
}
