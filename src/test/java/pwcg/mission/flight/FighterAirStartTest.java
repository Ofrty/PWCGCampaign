package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.validate.EscortForPlayerValidator;
import pwcg.mission.flight.validate.FighterAirStartFlightValidator;
import pwcg.mission.flight.validate.FlightActivateValidator;
import pwcg.mission.flight.validate.PlaneRtbValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypointPackageValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class FighterAirStartTest 
{    
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.AllowAirStartsKey, "1");
    }

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.PATROL, MissionProfile.DAY_TACTICAL_MISSION);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_START);
        assert (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        FighterAirStartFlightValidator airStartFlightValidator = new FighterAirStartFlightValidator();
        airStartFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.PATROL);
        FlightActivateValidator.validate(flight);
        
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getMissionFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
	}
	
}
