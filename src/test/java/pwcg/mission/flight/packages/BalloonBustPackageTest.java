package pwcg.mission.flight.packages;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitElement;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.infantry.BalloonUnit;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.PwcgTestBase;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class BalloonBustPackageTest extends PwcgTestBase
{
    public BalloonBustPackageTest()
    {
        super (PWCGProduct.BOS);
    }

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }

    @Test
    public void balloonBustFlightTest() throws PWCGException
    {
        IFlight flight = buildFlight();
        verifyBalloonPosition(flight);
   }

    private IFlight buildFlight() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.BALLOON_BUST, playerSquadron);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);

        return mission.getMissionFlights().getPlayerFlights().get(0);
    }

    private void verifyBalloonPosition(IFlight flight) throws PWCGException
    {
        List<BalloonUnit> balloons = getBalloonUnits(flight);

        boolean closeToBalloon = false;
        for (BalloonUnit balloonUnit : balloons)
        {
            verifyBallonUnitHasBalloon(balloonUnit);

            for (McuWaypoint balloonDefenseWaypoint : flight.getWaypointPackage().getAllWaypoints())
            {
                if (balloonDefenseWaypoint.getWaypointType() == WaypointType.BALLOON_BUST_WAYPOINT)
                {
                    double distanceFromBalloon = MathUtils.calcDist(balloonDefenseWaypoint.getPosition(), balloonUnit.getPosition());
                    if (distanceFromBalloon < 8000)
                    {
                        closeToBalloon = true;
                    }
                }
            }
        }
        assert(closeToBalloon);
    }

    private void verifyBallonUnitHasBalloon(BalloonUnit balloonUnit)
    {
        GroundUnitElement balloonElement = null;
        for(GroundUnitElement groundUnitElement : balloonUnit.getGroundElements())
        {
            if (Balloon.isBalloonName(groundUnitElement.getVehicle().getScript()))
            {
                balloonElement = groundUnitElement;
            }
        }
        assert(balloonElement != null);
    }

    private List<BalloonUnit> getBalloonUnits(IFlight flight) throws PWCGException
    {
        List<BalloonUnit> balloons = new ArrayList<>();
        for (GroundUnitCollection groundUnitCollection : flight.getMission().getMissionGroundUnitBuilder().getBalloonUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                if (groundUnit instanceof BalloonUnit)
                {
                    if (flight.getSquadron().getCountry().getSide() == groundUnit.getCountry().getSide().getOppositeSide())
                    {
                        BalloonUnit balloonUnit = (BalloonUnit) groundUnit;
                        balloons.add(balloonUnit);
                    }
                }
            }
        }
        assert(balloons.size() > 0);
        return balloons;
    }
}
