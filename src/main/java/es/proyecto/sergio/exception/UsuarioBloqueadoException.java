package es.proyecto.sergio.exception;

public class UsuarioBloqueadoException extends Exception{

    public UsuarioBloqueadoException(String errorMessage) {
        super(errorMessage);
    }

}
