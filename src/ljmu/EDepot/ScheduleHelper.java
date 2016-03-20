package ljmu.EDepot;

import java.util.Date;

public interface ScheduleHelper
{
	public void UpdateActiveSchedule( WorkSchedule schedule );
	
	public boolean ScanForOverlap( Date date );
}
