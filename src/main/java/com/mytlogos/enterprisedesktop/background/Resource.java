package com.mytlogos.enterprisedesktop.background;




public class Resource<T> {

    private final Status status;

    private final T data;

    private final String message;

    private Resource( Status status,  T data,
                      String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success( T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg,  T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading( T data) {
        return new Resource<>(Status.LOADING, data, null);
    }


    public String getMessage() {
        return message;
    }


    public T getData() {
        return data;
    }


    public Status getStatus() {
        return status;
    }

    public enum Status {SUCCESS, ERROR, LOADING}
}
