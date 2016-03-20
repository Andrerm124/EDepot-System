package ljmu.EDepot;

public class Truck extends Vehicle
{
	private static final long serialVersionUID = -3022492451547112347L;
	private int intCargoCapacity;

	public Truck()
	{
		super();
	}

	public Truck( String strMake, String strModel, String strRegNo,
			int intWeight )
	{
		super( strMake, strModel, strRegNo, intWeight );
		this.strType = "Truck";
	}

	// GETTERS & SETTERS \\
	public void setCapacity( int intCargoCapacity )
	{
		this.intCargoCapacity = intCargoCapacity;
	}

	public int getCapacity()
	{
		return this.intCargoCapacity;
	}
}
