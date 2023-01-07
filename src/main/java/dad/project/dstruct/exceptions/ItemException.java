package dad.project.dstruct.exceptions;

public final class ItemException extends RuntimeException {
	private static final long serialVersionUID = 4763131641493429033L;

	public ItemException() {
		super();
	}

	public ItemException(String message, Throwable cause) {
		super(message, cause);
	}

	public ItemException(String message) {
		super(message);
	}

	public ItemException(Throwable cause) {
		super(cause);
	}
}
