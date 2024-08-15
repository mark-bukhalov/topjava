package ru.javawebinar.topjava.util.exception;

public class CustomDatabaseException extends RuntimeException{
    public CustomDatabaseException(String message) {
        super(message);
    }
}
