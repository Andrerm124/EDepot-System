package ljmu.EDepot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ljmu.Exceptions.DateException;

public class WorkSchedule
{
	// TODO Implement full functionality \\
	public static enum ScheduleState {
			PENDING, ACTIVE, ARCHIVED
	};
	
	private static final DateFormat dateFormat = new SimpleDateFormat( "dd-MM-yy" );
	private String strClient;
	private Date dateStart;
	private Date dateEnd;
	private ScheduleState state = ScheduleState.PENDING;
	
	public WorkSchedule()
	{
	}
	
	public WorkSchedule( String strClient, Date dateStart, Date dateEnd )
	{
		this.strClient = strClient;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	// GETTERS & SETTERS \\
	public String getStrClient()
	{
		return strClient;
	}

	public void setStrClient( String strClient )
	{
		this.strClient = strClient;
	}

	public Date getDateStart()
	{
		return dateStart;
	}

	public void setDateStart( Date dateStart ) throws DateException
	{
		if( !dateStart.after( new Date() ) )
			throw new DateException(
					"### Date must be in the future ###" );
		
		this.dateStart = dateStart;
	}
	
	public void setDateStart( String strStart ) throws DateException, ParseException
	{
		Date dateStart = dateFormat.parse( strStart );
		
		if( !dateStart.after( new Date() ) )
			throw new DateException(
					"### Date must be in the future ###" );
		
		this.dateStart = dateStart;	
	}

	public Date getDateEnd()
	{
		return dateEnd;
	}

	public void setDateEnd( Date dateEnd ) throws DateException
	{
		if( dateStart == null )
			throw new DateException( 
					"### Start date has not been set ###" );
		
		if( !dateEnd.after( dateStart ) )
			throw new DateException(
					"### End date must be after of the Start date ###" );
		
		this.dateEnd = dateEnd;
	}
	
	public ScheduleState getState()
	{
		return state;
	}
	
	public void setState( ScheduleState state )
	{
		this.state = state;
	}
}
