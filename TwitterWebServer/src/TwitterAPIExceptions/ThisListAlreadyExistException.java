package TwitterAPIExceptions;

public class ThisListAlreadyExistException extends TwitterApiException {

	public ThisListAlreadyExistException() {
		super("This list alread exists");
	}

	private static final long serialVersionUID = 5887786332256621085L;

}
