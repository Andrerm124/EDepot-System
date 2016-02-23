package ljmu.Exceptions;

public class EscapeException extends Exception
{
	private static final long serialVersionUID = 8371820062285105940L;

	/**
	 * Used to escape a method effectively
	 * @param strMessage
	 */
	public EscapeException( String strMessage )
	{
		super( strMessage );
	}
}
