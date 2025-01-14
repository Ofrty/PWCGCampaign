package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointPriorityValidator
{

    public static void validateWaypointTypes(IFlight flight) throws PWCGException
    {

        WaypointPriority expectedWaypointPriority = WaypointPriority.PRIORITY_MED;
        if (flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            expectedWaypointPriority = WaypointPriority.PRIORITY_LOW;
        }
        else
        {
            expectedWaypointPriority = WaypointPriority.PRIORITY_MED;
            if (FlightTypes.isHighPriorityFlight(flight.getFlightType()))
            {
                throw new PWCGException("No high priority flights");
            }
        }

        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            if (FlightTypes.isHighPriorityFlight(flight.getFlightType())) 
            {
                assert (waypoint.getPriority() == WaypointPriority.PRIORITY_HIGH);    
            }
            else if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TAKEOFF))
            {
                assert (waypoint.getPriority() != WaypointPriority.PRIORITY_LOW);
            }
            else if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_LANDING_APPROACH))
            {
                assert (waypoint.getPriority() == WaypointPriority.PRIORITY_MED);
            }
            else
            {
                assert (waypoint.getPriority() == expectedWaypointPriority);
            }
        }
    }
}
