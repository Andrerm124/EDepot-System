package ljmu.Exceptions;

public class NotFoundException extends Exception
{
	private static final long serialVersionUID = 3229013006253032663L;

	public NotFoundException( String strMessage )
	{
		super( strMessage );
	}
}
