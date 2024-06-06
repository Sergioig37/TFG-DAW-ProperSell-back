package com.example.start.exception;

public class PasswordNoExisteException extends Exception{

    public PasswordNoExisteException(String errorMessage) {
        super(errorMessage);
    }

}
