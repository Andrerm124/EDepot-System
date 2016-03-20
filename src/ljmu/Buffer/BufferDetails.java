package ljmu.Buffer;

import java.util.Date;

import ljmu.EDepot.Depot;
import ljmu.EDepot.Vehicle;

public class BufferDetails
{
	private Vehicle vehicle;
	private Depot depot;
	private Date date;

	public BufferDetails( Vehicle vehicle, Depot depot, Date date )
	{
		this.vehicle = vehicle;
		this.depot = depot;
		this.date = date;
	}
	
	// GETTERS & SETTERS \\
	public Vehicle getVehicle()
	{
		return this.vehicle;
	}

	public void setVehicle( Vehicle vehicle )
	{
		this.vehicle = vehicle;
	}

	public Depot getDepot()
	{
		return this.depot;
	}

	public void setDepot( Depot depot )
	{
		this.depot = depot;
	}

	public Date getDate()
	{
		return this.date;
	}

	public void setDate( Date date )
	{
		this.date = date;
	}
}
