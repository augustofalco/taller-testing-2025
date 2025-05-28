package progAvan.shared;

import progAvan.shared.error.BaseError;

public class Result<T> {
    private final T data;
    private final String message;
    private final Status status;
    private final BaseError error;

    public enum Status {
        SUCCESS,
        FAILURE
    }

    private Result(T data, String message, Status status, BaseError error) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.error = error;
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(data, message, Status.SUCCESS, null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(null, message, Status.FAILURE, null);
    }

    public static <T> Result<T> failure(String message, T data) {
        return new Result<>(data, message, Status.FAILURE, null);
    }

    public static <T> Result<T> failure(BaseError error) {
        return new Result<>(null, error.getMessage(), Status.FAILURE, error);
    }

    public static <T> Result<T> failure(BaseError error, T data) {
        return new Result<>(data, error.getMessage(), Status.FAILURE, error);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isFailure() {
        return status == Status.FAILURE;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public BaseError getError() {
        return error;
    }

    public String getErrorCode() {
        return error != null ? error.getCode() : null;
    }

    public Status getStatus() {
        return status;
    }
}
