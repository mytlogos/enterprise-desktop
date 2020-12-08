package com.mytlogos.enterprisedesktop.tools;

import java.io.IOException;

public class NotEnoughSpaceException extends IOException {
    /**
     * Alibi Uid.
     */
    private static final long serialVersionUID = 3397480902909897686L;

    public NotEnoughSpaceException() {
    }

    public NotEnoughSpaceException(String message) {
        super(message);
    }

    public NotEnoughSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughSpaceException(Throwable cause) {
        super(cause);
    }
}
