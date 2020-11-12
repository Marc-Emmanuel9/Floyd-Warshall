package projet.exception;

public class EmptyFileException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String message;
	
	public EmptyFileException(final String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
