package es.proyecto.sergio.exception;

public class CorreoNoEncontradoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CorreoNoEncontradoException(String errorMessage) {
        super(errorMessage);
    }
}
