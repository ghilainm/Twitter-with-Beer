package TwitterAPIExceptions;

public class NotLoggedException extends TwitterApiException{
	
	private static final long serialVersionUID = 1781356328068437892L;

	public NotLoggedException(){
		super("Please log in to execute this action");
	}
}
