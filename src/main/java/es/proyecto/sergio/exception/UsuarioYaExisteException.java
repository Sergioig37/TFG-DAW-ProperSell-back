package es.proyecto.sergio.exception;

public class UsuarioYaExisteException extends Exception{

    public UsuarioYaExisteException(String errorMessage) {
        super(errorMessage);
    }

}
