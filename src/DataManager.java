import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ljmu.EDepot.Depot;
import ljmu.Exceptions.ContainException;

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
	public static boolean LoadHashmap( File sampleData )
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
	public static void SaveHashmap( File sampleData,
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
	public static void CreateDebugData( ArrayList< Depot > arrDepots )
	{
		// Create 10 new depots \\
		for( int i = 0; i < 10; i++ )
		{
			Depot depot = new Depot( "Depot" + i );

			arrDepots.add( depot );
			
			try
			{
				depot.AddManager( "Manager1", "default" );
			} catch( ContainException e1 )
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Create 1-21 drivers, tankers, and trucks with specific data \\
			for( int intDriverIndex = 0; intDriverIndex < 1
					+ ( Math.random() * 20 ); intDriverIndex++ )
			{
				try
				{
					depot.AddDriver( "User" + intDriverIndex, "default" );
				} catch( ContainException e ) {}
			}

			for( int intTankerIndex = 0; intTankerIndex < 1
					+ ( Math.random() * 20 ); intTankerIndex++ )
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for( int intTruckIndex = 0; intTruckIndex < 1
					+ ( Math.random() * 20 ); intTruckIndex++ )
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// Used for debugging purposes \\
		System.out.println( "Breakpoint" );
	}
}
