package ljmu.EDepot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import ljmu.EDepot.WorkSchedule.ScheduleState;
import ljmu.Exceptions.DateException;

public class Driver implements Serializable, ScheduleHelper
{
	private static final long serialVersionUID = 1570190466015024988L;
	protected String strUsername;
	protected String strPassword;
	protected boolean boolAvailable;
	private WorkSchedule scheduleActive;
	protected ArrayList< WorkSchedule > arrSchedules = new ArrayList< >();

	public Driver()
	{}

	public Driver( String strUsername, String strPassword )
	{
		this.strUsername = strUsername;
		this.strPassword = strPassword;
	}

	@SuppressWarnings( "hiding" )
	public boolean CheckPassword( String strPassword )
	{
		return this.strPassword.equals( strPassword );
	}

	public void AddSchedule( WorkSchedule schedule )
			throws DateException
	{
		if( schedule.getDateStart() == null )
			throw new DateException(
					"### Schedule has not been assigned a date ###" );

		if( this.arrSchedules.size() == 0 )
		{
			this.arrSchedules.add( schedule );
			return;
		}

		int index = 0;

		for( WorkSchedule schedLoop : this.arrSchedules )
		{
			Date dateStart = schedLoop.getDateStart();
			Date dateEnd = schedLoop.getDateEnd();

			if( dateStart == null
					|| ( schedule.getDateStart().before( dateStart ) && schedule.getDateEnd().before( dateEnd ) ) )
			{
				this.arrSchedules.add( index, schedule );
				return;
			}

			index++;
		}

		this.arrSchedules.add( schedule );
	}

	@Override
	public void UpdateActiveSchedule( WorkSchedule schedule )
	{
		if( schedule == this.scheduleActive )
		{
			if( schedule.getState() != ScheduleState.ACTIVE )
				this.scheduleActive = null;
		}
		else if( schedule.getState() == ScheduleState.ACTIVE )
			this.scheduleActive = schedule;
	}

	/**
	 * Scans the vehicles archived/pending schedules for an overlapping date
	 * 
	 * @param date
	 * @return <b>TRUE when <b>Overlapping</b>
	 */
	@Override
	public boolean ScanForOverlap( Date date )
	{
		for( WorkSchedule schedule : this.arrSchedules )
		{
			if( date.after( schedule.getDateStart() )
					&& date.before( schedule.getDateEnd() ) )
				return true;
		}

		return false;
	}

	// GETTERS & SETTERS \\
	public String getUsername()
	{
		return this.strUsername;
	}

	public String getPassword()
	{
		return this.strPassword;
	}

	public boolean getAvailable()
	{
		return this.boolAvailable;
	}

	public void setAvailable( boolean boolAvailable )
	{
		this.boolAvailable = boolAvailable;
	}

	public void setSchedule( WorkSchedule schedule ) throws DateException
	{
		AddSchedule( schedule );
		schedule.setDriver( this );
	}

	public ArrayList< WorkSchedule > getScheduleList()
	{
		return this.arrSchedules;
	}
}
