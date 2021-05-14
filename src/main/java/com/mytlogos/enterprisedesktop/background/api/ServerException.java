package com.mytlogos.enterprisedesktop.background.api;

import java.io.IOException;

public class ServerException extends IOException {
    /**
     * Alibi Uid.
     */
    private static final long serialVersionUID = 2788180305707794052L;
    public final int responseCode;
    public final String errorMessage;

    public ServerException(int responseCode, String errorMessage) {
        super(String.format("No Body, Http-Code %d, ErrorBody: %s", responseCode, errorMessage));
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
    }

    public ServerException(Throwable cause, int responseCode, String errorMessage) {
        super(cause);
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
    }

    public ServerException(ServerException cause) {
        super(cause);
        this.responseCode = cause.responseCode;
        this.errorMessage = cause.errorMessage;
    }
}
