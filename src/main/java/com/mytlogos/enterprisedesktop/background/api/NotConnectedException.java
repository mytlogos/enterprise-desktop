package com.mytlogos.enterprisedesktop.background.api;

import java.io.IOException;

public class NotConnectedException extends IOException {
    /**
     * Alibi serial Uid.
     */
    private static final long serialVersionUID = 498941837484887801L;

    public NotConnectedException() {
    }

    public NotConnectedException(String message) {
        super(message);
    }

    public NotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotConnectedException(Throwable cause) {
        super(cause);
    }
}
