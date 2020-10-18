package projet.exception;

public class NonCompletGrapheException extends Exception {

	private String message;
	
	public NonCompletGrapheException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
