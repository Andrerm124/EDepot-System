package ljmu.EDepot;

import java.io.Serializable;
import java.util.ArrayList;

public class Driver implements Serializable
{
	// TODO Setup Schedule Implementation \\
	private static final long serialVersionUID = 1570190466015024988L;
	protected String strUsername;
	protected String strPassword;
	protected boolean boolAvailable;
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
		arrSchedules.add( schedule );
	}
}
