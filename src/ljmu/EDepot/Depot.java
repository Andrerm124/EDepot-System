package ljmu.EDepot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ljmu.Exceptions.AuthenticationException;
import ljmu.Exceptions.ContainException;
import ljmu.Exceptions.NotFoundException;

public class Depot implements Serializable
{
	// TODO Annotate Depot Code \\
	// TODO Implement full functionality \\
	private static final long serialVersionUID = 4513537717187867873L;
	
	private HashMap< String, Vehicle > hashVehicles = new HashMap< String, Vehicle >();
	private HashMap< String, Driver > hashDrivers = new HashMap< String, Driver >();
	private ArrayList< WorkSchedule > arrSchedules = new ArrayList< WorkSchedule >();
	
	private String strDepotID;
	
	/**
	 * Checks whether the current user has <b>previously logged on</b><br>
	 * <b>Transient</b> so that the state is never saved to the sample data
	 */
	private transient boolean boolAuthenticated = false;
	private transient boolean boolManager = false;
	private transient Driver loggedInAccount;

	public Depot( String strID )
	{
		this.strDepotID = strID;
	}

	/**
	 * Perform an authentication process to access the system
	 * @param strUsername
	 * @param strPassword
	 * @throws NotFoundException 
	 */
	public boolean LogOn( Driver driver, String strPassword ) throws Exception
	{
		boolAuthenticated = driver.CheckPassword( strPassword );

		if( !boolAuthenticated )
			throw new Exception( "### Incorrect Password ###" );
		
		if( boolAuthenticated )
		{
			loggedInAccount = driver;
			
			boolManager = driver.getClass() == Manager.class;
		}
		
		return this.boolAuthenticated;
	}

	/**
	 * Adds a new Vehicle to the Vehicle Map with defined properties
	 * @param type
	 * @param strMake
	 * @param strModel
	 * @param strRegNo
	 * @param intWeight
	 * @return Vehicle - New Vehicle Object
	 * @throws ContainException 
	 */
	public Vehicle AddVehicle( Vehicle.enumType type, String strMake, String strModel,
			String strRegNo, int intWeight ) throws ContainException
	{
		if( hashVehicles.containsKey( strRegNo ) )
			throw new ContainException( "### Duplicate Registration Numbers ###" );
		
		Vehicle vehicle = 
				type == Vehicle.enumType.TANKER ? 
						new Tanker( strMake, strModel, strRegNo, intWeight ) :
						new Truck( strMake, strModel, strRegNo, intWeight );
		
		hashVehicles.put( strRegNo, vehicle );

		return vehicle;
	}

	/**
	 * Adds a Tanker to the Vehicle Map with defined properties
	 * @param strMake
	 * @param strModel
	 * @param strRegNo
	 * @param intWeight
	 * @param intLiquidCapacity
	 * @param strLiquidType
	 * @return Tanker - New Tanker Object
	 * @throws ContainException 
	 */
	public Tanker AddTanker( String strMake, String strModel,
			String strRegNo, int intWeight, int intLiquidCapacity,
			String strLiquidType ) throws ContainException
	{
		Tanker tanker = (Tanker) AddVehicle( Vehicle.enumType.TANKER, strMake, strModel, strRegNo,
				intWeight );

		tanker.setCapacity( intLiquidCapacity );
		tanker.setLiquidType( strLiquidType );

		return tanker;
	}

	/**
	 * Adds a Truck to the Vehicle Map with defined properties
	 * @param strMake
	 * @param strModel
	 * @param strRegNo
	 * @param intWeight
	 * @param intCargoCapacity
	 * @return Truck - New Truck Object
	 * @throws ContainException 
	 */
	public Truck AddTruck( String strMake, String strModel,
			String strRegNo, int intWeight, int intCargoCapacity) throws ContainException
	{
		Truck truck = (Truck) AddVehicle( Vehicle.enumType.TRUCK, strMake, strModel, strRegNo,
				intWeight );

		truck.setCapacity( intCargoCapacity );

		return truck;
	}

	/**
	 * Adds a Driver to the Driver Map with defined Username/Password
	 * @param strUsername
	 * @param strPassword
	 * @return Driver - New Driver Object
	 * @throws ContainException 
	 */
	public Driver AddDriver( String strUsername, String strPassword ) throws ContainException
	{
		if( hashDrivers.containsKey( strUsername ) )
			throw new ContainException( "### Duplicate Username ###" );

		Driver driver = new Driver( strUsername, strPassword );

		hashDrivers.put( strUsername, driver );

		return driver;
	}
	
	public Manager AddManager( String strUsername, String strPassword ) throws ContainException
	{
		if( hashDrivers.containsKey( strUsername ) )
			throw new ContainException( "### Duplicate Username ###" );

		Manager manager = new Manager( strUsername, strPassword );

		hashDrivers.put( strUsername, manager );
		
		return manager;
	}
	
	public void AddSchedule( WorkSchedule schedule )
	{
		arrSchedules.add( schedule );
	}
	
	/**
	 * TODO SetupWorkSchedule
	 * @return
	 * @throws AuthenticationException 
	 */
	public void SetupWorkSchedule( WorkSchedule schedule, Driver driver, Vehicle vehicle ) throws AuthenticationException
	{
		//if( !boolAuthenticated )
			//throw new AuthenticationException( "### Not Authenticated ###" );
		
		driver.setSchedule( schedule );
		vehicle.setSchedule( schedule );
	}

	// GETTERS & SETTERS \\	
	public boolean getAuthState()
	{
		return boolAuthenticated;
	}
	
	public void setAuthState( boolean boolAuthenticated )
	{
		this.boolAuthenticated = boolAuthenticated;
	}
	
	public String getID()
	{
		return strDepotID;
	}
	
	public void setID( String strID )
	{
		this.strDepotID = strID;
	}	

	public Vehicle getVehicle( String strRegNo ) throws NotFoundException
	{
		Vehicle vehicle = hashVehicles.get( strRegNo );
		
		if( vehicle == null )
			throw new NotFoundException( "### Vehicle Not Found ###" );
		
		return vehicle;
	}
	
	public Driver getDriver( String strUsername ) throws NotFoundException
	{
		Driver driver = hashDrivers.get( strUsername );

		if( driver == null )
			throw new NotFoundException( "### Driver Not Found ###" );
				
		return driver;
	}
	
	public HashMap< String, Vehicle > getVehicleMap()
	{
		return hashVehicles;
	}
	
	public void setVehicleMap( HashMap< String, Vehicle > hashVehicles )
	{
		this.hashVehicles = hashVehicles;
	}
	
	public HashMap< String, Driver > getDriverMap()
	{
		return hashDrivers;
	}
	
	public void setDriverMap( HashMap< String, Driver > hashDrivers )
	{
		this.hashDrivers = hashDrivers;
	}
	
	public ArrayList< WorkSchedule > getScheduleList()
	{
		return arrSchedules;
	}
	
	public void setScheduleList( ArrayList< WorkSchedule > arrSchedules )
	{
		this.arrSchedules = arrSchedules;
	}
	
	public Driver getAccount()
	{
		return loggedInAccount;
	}
	
	public boolean isManager()
	{
		return boolManager;
	}
}
