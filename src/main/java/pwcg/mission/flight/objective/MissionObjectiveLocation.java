package pwcg.mission.flight.objective;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionObjectiveLocation
{
    static String formMissionObjectiveLocation(Coordinate targetLocation) throws PWCGException 
    {
        String missionObjectiveLocation = "";
        String targetName =  PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(targetLocation).getName();
        if (!targetName.isEmpty())
        {
            missionObjectiveLocation =   " near " + targetName;
        }
        
        return missionObjectiveLocation;
    }

    static String getMissionObjectiveLocation(Squadron squadron, Date date, Coordinate position) throws PWCGException 
    {
        String targetName =  PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(position).getName();
        return " near " + targetName;
    }
}
