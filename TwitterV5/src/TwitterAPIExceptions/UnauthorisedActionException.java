package TwitterAPIExceptions;

public class UnauthorisedActionException extends TwitterApiException {

	public UnauthorisedActionException() {
		super("This action is not permitted");
	}

	private static final long serialVersionUID = -4220943746381178405L;

}
