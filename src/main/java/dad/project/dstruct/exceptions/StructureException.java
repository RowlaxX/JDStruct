package dad.project.dstruct.exceptions;

public final class StructureException extends RuntimeException {
	private static final long serialVersionUID = -8070413616412654512L;

	public StructureException() {
		super();
	}

	public StructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public StructureException(String message) {
		super(message);
	}

	public StructureException(Throwable cause) {
		super(cause);
	}
}
