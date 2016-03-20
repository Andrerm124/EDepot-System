package ljmu.EDepot;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import ljmu.Exceptions.DateException;
import ljmu.System.DateMgr;

public class WorkSchedule implements Serializable
{
	private static final long serialVersionUID = 9112932103181761798L;

	// TODO Implement full functionality \\
	public static enum ScheduleState
	{
		PENDING, ACTIVE, ARCHIVED
	}

	private String strClient;
	private Date dateStart;
	private Date dateEnd;
	private ScheduleState state = ScheduleState.PENDING;

	private Vehicle vehicle;
	private Driver driver;

	public WorkSchedule()
	{}

	public WorkSchedule( String strClient, Date dateStart,
			Date dateEnd )
	{
		this.strClient = strClient;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	/**
	 * Update the state of the current schedule
	 */
	public void UpdateState()
	{
		// If the state of the schedule is not valid, update it
		Date dateCur = DateMgr.getDate();

		if( this.state != ScheduleState.ACTIVE
				&& this.dateStart.before( dateCur )
				&& this.dateEnd.after( dateCur ) )
			setState( ScheduleState.ACTIVE );
		else if( this.state != ScheduleState.ARCHIVED
				&& this.dateEnd.before( dateCur ) )
			setState( ScheduleState.ARCHIVED );
		else if( this.state != ScheduleState.PENDING
				&& this.dateEnd.after( dateCur ) )
			setState( ScheduleState.PENDING );
	}

	// GETTERS & SETTERS \\
	public String getStrClient()
	{
		return this.strClient;
	}

	public void setStrClient( String strClient )
	{
		this.strClient = strClient;
	}

	public Date getDateStart()
	{
		return this.dateStart;
	}

	public void setDateStart( Date dateStart ) throws DateException
	{
		// Create a calendar to test if the start date is in the future
		Calendar c = (Calendar) DateMgr.getCalendar().clone();
		c.add( Calendar.HOUR, 47 );

		if( !dateStart.after( c.getTime() ) )
			throw new DateException(
					"### Date must be at least 48 hours in the future ###" );

		this.dateStart = dateStart;
	}

	public Date getDateEnd()
	{
		return this.dateEnd;
	}

	public void setDateEnd( Date dateEnd ) throws DateException
	{
		if( this.dateStart == null )
			throw new DateException(
					"### Start date has not been set ###" );

		if( !dateEnd.after( this.dateStart ) )
			throw new DateException(
					"### End date must be after of the Start date ###" );

		this.dateEnd = dateEnd;
	}

	public ScheduleState getState()
	{
		return this.state;
	}

	public void setState( ScheduleState state )
	{
		this.state = state;

		if( this.driver != null )
			this.driver.UpdateActiveSchedule( this );

		if( this.vehicle != null )
			this.vehicle.UpdateActiveSchedule( this );
	}

	public Vehicle getVehicle()
	{
		return this.vehicle;
	}

	public void setVehicle( Vehicle vehicle )
	{
		this.vehicle = vehicle;
	}

	public Driver getDriver()
	{
		return this.driver;
	}

	public void setDriver( Driver driver )
	{
		this.driver = driver;
	}

	public boolean isComplete()
	{
		return !this.strClient.isEmpty() && this.dateStart != null
				&& this.dateEnd != null && this.vehicle != null
				&& this.driver != null;
	}
}
