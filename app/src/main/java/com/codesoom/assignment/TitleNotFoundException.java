package com.codesoom.assignment;

/**
 * RequestBody로 넘어오는 title이 empty일 때 발생하는 런타임 예외
 */
public class TitleNotFoundException extends RuntimeException {

    public TitleNotFoundException() {
        super();
    }

    public TitleNotFoundException(String message) {
        super(message);
    }

    public TitleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TitleNotFoundException(Throwable cause) {
        super(cause);
    }

    protected TitleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
