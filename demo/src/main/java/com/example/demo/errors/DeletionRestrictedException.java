package com.example.demo.errors;

/** Se lanza cuando una relación existente impide borrar un registro. */
public class DeletionRestrictedException extends RuntimeException {

    public DeletionRestrictedException(String message, Throwable cause) {
        super(message, cause);
    }
}
