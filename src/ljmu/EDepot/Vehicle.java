package ljmu.EDepot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import ljmu.EDepot.WorkSchedule.ScheduleState;
import ljmu.Exceptions.DateException;

public abstract class Vehicle implements Serializable, ScheduleHelper
{
	private static final long serialVersionUID = -5375860838334241152L;

	public static enum enumType
	{
		TANKER, TRUCK
	}

	protected String strType;
	protected String strMake;
	protected String strModel;
	protected String strRegNo;
	protected int intWeight;
	protected WorkSchedule scheduleActive;
	protected ArrayList< WorkSchedule > arrSchedules = new ArrayList< >();

	public Vehicle()
	{

	}

	public Vehicle( String strMake, String strModel, String strRegNo,
			int intWeight )
	{
		this.strMake = strMake;
		this.strModel = strModel;
		this.strRegNo = strRegNo;
		this.intWeight = intWeight;
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
	public boolean getAvailable()
	{
		if( this.scheduleActive != null )
			return false;

		for( WorkSchedule schedule : this.arrSchedules )
		{
			if( schedule.getState() == ScheduleState.PENDING )
				return false;
		}

		return true;
	}

	public void setSchedule( WorkSchedule schedule ) throws DateException
	{
		AddSchedule( schedule );
		schedule.setVehicle( this );
	}

	public WorkSchedule getSchedule()
	{
		return this.scheduleActive;
	}

	public ArrayList< WorkSchedule > getScheduleList()
	{
		return this.arrSchedules;
	}

	public String getType()
	{
		return this.strType;
	}

	public void setType( String strType )
	{
		this.strType = strType;
	}

	public String getMake()
	{
		return this.strMake;
	}

	public void setMake( String strMake )
	{
		this.strMake = strMake;
	}

	public String getModel()
	{
		return this.strModel;
	}

	public void setModel( String strModel )
	{
		this.strModel = strModel;
	}

	public String getRegNo()
	{
		return this.strRegNo;
	}

	public void setRegNo( String strRegNo )
	{
		this.strRegNo = strRegNo;
	}

	public int getWeight()
	{
		return this.intWeight;
	}

	public void setWeight( int intWeight )
	{
		this.intWeight = intWeight;
	}
}
