package com.example.start.exception;

public class UsuarioYaExisteException extends Exception{

    public UsuarioYaExisteException(String errorMessage) {
        super(errorMessage);
    }

}
