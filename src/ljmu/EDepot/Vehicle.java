package ljmu.EDepot;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Vehicle implements Serializable
{
	// TODO Setup Schedule Implementation \\
	private static final long serialVersionUID = -5375860838334241152L;
	public static enum enumType { TANKER, TRUCK }
	protected String strType;
	protected String strMake;
	protected String strModel;
	protected String strRegNo;
	protected int intWeight;
	protected boolean boolAvailable;
	protected WorkSchedule schedule;
	protected ArrayList< WorkSchedule > arrSchedules = new ArrayList< WorkSchedule >();
	
	public Vehicle()
	{
		
	}
	
	public Vehicle( String strMake, String strModel, String strRegNo, int intWeight )
	{
		this.strMake = strMake;
		this.strModel = strModel;
		this.strRegNo = strRegNo;
		this.intWeight = intWeight;
	}
	
	// GETTERS & SETTERS \\
	public boolean getAvaiable()
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
	
	public WorkSchedule getSchedule()
	{
		return schedule;
	}
	
	public String getType()
	{
		return strType;
	}
	
	public void setType( String strType )
	{
		this.strType = strType;
	}
	
	public String getMake()
	{
		return strMake;
	}
	
	public void setMake( String strMake )
	{
		this.strMake = strMake;
	}
	
	public String getModel()
	{
		return strModel;
	}
	
	public void setModel( String strModel )
	{
		this.strModel = strModel;
	}
	
	public String getRegNo()
	{
		return strRegNo;
	}
	
	public void setRegNo( String strRegNo )
	{
		this.strRegNo = strRegNo;
	}
	
	public int getWeight()
	{
		return intWeight;
	}
	
	public void setWeight( int intWeight )
	{
		this.intWeight = intWeight;
	}
}
