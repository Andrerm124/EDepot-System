package ljmu.EDepot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ljmu.Exceptions.AuthenticationException;
import ljmu.Exceptions.ContainException;
import ljmu.Exceptions.DateException;
import ljmu.Exceptions.EscapeException;
import ljmu.Exceptions.NotFoundException;

public class Depot implements Serializable
{
	private static final long serialVersionUID = 4513537717187867873L;

	// Create maps/lists to hold object data \\
	/**
	 * HashMap of all vehicles in the depot
	 */
	private HashMap< String, Vehicle > hashVehicles = new HashMap< >();
	/**
	 * HashMap of all Drivers in the depot
	 */
	private HashMap< String, Driver > hashDrivers = new HashMap< >();
	/**
	 * Arraylist of every workschedule in the depot
	 */
	private ArrayList< WorkSchedule > arrSchedules = new ArrayList< >();

	private String strDepotID;

	// Data regarding the current session \\
	/**
	 * Checks whether the current user has <b>previously logged on</b><br>
	 * <b>Transient</b> so that the state is never saved to the sample data
	 */
	private transient boolean boolAuthenticated = false;
	/**
	 * Checks whether the user is a manager
	 */
	private transient boolean boolManager = false;
	/**
	 * Holds the current logged in driver
	 */
	private transient Driver loggedInAccount;

	public Depot( String strID )
	{
		this.strDepotID = strID;
	}

	/**
	 * Perform an authentication process to access the system
	 * 
	 * @param strUsername
	 * @param strPassword
	 * @throws NotFoundException
	 */
	public boolean LogOn( Driver driver, String strPassword )
			throws Exception
	{
		// Authenticate the user
		this.boolAuthenticated = driver.CheckPassword( strPassword );

		// If authentication failed, end system 
		if( !this.boolAuthenticated )
			throw new Exception( "!!! Incorrect Password !!!" );

		// Assign the logged in account and test whether user is a manager
		this.loggedInAccount = driver;
		this.boolManager = driver.getClass() == Manager.class;
		return true;
	}

	/**
	 * Adds a new Vehicle to the Vehicle Map with defined properties
	 * 
	 * @param type
	 * @param strMake
	 * @param strModel
	 * @param strRegNo
	 * @param intWeight
	 * @return Vehicle - New Vehicle Object
	 * @throws ContainException
	 */
	public Vehicle AddVehicle( Vehicle.enumType type, String strMake,
			String strModel, String strRegNo, int intWeight )
					throws ContainException
	{
		// If the depot contains the reg #, throw an exception
		if( this.hashVehicles.containsKey( strRegNo ) )
			throw new ContainException(
					"!!! Duplicate Registration Numbers !!!" );

		// Otherwise, create the vehicle and put it into the map
		Vehicle vehicle = type == Vehicle.enumType.TANKER
				? new Tanker( strMake, strModel, strRegNo, intWeight )
				: new Truck( strMake, strModel, strRegNo, intWeight );

		this.hashVehicles.put( strRegNo, vehicle );

		return vehicle;
	}

	/**
	 * Adds a new pre-defined vehicle to the depot's vehicle map
	 * 
	 * @param vehicle
	 * @throws ContainException
	 */
	public void AddVehicle( Vehicle vehicle ) throws ContainException
	{
		// If the depot contains the reg #, throw an exception
		if( this.hashVehicles.containsKey( vehicle.getRegNo() ) )
			throw new ContainException(
					"!!! Duplicate Registration Numbers !!!" );

		this.hashVehicles.put( vehicle.getRegNo(), vehicle );
	}

	/**
	 * Removes a vehicle from the depot based on a specified reg#
	 * 
	 * @param strRegNo
	 * @throws NotFoundException
	 */
	public void RemoveVehicle( String strRegNo )
			throws NotFoundException
	{
		// If the depot does not contain the reg #, throw an exception
		if( !this.hashVehicles.containsKey( strRegNo ) )
			throw new NotFoundException(
					"!!! No vehicle found with that Registration Number !!!" );

		// Remove the vehicle from the map
		Vehicle veh = this.hashVehicles.remove( strRegNo );

		// If the removal of the vehicle found nothing, throw an error
		if( veh == null )
			throw new NotFoundException(
					"!!! Unhandled Error removing vehicle !!!" );
	}

	/**
	 * Adds a Tanker to the Vehicle Map with defined properties
	 * 
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
		// Attempt to create a new tanker vehicle
		Tanker tanker = (Tanker) AddVehicle( Vehicle.enumType.TANKER,
				strMake, strModel, strRegNo, intWeight );

		// Apply its specialised data
		tanker.setCapacity( intLiquidCapacity );
		tanker.setLiquidType( strLiquidType );

		return tanker;
	}

	/**
	 * Adds a Truck to the Vehicle Map with defined properties
	 * 
	 * @param strMake
	 * @param strModel
	 * @param strRegNo
	 * @param intWeight
	 * @param intCargoCapacity
	 * @return Truck - New Truck Object
	 * @throws ContainException
	 */
	public Truck AddTruck( String strMake, String strModel,
			String strRegNo, int intWeight, int intCargoCapacity )
					throws ContainException
	{
		// Attempt to create a truck vehicle
		Truck truck = (Truck) AddVehicle( Vehicle.enumType.TRUCK,
				strMake, strModel, strRegNo, intWeight );

		truck.setCapacity( intCargoCapacity );

		return truck;
	}

	/**
	 * Adds a Driver to the Driver Map with defined Username/Password
	 * 
	 * @param strUsername
	 * @param strPassword
	 * @return Driver - New Driver Object
	 * @throws ContainException
	 */
	public Driver AddDriver( String strUsername, String strPassword )
			throws ContainException
	{
		// If the depot contains the username, throw an error
		if( this.hashDrivers.containsKey( strUsername ) )
			throw new ContainException(
					"!!! Duplicate Username !!!" );

		// Otherwise, create a new driver and put into the map
		Driver driver = new Driver( strUsername, strPassword );

		this.hashDrivers.put( strUsername, driver );

		return driver;
	}

	/**
	 * Remove a driver from the depot based on a specified username
	 * 
	 * @param strUsername
	 * @throws NotFoundException
	 */
	public void RemoveDriver( String strUsername )
			throws NotFoundException
	{
		// If the depot doesn't contain the username, throw an error
		if( this.hashDrivers.containsKey( strUsername ) )
			throw new NotFoundException(
					"!!! No driver found with that username !!!" );

		Driver driver = this.hashDrivers.remove( strUsername );

		if( driver == null )
			throw new NotFoundException(
					"!!! Unhandled Error removing driver !!!" );
	}

	/**
	 * Add a new manager to the depot with the specified details
	 * 
	 * @param strUsername
	 * @param strPassword
	 * @return
	 * @throws ContainException
	 */
	public Manager AddManager( String strUsername,
			String strPassword ) throws ContainException
	{
		if( this.hashDrivers.containsKey( strUsername ) )
			throw new ContainException(
					"!!! Duplicate Username !!!" );

		Manager manager = new Manager( strUsername, strPassword );

		this.hashDrivers.put( strUsername, manager );

		return manager;
	}

	/**
	 * Add a new schedule to the depot's schedule list
	 * 
	 * @param schedule
	 */
	public void AddSchedule( WorkSchedule schedule )
	{
		this.arrSchedules.add( schedule );
	}

	/**
	 * TODO SetupWorkSchedule
	 * 
	 * @return
	 * @throws AuthenticationException
	 * @throws EscapeException 
	 */
	public void SetupWorkSchedule( WorkSchedule schedule,
			Driver driver, Vehicle vehicle, boolean boolOverride )
					throws AuthenticationException, EscapeException
	{
		// If the user is not authenticated and the authentication has NOT been overriden
		// Throw an authentication error
		if( !boolOverride && !this.boolAuthenticated )
			throw new AuthenticationException(
					"!!! Not Authenticated !!!" );

		// Add the schedule to this depot
		AddSchedule( schedule );

		try
		{
			// Assign the appropriate drivers/vehicles their schedules
			driver.setSchedule( schedule );
			vehicle.setSchedule( schedule );

			schedule.setDriver( driver );
			schedule.setVehicle( vehicle );
		} catch( @SuppressWarnings( "unused" ) DateException e )
		{
			throw new EscapeException( "!!! Could not create schedule !!!" );
		}
	}

	// GETTERS & SETTERS \\
	public boolean getAuthState()
	{
		return this.boolAuthenticated;
	}

	public void setAuthState( boolean boolAuthenticated )
	{
		this.boolAuthenticated = boolAuthenticated;
	}

	public String getID()
	{
		return this.strDepotID;
	}

	public void setID( String strID )
	{
		this.strDepotID = strID;
	}

	public Vehicle getVehicle( String strRegNo )
			throws NotFoundException
	{
		Vehicle vehicle = this.hashVehicles.get( strRegNo );

		if( vehicle == null )
			throw new NotFoundException(
					"!!! Vehicle Not Found !!!" );

		return vehicle;
	}

	public Driver getDriver( String strUsername )
			throws NotFoundException
	{
		Driver driver = this.hashDrivers.get( strUsername );

		if( driver == null )
			throw new NotFoundException( "!!! Driver Not Found !!!" );

		return driver;
	}

	public Driver getLoggedOnAccount()
	{
		return this.loggedInAccount;
	}

	public HashMap< String, Vehicle > getVehicleMap()
	{
		return this.hashVehicles;
	}

	public void setVehicleMap(
			HashMap< String, Vehicle > hashVehicles )
	{
		this.hashVehicles = hashVehicles;
	}

	public HashMap< String, Driver > getDriverMap()
	{
		return this.hashDrivers;
	}

	public void setDriverMap( HashMap< String, Driver > hashDrivers )
	{
		this.hashDrivers = hashDrivers;
	}

	public ArrayList< WorkSchedule > getScheduleList()
	{
		return this.arrSchedules;
	}

	public void setScheduleList(
			ArrayList< WorkSchedule > arrSchedules )
	{
		this.arrSchedules = arrSchedules;
	}

	public Driver getAccount()
	{
		return this.loggedInAccount;
	}

	public boolean isManager()
	{
		return this.boolManager;
	}
}
