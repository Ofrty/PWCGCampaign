package pwcg.mission.flight;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.escort.PlayerIsEscortFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.scramble.PlayerScrambleFlight;
import pwcg.mission.flight.strategicintercept.StrategicInterceptFlight;
import pwcg.mission.flight.validate.EscortForPlayerValidator;
import pwcg.mission.flight.validate.FlightActivateValidator;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.mission.flight.validate.PlaneRtbValidator;
import pwcg.mission.flight.validate.PlayerEscortFlightValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypointPackageValidator;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.GroundUnitPositionVerifier;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class PlayerFlightTypeBoSFighterTest 
{    
    private Campaign campaign;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.PATROL, MissionProfile.DAY_TACTICAL_MISSION);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        assert (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.PATROL);
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getMissionFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getMissionFlights().getAiFlights().size() >= 3);
        
        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
	}

    @Test
    public void interceptFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.INTERCEPT, MissionProfile.DAY_TACTICAL_MISSION);
        InterceptFlight flight = (InterceptFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        assert (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.INTERCEPT);
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getMissionFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getMissionFlights().getAiFlights().size() >= 3);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
    }

    @Test
    public void strategicInterceptFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.STRATEGIC_INTERCEPT, MissionProfile.DAY_TACTICAL_MISSION);
        StrategicInterceptFlight flight = (StrategicInterceptFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        assert (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT);
        FlightActivateValidator.validate(flight);
        
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getMissionFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
        
        assert(mission.getMissionFlights().getAiFlights().size() == 1);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
    }

	@Test
	public void offensiveFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.OFFENSIVE, MissionProfile.DAY_TACTICAL_MISSION);
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        assert (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.OFFENSIVE);
        
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getMissionFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getMissionFlights().getAiFlights().size() >= 3);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
	}

	@Test
	public void escortFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT, MissionProfile.DAY_TACTICAL_MISSION);
        PlayerIsEscortFlight flight = (PlayerIsEscortFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        assert (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

		PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator(mission.getMissionFlights());
		escortFlightValidator.validateEscortFlight();
        assert(flight.getFlightType() == FlightTypes.ESCORT);
        FlightActivateValidator.validate(flight);
        PositionEvaluator.evaluateAiFlight(mission);

        List<IFlight> playerEscortedFlight = mission.getMissionFlights().getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORTED);
        assert(playerEscortedFlight != null);        
        assert(flight.getAssociatedFlight() != null);        

        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getMissionFlights().getAiFlights().size() >= 3);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
	}

    @Test
    public void scrambleFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.SCRAMBLE, MissionProfile.DAY_TACTICAL_MISSION);
        PlayerScrambleFlight flight = (PlayerScrambleFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        assert (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.SCRAMBLE);        
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getMissionFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getMissionFlights().getAiFlights().size() >= 3);
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
	{
        assert (targetDefinition.getCountry() != null);
        assert (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        assert (targetDefinition.getTargetType() != TargetType.TARGET_NONE);
	}
}
