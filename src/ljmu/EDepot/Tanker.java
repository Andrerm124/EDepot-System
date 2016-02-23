package ljmu.EDepot;

public class Tanker extends Vehicle
{
	// TODO Annotate Tanker \\
	// TODO Check for cleanups \\
	private static final long serialVersionUID = -5256688007114050911L;
	private int intLiquidCapacity = -1;
	private String strLiquidType = "Unspecified";
	
	public Tanker()
	{
		super();
	}
	
	public Tanker( String strMake, String strModel, String strRegNo, int intWeight )
	{
		super( strMake, strModel, strRegNo, intWeight );
		this.strType = "Tanker";
	}
	
	// GETTERS & SETTERS \\
	public void setCapacity( int intLiquidCapacity )
	{
		this.intLiquidCapacity = intLiquidCapacity;
	}
	
	public int getCapacity()
	{
		return intLiquidCapacity;
	}
	
	public void setLiquidType( String strLiquidType )
	{
		this.strLiquidType = strLiquidType; 
	}
	
	public String getLiquidType()
	{
		return strLiquidType;
	}
}
