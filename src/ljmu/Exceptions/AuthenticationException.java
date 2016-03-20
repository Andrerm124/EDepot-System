package ljmu.Exceptions;

public class AuthenticationException extends Exception
{
	private static final long serialVersionUID = -4164896656960295456L;

	public AuthenticationException( String strMessage )
	{
		super( strMessage );
	}
}
