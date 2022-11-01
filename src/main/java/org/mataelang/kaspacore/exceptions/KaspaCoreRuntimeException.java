package org.mataelang.kaspacore.exceptions;

public class KaspaCoreRuntimeException extends RuntimeException {
    public KaspaCoreRuntimeException() {
        super();
    }

    public KaspaCoreRuntimeException(String message) {
        super(message);
    }

    public KaspaCoreRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public KaspaCoreRuntimeException(Throwable cause) {
        super(cause);
    }

    public KaspaCoreRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
