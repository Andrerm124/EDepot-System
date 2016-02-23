package ljmu.EDepot;

import java.util.Date;

public class WorkSchedule
{
	// TODO Implement full functionality \\
	public static enum ScheduleState {
			PENDING, ACTIVE, ARCHIVED
	};
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

	public void setDateStart( Date dateStart )
	{
		this.dateStart = dateStart;
	}

	public Date getDateEnd()
	{
		return dateEnd;
	}

	public void setDateEnd( Date dateEnd )
	{
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
