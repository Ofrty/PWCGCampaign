package pwcg.dev.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.dev.utils.AirfieldDistanceOrganizer.AirfieldSet;

public class AirfieldBestMMatchFinder
{
    public static Airfield recommendBestMatch(Squadron squadron, Date date) throws PWCGException
    {        
        Airfield squadronField = squadron.determineCurrentAirfieldAnyMap(date);

        double closest = 100000000.0;
        Airfield bestField = null;
        
        AirfieldDistanceOrganizer airfieldDistanceOrganizer = new AirfieldDistanceOrganizer();
        airfieldDistanceOrganizer.process(date, FrontMapIdentifier.MOSCOW_MAP);
        
        AirfieldSet airfieldSet = airfieldDistanceOrganizer.axisAirfieldSet;
        if (squadron.determineSquadronCountry(date).getSide() == Side.ALLIED)
        {
            airfieldSet = airfieldDistanceOrganizer.alliedAirfieldSet;
        }
        
        List<Airfield> relativeFields = new ArrayList<Airfield>(airfieldSet.getBomberFields().values());
        Role squadronRole = squadron.determineSquadronPrimaryRole(date);
        if (squadronRole.isRoleCategory(RoleCategory.FIGHTER))
        {
            relativeFields = new ArrayList<Airfield>(airfieldSet.getFighterFields().values());
        }
        
        for (Airfield field: relativeFields)
        {
            double distanceToOtherField = MathUtils.calcDist(squadronField.getPosition(), field.getPosition());
            if (distanceToOtherField < closest)
            {
                closest = distanceToOtherField;
                bestField = field;
            }
        }
        
        PWCGLogger.log(LogLevel.DEBUG, bestField.getName() + "   Km to front: " + closest);
        
        return bestField;
    }
}
