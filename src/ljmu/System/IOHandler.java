package ljmu.System;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import ljmu.Buffer.MoveBuffer;
import ljmu.EDepot.Depot;
import ljmu.EDepot.Driver;
import ljmu.EDepot.Vehicle;
import ljmu.EDepot.WorkSchedule;
import ljmu.Exceptions.AuthenticationException;
import ljmu.Exceptions.CloseException;
import ljmu.Exceptions.DateException;
import ljmu.Exceptions.EscapeException;
import ljmu.Exceptions.NotFoundException;

public class IOHandler
{
	// TODO Annotate IOHandler Code \\
	private static ArrayList< Depot > arrDepots = new ArrayList< >();
	private static boolean boolAlive = true;
	private static Scanner scanner;

	/**
	 * Main method of system
	 * 
	 * @param obj
	 */
	public static void main( String[] obj )
	{
		InitialiseSystem();

		DataManager.HandleSampleData();
		UpdateScheduleStates();

		Depot depot = null;

		try
		{
			depot = PerformLoginProcess();
			BeginSystemLoop( depot );
		} catch( AuthenticationException | CloseException | EscapeException e )
		{
			System.out.println( e.getMessage() );
		}

		DateMgr.setAlive( false );
	}

	/**
	 * Initialise the System Resources
	 */
	@SuppressWarnings( "unused" )
	private static void InitialiseSystem()
	{
		scanner = new Scanner( System.in );
		new DateMgr();
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

		if( !ValidatePassword( driver, depotSelected ) )
			return null;

		return depotSelected;
	}

	/**
	 * Test whether the system should display a list of Depot ID's
	 */
	private static void ShouldDisplayDepotList()
	{
		System.out.println(
				"\tTo display a list of Depot ID's, enter Y\n\t"
						+ "Otherwise, press the Enter key" );

		String strInput = scanner.nextLine().toLowerCase();

		if( strInput.equals( "y" ) )
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
				return FindDepot( strInput );
			} catch( NotFoundException e )
			{
				if( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException(
							"### Ending Process ###" );
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
				System.out.println(
						"Please enter your Depot Username (Case Sensitive)" );
				String strInput = scanner.nextLine();
				return depot.getDriver( strInput );
			} catch( NotFoundException e )
			{
				if( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException(
							"### Ending Process ###" );
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
	private static boolean ValidatePassword( Driver driver,
			Depot depot ) throws EscapeException
	{
		int intErrorCount = 0;

		while( true )
		{
			System.out.println(
					"Please enter your Depot Password (Case Sensitive)" );

			String strInput = scanner.nextLine();

			try
			{
				return depot.LogOn( driver, strInput );
			} catch( Exception e )
			{
				intErrorCount++;

				if( intErrorCount > 2 )
					throw new EscapeException(
							"!!! Login Count Exceeded !!!" );

				if( !ShouldRetry( e.getMessage() + " - Attempt "
						+ intErrorCount + " of 3" ) )
					throw new EscapeException(
							"### Ending Process ###" );
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
	 * @throws CloseException 
	 * @throws EscapeException
	 */
	private static void BeginSystemLoop( Depot depot )
			throws AuthenticationException, CloseException
	{
		if( !depot.getAuthState() )
			throw new AuthenticationException(
					"!!! Not Authenticated !!!" );

		do
		{
			DisplayMainMenu( depot );
			String strInput = scanner.nextLine().toLowerCase();

			try
			{
				switch( strInput )
				{
				case "view":
					PrintSchedules( depot.getLoggedOnAccount()
							.getScheduleList() );
					break;
				case "date":
					SetSystemDate( depot );
					break;
				case "show":
					System.out.println( "\t" + DateMgr.getDate() );
					break;
				case "debug":
					System.out.println(
							"Count: " + depot.getScheduleList().size() );
					PrintSchedules( depot.getScheduleList() );
					break;
				case "toggle":
					DateMgr.ToggleState();
					break;
				case "setup":
					if( !depot.isManager() )
					{
						System.out.println(
								"!!! Requires Managerial Status !!!" );
						break;
					}
	
					SetupWorkSchedule( depot );
					break;
				case "move":
					if( !depot.isManager() )
					{
						System.out.println(
								"!!! Requires Managerial Status !!!" );
						break;
					}
	
					MoveVehicle( depot );
					break;
				case "vehicles":
					if( !depot.isManager() )
					{
						System.out.println(
								"!!! Requires Managerial Status !!!" );
						break;
					}
					
					PrintVehicleIDList( RequestDepotInput() );
					break;
				case "drivers":
					if( !depot.isManager() )
					{
						System.out.println(
								"!!! Requires Managerial Status !!!" );
						break;
					}
					
					PrintDriverIDList( RequestDepotInput() );
					break;
				case "exit":
					throw new CloseException( "### Closing System ###" );
				default:
					System.out.println( "!!! Unfound command !!!" );
				}
			}
			catch( EscapeException e )
			{
				System.out.println( e.getMessage() );
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
				+ ( depot.isManager() ? " - MANAGER MODE" : "" )
				+ "###\n"
				+ "Please enter the keywords to select an action"
				+ "\n\tExit - Exit the system"
				+ "\n\tView - View Workschedule"
				+ "\n\tDate - Set the system date"
				+ "\n\tShow - Show the system date"
				+ "\n\tDebug - Display all workschedules"
				+ "\n\tToggle - Toggle the system clock On/Off";

		if( depot.isManager() )
			strMenuText += "\n\tSetup - Setup a new schedule"
					+ "\n\tMove - Move a vehicle to another depot"
					+ "\n\tVehicles - View a depot's vehicle list"
					+ "\n\tDrivers - View a depot's driver list";

		System.out.println( strMenuText );
	}

	/**
	 * Create a new workschedule between a vehicle and driver with specified
	 * dates
	 * 
	 * @param depot
	 * @throws EscapeException
	 * @throws AuthenticationException
	 */
	private static void SetupWorkSchedule( Depot depot )
			throws EscapeException, AuthenticationException
	{
		WorkSchedule schedule = RequestScheduleInput( depot );
		Vehicle vehicle = null;

		while( true )
		{
			vehicle = RequestVehicleInput( depot );

			try
			{
				if( vehicle.ScanForOverlap( schedule.getDateStart() )
						&& vehicle.ScanForOverlap(
								schedule.getDateEnd() ) )
					throw new DateException(
							"!!! Vehicle is not available for this date !!!" );

				System.out.println( "### Vehicle Accepted ###" );
				break;
			} catch( DateException e )
			{
				System.out.println( "\t" + e.getMessage()
						+ "\n\tEnter V to View the Vehicle list and try again"
						+ "\n\tEnter Y to try again"
						+ "\n\tPress Enter to return to main menu" );

				String strReply = scanner.nextLine().toLowerCase();

				if( strReply.equals( "v" ) )
					PrintVehicleIDList( depot );
				else if( strReply.equals( "y" ) )
					continue;
				else
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}

		Driver driver = null;

		while( true )
		{
			driver = RequestDriverInput( depot );

			try
			{
				if( driver.ScanForOverlap( schedule.getDateStart() )
						&& driver.ScanForOverlap(
								schedule.getDateEnd() ) )
					throw new DateException(
							"!!! Driver is not available for this date !!!" );

				System.out.println( "### Driver Accepted ###" );
				break;
			} catch( DateException e )
			{
				System.out.println( "\t" + e.getMessage()
						+ "\n\tEnter V to View the Driver list and try again"
						+ "\n\tEnter Y to try again"
						+ "\n\tPress Enter to return to main menu" );

				String strReply = scanner.nextLine().toLowerCase();

				if( strReply.equals( "v" ) )
					PrintDriverIDList( depot );
				else if( strReply.equals( "y" ) )
					continue;
				else
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}

		if( driver == null || vehicle == null )
			throw new EscapeException( "!!! Unhandled Error !!" );

		depot.SetupWorkSchedule( schedule, driver, vehicle, false );

		System.out.println( "Setup new WorkSchedule" );
	}

	/**
	 * Prompt the user to enter the details of the new schedule (Client,
	 * Start/End Dates)
	 * 
	 * @param depot
	 * @return WorkSchedule - The newly created schedule
	 * @throws EscapeException
	 */
	private static WorkSchedule RequestScheduleInput( Depot depot )
			throws EscapeException
	{
		WorkSchedule schedule = new WorkSchedule();

		String strInput;

		System.out.println( "### Creating New Schedule ###" );

		while( true )
		{
			System.out.println( "Please enter a Client Name" );

			strInput = scanner.nextLine();

			schedule.setStrClient( strInput );
			break;
		}

		while( true )
		{
			try
			{
				Date date = RequestDateInput( depot,
						"Please enter a start date", "hh:mm dd-MM-yy" );

				schedule.setDateStart( date );
				break;
			} catch( DateException e )
			{
				if( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}

		while( true )
		{
			try
			{
				Date date = RequestDateInput( depot,
						"Please enter an ending date", "hh:mm dd-MM-yy" );

				schedule.setDateEnd( date );
				break;
			} catch( DateException e )
			{
				if( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}

		System.out.println( "### Schedule Created Successfully ###" );
		return schedule;
	}

	/**
	 * Prompt the user to provide a vehicle registration
	 * 
	 * @param depot
	 * @return Vehicle - The vehicle with specified Reg#
	 * @throws EscapeException
	 */
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
				System.out.println( "\t" + e.getMessage()
						+ "\n\tEnter V to View the Vehicle list and try again"
						+ "\n\tEnter Y to try again"
						+ "\n\tPress Enter to return to main menu" );

				String strReply = scanner.nextLine().toLowerCase();

				if( strReply.equals( "v" ) )
					PrintVehicleIDList( depot );
				else if( strReply.equals( "y" ) )
					continue;
				else
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}
	}

	/**
	 * Prompt the user to provide a drivers username
	 * 
	 * @param depot
	 * @return Driver - The driver with specified Username
	 * @throws EscapeException
	 */
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
						+ "\n\tEnter V to View the Driver list and try again"
						+ "\n\tEnter Y to try again"
						+ "\n\tPress Enter to return to main menu" );

				String strReply = scanner.nextLine().toLowerCase();

				if( strReply.equals( "v" ) )
					PrintDriverIDList( depot );
				else if( strReply.equals( "y" ) )
					continue;
				else
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}
	}

	// TODO Setup Move Function
	/**
	 * Move a vehicle from one depot to another
	 * 
	 * @param depot
	 * @throws EscapeException
	 */
	private static void MoveVehicle( Depot depot )
			throws EscapeException
	{
		Vehicle vehicle = RequestVehicleInput( depot );

		while( true )
		{
			Date moveDate = RequestDateInput( depot,
					"Please enter a move date" );

			try
			{
				if( !moveDate.after( DateMgr.getDate() ) )
					throw new DateException(
							"!!! Date must be in the future !!!" );

				if( !vehicle.getAvailable() )
					throw new DateException(
							"!!! Vehicle has ACTIVE/PENDING schedules !!!" );

				Depot depotNew = RequestDepotInput();

				depot.RemoveVehicle( vehicle.getRegNo() );
			
				MoveBuffer.AddToBuffer( vehicle, depotNew, moveDate );
				break;
			} catch( DateException | NotFoundException e )
			{
				if( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}
	}

	/**
	 * Prompt the user to enter a date
	 * 
	 * @param depot
	 * @param strPrompt
	 *            - The message to display in the console prior to entry
	 * @return
	 * @throws EscapeException
	 */
	private static Date RequestDateInput( Depot depot,
			String strPrompt ) throws EscapeException
	{
		return RequestDateInput( depot, strPrompt, "hh:mm dd-MM-yy" );
	}

	/**
	 * Prompt the user to enter a date
	 * 
	 * @param depot
	 * @param strPrompt
	 *            - The message to display in the console prior to entry
	 * @param strFormat
	 *            - The format the date should follow <i>(E.G dd-MM-yy)</i>
	 * @return
	 * @throws EscapeException
	 */
	private static Date RequestDateInput( Depot depot,
			String strPrompt, String strFormat )
					throws EscapeException
	{
		DateFormat format = new SimpleDateFormat( strFormat );

		while( true )
		{
			System.out.println( strPrompt + " (" + strFormat + ")" );
			String strInput = scanner.nextLine();

			try
			{
				Date date = format.parse( strInput );

				return date;
			} catch( ParseException e )
			{
				if( !ShouldRetry( e.getMessage() ) )
					throw new EscapeException(
							"### Returning to Main Menu ###" );
			}
		}
	}

	/**
	 * Update the system clock with a new date/time
	 * 
	 * @param depot
	 * @throws EscapeException
	 */
	private static void SetSystemDate( Depot depot )
			throws EscapeException
	{
		Date date = RequestDateInput( depot,
				"Please enter a new system date" );
		DateMgr.setDate( date );

		System.out.println( "\tNew system date applied: " + date );
	}

	/**
	 * Prints a list of Depot ID's
	 */
	private static void PrintDepotIDList()
	{
		for( Depot depot : arrDepots )
			System.out.println( "\t" + depot.getID() );
	}

	/**
	 * Prints a list of all vehicle Reg#'s in a depot
	 * 
	 * @param depot
	 */
	private static void PrintVehicleIDList( Depot depot )
	{
		for( String strID : depot.getVehicleMap().keySet() )
			System.out.println( "\t" + strID );
	}

	/**
	 * Prints a list of all driver's usernames in a depot
	 * 
	 * @param depot
	 */
	private static void PrintDriverIDList( Depot depot )
	{
		for( String strID : depot.getDriverMap().keySet() )
			System.out.println( "\t" + strID );
	}

	/**
	 * Prints a defined list of workschedules contents
	 * 
	 * @param arrSchedules
	 */
	private static void PrintSchedules(
			ArrayList< WorkSchedule > arrSchedules )
	{
		int i = 0;

		for( WorkSchedule schedule : arrSchedules )
		{
			i++;

			System.out.println( "- Schedule " + i + " -"
					+ "\n\tClient ID: " + schedule.getStrClient()
					+ "\n\tState: " + schedule.getState()
					+ "\n\tStart Date: " + schedule.getDateStart()
					+ "\n\tEnd Date: " + schedule.getDateEnd()
					+ "\n\tDriver: "
					+ schedule.getDriver().getUsername()
					+ "\n\tVehicle: "
					+ schedule.getVehicle().getRegNo() );
		}
	}

	/**
	 * Prompt the user to define whether an event should happen again
	 * 
	 * @param strOutput
	 * @return
	 */
	private static boolean ShouldRetry( String strOutput )
	{
		System.out.println( strOutput
				+ "\n\tEnter Y to retry, otherwise press Enter" );

		String strInput = scanner.nextLine().toLowerCase();

		if( strInput.equals( "y" ) )
			return true;

		return false;
	}

	/**
	 * Finds the Depot with the associated ID
	 * 
	 * @param strID
	 * @return Depot
	 */
	private static Depot FindDepot( String strID )
			throws NotFoundException
	{
		for( Depot depot : arrDepots )
		{
			if( depot.getID().equalsIgnoreCase( strID ) )
				return depot;
		}

		throw new NotFoundException(
				"No Depot found with the specified ID"
						+ "\nPlease try again, or press Enter to end the program" );
	}

	/**
	 * Update every schedule's state in the entire system Usually only performed
	 * when a system clock change occurs
	 */
	public static void UpdateScheduleStates()
	{
		for( Depot depot : arrDepots )
		{
			for( WorkSchedule schedule : depot.getScheduleList() )
				schedule.UpdateState();
		}
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
