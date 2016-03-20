package ljmu.System;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import ljmu.EDepot.Depot;
import ljmu.EDepot.Driver;
import ljmu.EDepot.Vehicle;
import ljmu.EDepot.WorkSchedule;
import ljmu.Exceptions.AuthenticationException;
import ljmu.Exceptions.ContainException;
import ljmu.Exceptions.DateException;
import ljmu.Exceptions.EscapeException;

public class DataManager
{
	// TODO Check for DataManager cleanups \\
	/**
	 * Decides whether to load the sample data from a file Or whether it needs
	 * to create new sample data
	 */
	public static void HandleSampleData()
	{
		// Load the sample data file into a variable \\
		String strDirectory = System.getProperty( "user.dir" )
				+ System.getProperty( "file.separator" )
				+ "SampleData.ser";
		File sampleData = new File( strDirectory );

		// TODO Enable this before release
		// If the file exists, load the data and end method \\

		if( sampleData.exists() && LoadHashmap( sampleData ) )
			return;

		// Otherwise, create random data and save to a file \\
		CreateDebugData( IOHandler.getDepotList() );
		SaveHashmap( sampleData, IOHandler.getDepotList() );
	}

	/**
	 * Using a .ser file, load the containing data.
	 * 
	 * @param sampleData
	 * @return False if errors occurred
	 */
	@SuppressWarnings(
	{ "unchecked", "resource" } )
	private static boolean LoadHashmap( File sampleData )
	{
		try
		{
			// Load the appropriate streams \\
			FileInputStream fileInput = new FileInputStream(
					sampleData );
			ObjectInputStream objectInput = new ObjectInputStream(
					fileInput );

			// Read the data of the streams to populate the Depots ArrayList \\
			IOHandler.setDepotList(
					(ArrayList< Depot >) objectInput.readObject() );

			// Close the streams after we're finished with them\\
			objectInput.close();
			fileInput.close();
		} catch( IOException | ClassNotFoundException e )
		{
			// If any errors occurred, return false \\
			e.printStackTrace();
			return false;
		}

		// If all went well, return true \\
		return true;
	}

	/**
	 * Used only to serialise an existing map sample Only implemented for the
	 * creation of the map Not used when new data is added
	 */
	@SuppressWarnings( "resource" )
	private static void SaveHashmap( File sampleData,
			ArrayList< Depot > arrDepots )
	{
		try
		{
			// Load the appropriate streams to save the data \\
			FileOutputStream fileStream = new FileOutputStream(
					sampleData );
			ObjectOutputStream objectStream = new ObjectOutputStream(
					fileStream );

			// Write all of the depots and their contents to a file \\
			objectStream.writeObject( arrDepots );

			// Ensure the data in the buffer has passed before closing the
			// streams \\
			objectStream.flush();
			objectStream.close();
			fileStream.close();
		} catch( IOException e )
		{
			// Display any errors \\
			// As we do not particularly care if the file saved, do nothing else
			// \\
			e.printStackTrace();
		}
	}

	/**
	 * Creates a fairly random data sample to work with
	 */
	private static void CreateDebugData(
			ArrayList< Depot > arrDepots )
	{
		// Create 10 new depots \\
		for( int i = 0; i < 3; i++ )
		{
			Depot depot = new Depot( "Depot" + i );

			arrDepots.add( depot );

			CreateDriverLists( depot );
			CreateVehicleLists( depot );
			CreateScheduleLists( depot );
		}

		// Used for debugging purposes \\
		System.out.println( "Breakpoint" );
	}

	/**
	 * Generates a manager and 1-21 drivers in a depot
	 * 
	 * @param depot
	 */
	@SuppressWarnings( "unused" )
	private static void CreateDriverLists( Depot depot )
	{
		try
		{
			depot.AddManager( "Manager", "default" );
		} catch( ContainException e )
		{
			System.out.println(
					"!! Error generating Debug Manager !!" );
		}

		// Create 1-21 drivers, tankers, and trucks with specific data \\
		for( int intDriverIndex = 0; intDriverIndex < 1
				+ ( Math.random() * 20 ); intDriverIndex++ )
		{
			try
			{
				depot.AddDriver( "User" + intDriverIndex, "default" );
			} catch( ContainException e )
			{
				System.out.println(
						"!! Error generating Debug Driver !!" );
			}
		}
	}

	/**
	 * Generates between 1-21 Tankers and 1-21 Trucks in a depot
	 * 
	 * @param depot
	 */
	@SuppressWarnings( "unused" )
	private static void CreateVehicleLists( Depot depot )
	{
		for( int intTankerIndex = 0; intTankerIndex < 1
				+ ( Math.random() * 10 ); intTankerIndex++ )
		{
			try
			{
				depot.AddTanker( "Tanker" + intTankerIndex,
						"Tanker" + intTankerIndex,
						"Reg" + intTankerIndex + "Tanker",
						500 + (int) ( Math.random() * 2000d ),
						500 + (int) ( Math.random() * 2000d ),
						"Fuel" );
			} catch( ContainException e )
			{
				System.out.println(
						"!! Error generating Debug Tanker !!" );
			}
		}

		for( int intTruckIndex = 0; intTruckIndex < 1
				+ ( Math.random() * 10 ); intTruckIndex++ )
		{
			try
			{
				depot.AddTruck( "Truck" + intTruckIndex,
						"Truck" + intTruckIndex,
						"Reg" + intTruckIndex + "Truck",
						500 + (int) ( Math.random() * 2000d ),
						500 + (int) ( Math.random() * 2000d ) );
			} catch( ContainException e )
			{
				System.out.println(
						"!! Error generating Debug Truck !!" );
			}
		}
	}

	/**
	 * Generates schedules between vehicles and drivers
	 * 
	 * @param depot
	 */
	private static void CreateScheduleLists( Depot depot )
	{
		HashMap< String, Driver > hashDrivers = depot.getDriverMap();
		HashMap< String, Vehicle > hashVehicles = depot
				.getVehicleMap();

		int intCap = Math.max( hashDrivers.size(),
				hashVehicles.size() );

		Iterator< Driver > iteratorD = hashDrivers.values()
				.iterator();
		Iterator< Vehicle > iteratorV = hashVehicles.values()
				.iterator();

		Driver driver = null;
		Vehicle vehicle = null;

		for( int intIndex = 0; intIndex < intCap; intIndex++ )
		{
			if( !iteratorD.hasNext() )
				iteratorD = hashDrivers.values().iterator();

			if( !iteratorV.hasNext() )
				iteratorV = hashVehicles.values().iterator();

			driver = iteratorD.next();
			vehicle = iteratorV.next();

			if( driver == null || vehicle == null )
				continue;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime( DateMgr.getDate() );

			for( int i = 0; i < 10; i++ )
			{
				calendar.add( Calendar.DATE,
						3 + (int) ( Math.random() * 5 ) );

				WorkSchedule schedule = new WorkSchedule();
				schedule.setStrClient( "Client " + i );

				try
				{
					// System.out.println( "Date: " + calendar.getTime() );
					schedule.setDateStart( calendar.getTime() );
					calendar.add( Calendar.HOUR,
							1 + (int) ( Math.random() * 71 ) );

					schedule.setDateEnd( calendar.getTime() );

					// System.out.println( "Setting up Schedule between: " +
					// driver.getUsername() + vehicle.getRegNo() );
					depot.SetupWorkSchedule( schedule, driver,
							vehicle, true );
				} catch( DateException | AuthenticationException | EscapeException e )
				{
					e.printStackTrace();
					System.out.println(
							"!! Error generating Debug schedule !!" );
				}
			}
		}
	}
}
