package org.gvormbrock.carpark.exception;

public class ErrorCode {
    private ErrorCode() {}
    public static final int TOWN_NOT_FOUND = 401;
    public static final int CAR_PARK_NOT_FOUND = 402;
    public static final int SERVER_ERROR = 500;
    public static final int JSON_MAL_FORMATTED = 501;
}
