package org.mataelang.kaspacore.exceptions;

public class PropertyRuntimeException extends RuntimeException{
    public PropertyRuntimeException() {
        super();
    }

    public PropertyRuntimeException(String message) {
        super(message);
    }

    public PropertyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyRuntimeException(Throwable cause) {
        super(cause);
    }

    public PropertyRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
