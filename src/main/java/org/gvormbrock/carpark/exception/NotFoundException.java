package org.gvormbrock.carpark.exception;

public class NotFoundException extends ErrorServerException {
    public NotFoundException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
