package ljmu.Buffer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ljmu.EDepot.Depot;
import ljmu.EDepot.Vehicle;
import ljmu.Exceptions.ContainException;
import ljmu.System.DateMgr;

public class MoveBuffer
{
	private static ArrayList< BufferDetails > buffer = new ArrayList< >();
	private static Date lastScan;
	
	public MoveBuffer()
	{
		
	}
	
	/**
	 * Add a vehicle to a buffer to simulate moving between depots
	 * @param vehicle
	 * @param depot
	 * @param date
	 */
	public static void AddToBuffer( Vehicle vehicle, Depot depot, Date date )
	{
		buffer.add( new BufferDetails( vehicle, depot, date ) );
	}
	
	/**
	 * Check the buffer for any vehicles arriving at their depot
	 */
	public static void UpdateBuffer()
	{		
		// Check if there has been a previous scan
		if( lastScan == null || lastScan.before( DateMgr.getDate() ) )
		{
			// Create a delay between scans
			Calendar scanOffset = DateMgr.getCalendar();
			scanOffset.add( Calendar.DATE, 1 );
			
			lastScan = scanOffset.getTime();
			
			// Iterate through the buffer for any vehicles requiring movement
			for( BufferDetails scanVal : buffer )
			{
				if( scanVal.getDate().after( DateMgr.getDate() ) )
				{
					try
					{
						scanVal.getDepot().AddVehicle( scanVal.getVehicle() );
					} catch( ContainException e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					buffer.remove( scanVal );
				}
			}
		}
	}
}
