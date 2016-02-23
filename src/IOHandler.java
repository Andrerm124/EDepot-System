import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import ljmu.EDepot.Depot;
import ljmu.EDepot.Driver;
import ljmu.EDepot.Vehicle;
import ljmu.EDepot.WorkSchedule;
import ljmu.Exceptions.AuthenticationException;
import ljmu.Exceptions.DateException;
import ljmu.Exceptions.EscapeException;
import ljmu.Exceptions.NotFoundException;

public class IOHandler
{
	// TODO Annotate IOHandler Code \\
	// TODO Check for IOHandle Cleanups \\
	private static ArrayList< Depot >	arrDepots	= new ArrayList< Depot >();
	private static boolean				boolAlive	= false;
	private static Scanner				scanner;

	/**
	 * Main method of system
	 * 
	 * @param obj
	 */
	public static void main( String[] obj )
	{
		DataManager.HandleSampleData();

		InitialiseSystem();

		Depot depot = null;

		try
		{
			depot = PerformLoginProcess();
			BeginSystemLoop( depot );
		} catch( EscapeException | AuthenticationException e )
		{
			System.out.println( e.getMessage() );
			return;
		}		 
	}

	/**
	 * Initialise the System Resources
	 */
	private static void InitialiseSystem()
	{
		scanner = new Scanner( System.in );
	}

	/**
	 * Begin the login process
	 * 
	 * @return Depot - The depot the user has logged into
	 */
	private static Depot PerformLoginProcess() throws EscapeException
	{
		System.out.println( "### Welcome to the Depot System ###\n"
				+ "Please pick a Depot to log into:" );

		ShouldDisplayDepotList();

		Depot depotSelected = RequestDepotInput();

		System.out.println( "Selected: " + depotSelected.getID() );

		Driver driver = ValidateUsername( depotSelected );

		if ( !ValidatePassword( driver, depotSelected ) )
			return null;

		return depotSelected;
	}

	/**
	 * Test whether the system should display a list of Depot ID's
	 */
	private static void ShouldDisplayDepotList()
	{
		System.out.println( "To display a list of Depot ID's, enter Y\n"
				+ "Otherwise, press the Enter key" );

		String strInput = scanner.nextLine().toLowerCase();

		if ( strInput.equals( "y" ) )
			PrintDepotIDList();
		else
			return;
	}

	/**
	 * Request the user to enter a depot ID
	 * 
	 * @param boolFailed
	 * @return Depot - Depot associated with input ID
	 * @throws Exception
	 */
	private static Depot RequestDepotInput() throws EscapeException
	{
		while( true )
		{
			try
			{
				System.out.println( "Please enter a Depot ID" );
				String strInput = scanner.nextLine().toLowerCase();
				return getDepot( strInput );
			} catch( NotFoundException e )
			{
				if ( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException( "### Ending Process ###" );
			}
		}
	}

	/**
	 * Request a username and validate it exists in the depot
	 * 
	 * @param depot
	 * @return Driver - Return the Driver object associated with the username
	 * @throws Exception
	 */
	private static Driver ValidateUsername( Depot depot )
			throws EscapeException
	{
		while( true )
		{
			try
			{
				System.out
						.println( "Please enter your Depot Username (Case Sensitive)" );
				String strInput = scanner.nextLine();
				return depot.getDriver( strInput );
			} catch( NotFoundException e )
			{
				if ( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException( "### Ending Process ###" );
			}
		}
	}

	/**
	 * Request a password and authenticate final credentials
	 * 
	 * @param strUsername
	 * @param depot
	 * @param intErrorCount
	 * @return Boolean - True on successful authentication
	 * @throws Exception
	 */
	private static boolean ValidatePassword( Driver driver, Depot depot )
			throws EscapeException
	{
		int intErrorCount = 0;

		while( true )
		{
			System.out
					.println( "Please enter your Depot Password (Case Sensitive)" );

			String strInput = scanner.nextLine();

			try
			{
				return depot.LogOn( driver, strInput );
			} catch( Exception e )
			{
				intErrorCount++;

				if ( intErrorCount > 2 )
					throw new EscapeException( "### Login Count Exceeded ###" );

				if ( !ShouldRetry( e.getMessage() + " - Attempt "
						+ intErrorCount + " of 3" ) )
					throw new EscapeException( "### Ending Process ###" );
			}
		}
	}

	/**
	 * The main function of the system<br>
	 * Contains a loop with multiple different command chains TODO Implement
	 * full functionality TODO Implement boolAlive in loop
	 * 
	 * @param depot
	 * @throws AuthenticationException
	 */
	private static void BeginSystemLoop( Depot depot )
			throws AuthenticationException
	{
		do
		{
			if ( !depot.getAuthState() )
				throw new AuthenticationException( "### Not Authenticated ###" );

			DisplayMainMenu( depot );
			String strInput = scanner.nextLine().toLowerCase();

			switch( strInput )
			{
			case "setup":
				try
				{
					SetupWorkSchedule( depot );
				} catch( EscapeException e )
				{
				}
				break;
			case "move":
				try
				{
					MoveVehicle( depot );
				} catch( EscapeException e )
				{
				}
				break;
			}
		} while( boolAlive );

		System.out.println( "### ESCAPED LOOP ###" );
	}

	/**
	 * Function to efficiently build the main menu<br>
	 * Menu depends on account type (Driver/Manager)
	 * 
	 * @param depot
	 */
	private static void DisplayMainMenu( Depot depot )
	{
		String strMenuText = "### MAIN MENU "
				+ ( depot.isManager() ? " - MANAGER MODE" : "" ) + "###\n"
				+ "Please enter the keywords to select an action\n\n"
				+ "View - View Workschedule";

		if ( depot.isManager() )
			strMenuText += "\nSetup - Setup a new schedule"
					+ "\nMove - Move a vehicle to another depot";

		System.out.println( strMenuText );
	}

	private static void SetupWorkSchedule( Depot depot ) throws EscapeException
	{
		WorkSchedule schedule = RequestScheduleInput( depot );
		Vehicle vehicle = RequestVehicleInput( depot );
		Driver driver = RequestDriverInput( depot );

		vehicle.setSchedule( schedule );
		driver.setSchedule( schedule );

		System.out.println( "Setup new WorkSchedule" );
	}

	private static WorkSchedule RequestScheduleInput( Depot depot )
			throws EscapeException
	{
		WorkSchedule schedule = new WorkSchedule();

		String strInput;

		System.out.println( "Creating New Schedule" );

		while( true )
		{
			System.out.println( "Please enter a Client Name" );

			strInput = scanner.nextLine();

			/*
			 * if( depot.ScheduleMapContains( strInput ) ) { if( !ShouldRetry(
			 * "Schedule List already contains specified client name" ) ) throw
			 * new EscapeException( "### Returning to Main Menu ###" ); else
			 * continue; }
			 */

			schedule.setStrClient( strInput );
			break;
		}

		while( true )
		{
			System.out.println( "Please enter a start date (dd-mm-yy)" );
			strInput = scanner.nextLine();

			try
			{
				schedule.setDateStart( strInput );
				break;
			} catch( DateException | ParseException e )
			{
				if ( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException( "### Returning to Main Menu ###" );
			}
		}

		while( true )
		{
			System.out.println( "Please enter an end date (dd-mm-yy)" );

			strInput = scanner.nextLine();

			try
			{
				schedule.setDateStart( strInput );
				break;
			} catch( DateException | ParseException e )
			{
				if ( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException( "### Returning to Main Menu ###" );
			}
		}

		System.out.println( "Schedule Created Successfully" );
		return schedule;
	}

	private static Vehicle RequestVehicleInput( Depot depot )
			throws EscapeException
	{
		String strInput;

		while( true )
		{
			System.out.println( "Please enter a Vehicle ID" );
			strInput = scanner.nextLine();

			try
			{
				return depot.getVehicle( strInput );
			} catch( NotFoundException e )
			{
				System.out.println( e.getMessage()
						+ "\nEnter V to View the Vehicle list and try again"
						+ "\nEnter Y to try again"
						+ "\nPress Enter to return to main menu" );

				String strReply = scanner.nextLine().toLowerCase();

				if ( strReply.equals( "v" ) )
					PrintVehicleIDList( depot );
				else if ( strReply.equals( "y" ) )
					continue;
				else
					throw new EscapeException( "### Returning to Main Menu ###" );
			}
		}
	}

	private static Driver RequestDriverInput( Depot depot )
			throws EscapeException
	{
		String strInput;
		while( true )
		{
			System.out.println( "Please enter a Driver Username" );

			strInput = scanner.nextLine();

			try
			{
				return depot.getDriver( strInput );
			} catch( NotFoundException e )
			{
				System.out.println( e.getMessage()
						+ "\nEnter V to View the Driver list and try again"
						+ "\nEnter Y to try again"
						+ "\nPress Enter to return to main menu" );

				String strReply = scanner.nextLine().toLowerCase();

				if ( strReply.equals( "v" ) )
					PrintDriverIDList( depot );
				else if ( strReply.equals( "y" ) )
					continue;
				else
					throw new EscapeException( "### Returning to Main Menu ###" );
			}
		}
	}

	private static void MoveVehicle( Depot depot ) throws EscapeException
	{
		Vehicle vehicle = RequestVehicleInput( depot );
		Date moveDate = RequestDateInput( depot, vehicle );
	}

	private static Date RequestDateInput( Depot depot, Vehicle vehicle )
			throws EscapeException
	{
		DateFormat format = new SimpleDateFormat( "dd-MM-yy" );

		while( true )
		{
			System.out.println( "Please enter a start date (dd-mm-yy)" );
			String strInput = scanner.nextLine();

			try
			{
				Date date = format.parse( strInput );

				if ( !date.after( new Date() ) )
					throw new DateException(
						"### Date must be in the future ###" );

				WorkSchedule schedule = vehicle.getSchedule();

				boolean boolOverlap = date.after( schedule.getDateStart() )
						&& date.before( schedule.getDateEnd() );

				if ( boolOverlap )
					throw new DateException( " " );

				return date;
			} catch( ParseException e )
			{
				if ( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException( "### Returning to Main Menu ###" );
			} catch( DateException e )
			{
				if ( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException( "### Returning to Main Menu ###" );
			}
		}
	}

	/**
	 * Prints a list of Depot ID's
	 */
	private static void PrintDepotIDList()
	{
		for( Depot depot : arrDepots )
			System.out.println( depot.getID() );
	}

	private static void PrintVehicleIDList( Depot depot )
	{
		for( String strID : depot.getVehicleMap().keySet() )
			System.out.print( strID );
	}

	private static void PrintDriverIDList( Depot depot )
	{
		for( String strID : depot.getDriverMap().keySet() )
			System.out.print( strID );
	}

	private static boolean ShouldRetry( String strOutput )
	{
		System.out.println( strOutput
				+ "\n\nEnter Y to retry, otherwise press Enter" );

		String strInput = scanner.nextLine().toLowerCase();

		if ( strInput.equals( "y" ) )
			return true;

		return false;
	}

	/**
	 * Finds the Depot with the associated ID
	 * 
	 * @param strID
	 * @return Depot
	 */
	private static Depot getDepot( String strID ) throws NotFoundException
	{
		for( Depot depot : arrDepots )
		{
			if ( depot.getID().equalsIgnoreCase( strID ) )
				return depot;
		}

		throw new NotFoundException( "No Depot found with the specified ID"
				+ "\nPlease try again, or press Enter to end the program" );
	}

	// GETTERS & SETTERS \\
	public static ArrayList< Depot > getDepotList()
	{
		return arrDepots;
	}

	public static void setDepotList( ArrayList< Depot > arrUpdate )
	{
		arrDepots = arrUpdate;
	}
}
