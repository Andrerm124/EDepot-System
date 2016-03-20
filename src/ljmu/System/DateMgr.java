package ljmu.System;

import java.util.Calendar;
import java.util.Date;

import ljmu.Buffer.MoveBuffer;

public class DateMgr
{
	static Date dateCur;
	static Calendar calendar;
	static boolean isAlive = true;
	static long lngDelay = 5000l;

	public DateMgr()
	{
		InitialiseSystem();

		StartThread();
	}

	/**
	 * Initialise the DateManagers resources
	 */
	private static void InitialiseSystem()
	{
		calendar = Calendar.getInstance();
		setDate( new Date() );
	}

	/**
	 * Begin the Date Thread that handles date looping
	 */
	private static void StartThread()
	{
		// Create a new thread to handle the system clock
		new Thread()
		{
			@SuppressWarnings( "all" )
			@Override
			public void run()
			{
				// Make a loop so that the clock repeatedly functions
				while( isAlive )
				{
					try
					{
						calendar.add( Calendar.DATE, 1 );
						dateCur = calendar.getTime();

						IOHandler.UpdateScheduleStates();
						MoveBuffer.UpdateBuffer();
						Thread.sleep( lngDelay );
					} catch( InterruptedException e )
					{
					}
				}
			}
		}.start();
	}

	/**
	 * Toggles the system clock
	 */
	public static void ToggleState()
	{
		isAlive = !isAlive;

		if( isAlive )
			StartThread();
	}

	/**
	 * Allows enable/disabling of the system clock
	 * 
	 * @param isAlive
	 */
	public static void setAlive( boolean isAlive )
	{
		DateMgr.isAlive = isAlive;

		if( isAlive )
			StartThread();
	}

	/**
	 * Return the state of the system clock
	 * 
	 * @return <b>true</b> when <b>active</b>
	 */
	public static boolean getAlive()
	{
		return DateMgr.isAlive;
	}

	/**
	 * @return Current system date
	 */
	public static Date getDate()
	{
		return dateCur;
	}

	/**
	 * Allows setting of system date
	 * 
	 * @param date
	 */
	public static void setDate( Date date )
	{
		dateCur = date;
		getCalendar().setTime( dateCur );
		IOHandler.UpdateScheduleStates();
	}

	/**
	 * Provides the current system calendar for comparisons
	 * 
	 * @return Calendar
	 */
	public static Calendar getCalendar()
	{
		return calendar;
	}
}