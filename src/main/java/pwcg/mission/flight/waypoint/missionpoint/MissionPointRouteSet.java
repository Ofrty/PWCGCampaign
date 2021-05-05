package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointRouteSet extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;
    
    public MissionPointRouteSet()
    {
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ROUTE;
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.getLastWaypoint().setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return super.getFirstWaypoint().getIndex();
    }

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    public void addWaypoint(McuWaypoint waypoint)
    {
        super.addWaypoint(waypoint);
    }
    
    public void addWaypoints(List<McuWaypoint> waypoints)
    {
        super.addWaypoints(waypoints);
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        return super.getWaypointsAsMissionPoints();
    }
    
    @Override
    public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException
    {
        super.finalizeMissionPointSet(flightPlanes);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        super.write(writer);
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypoints.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }
}