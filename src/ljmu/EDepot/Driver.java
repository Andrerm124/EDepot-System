package ljmu.EDepot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import ljmu.Exceptions.DateException;

public class Driver implements Serializable
{
	// TODO Setup Schedule Implementation \\
	private static final long serialVersionUID = 1570190466015024988L;
	protected String strUsername;
	protected String strPassword;
	protected boolean boolAvailable;
	private WorkSchedule scheduleActive;
	protected ArrayList< WorkSchedule > arrSchedules = new ArrayList< WorkSchedule >();
	
	public Driver()
	{
		
	}
	
	public Driver( String strUsername, String strPassword )
	{
		this.strUsername = strUsername;
		this.strPassword = strPassword;
	}
	
	public boolean CheckPassword( String strPassword )
	{
		return this.strPassword.equals( strPassword );
	}
	
	public void AddSchedule( WorkSchedule schedule ) throws DateException
	{
		if( schedule.getDateStart() == null )
			throw new DateException( "### Schedule has not been assigned a date ###" );		
		
		if( arrSchedules.size() == 0 )
		{
			arrSchedules.add( schedule );
			return;
		}
		
		int index = 0;
		
		for( WorkSchedule schedLoop : arrSchedules )
		{
			Date dateStart = schedLoop.getDateStart();
			
			if( dateStart == null ||
					schedule.getDateStart().before( dateStart ) )
			{
				arrSchedules.add( index, schedule );				
				return;
			}
			
			index++;
		}
		
		arrSchedules.add( schedule );
	}
	
	// TODO REMOVE THIS \\
	public void PrintList()
	{
		for( WorkSchedule schedule : arrSchedules )
		{
			System.out.println( schedule.getDateStart() );
		}
	}
	
	public void UpdateDate()
	{
		
	}
	
	// GETTERS & SETTERS \\
	public String getUsername()
	{
		return strUsername;
	}
	
	public String getPassword()
	{
		return strPassword;
	}
	
	public boolean getAvailable()
	{
		return boolAvailable;
	}
	
	public void setAvailable( boolean boolAvailable )
	{
		this.boolAvailable = boolAvailable;
	}
	
	public void setSchedule( WorkSchedule schedule )
	{
		
	}
}
