package com.example.start.exception;

public class UsuarioNoExisteException extends Exception{

    public UsuarioNoExisteException(String errorMessage) {
        super(errorMessage);
    }

}
