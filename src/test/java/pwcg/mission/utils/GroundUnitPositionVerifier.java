package pwcg.mission.utils;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitPositionDuplicateDetector;
import pwcg.mission.ground.org.GroundUnitCollection;

public class GroundUnitPositionVerifier
{
    public static void verifyGroundUnitPositionsAndAssert (Mission mission) throws PWCGException
    {
        verifyNoDUplicatePositions(mission);
        verifyProperBattlePlacement(mission);
    }

    private static void verifyNoDUplicatePositions(Mission mission) throws PWCGException
    {
        GroundUnitPositionDuplicateDetector duplicateDetector = new GroundUnitPositionDuplicateDetector();
        boolean noDuplicates = duplicateDetector.verifyMissionGroundUnitPositionsNotDuplicated(
                mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits(), mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits());
        assert (noDuplicates);
    }
    

    private static void verifyProperBattlePlacement(Mission mission) throws PWCGException
    {
        for (GroundUnitCollection assault : mission.getMissionGroundUnitBuilder().getAssaults())
        {
            assert (mission.getMissionBorders().isInBox(assault.getPosition()));
        }
    }
}