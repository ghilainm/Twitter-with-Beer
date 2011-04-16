package TwitterAPIExceptions;

public class ThisLineAlreadyExistException extends TwitterApiException {

	public ThisLineAlreadyExistException() {
		super("This line already exist");
	}

	private static final long serialVersionUID = 1517948277880090635L;

}
