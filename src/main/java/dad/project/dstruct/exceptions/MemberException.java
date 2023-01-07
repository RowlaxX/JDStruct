package dad.project.dstruct.exceptions;

public class MemberException extends RuntimeException {
	private static final long serialVersionUID = -7383252906533252668L;

	public MemberException() {
		super();
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public MemberException(String message) {
		super(message);
	}

	public MemberException(Throwable cause) {
		super(cause);
	}
}
