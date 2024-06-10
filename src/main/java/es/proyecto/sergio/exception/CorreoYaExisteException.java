package es.proyecto.sergio.exception;

public class CorreoYaExisteException extends Exception{

    public CorreoYaExisteException(String errorMessage) {
        super(errorMessage);
    }

}
