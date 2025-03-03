package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;

public class TestMissionBuilderUtility
{

    public static Mission createTestMission(Campaign campaign, MissionHumanParticipants participatingPlayers, CoordinateBox missionBorders,
            MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Skirmish skirmish = null;
        VehicleDefinition playerVehicleDefinition = null;
        Mission mission = new Mission(campaign, missionProfile, participatingPlayers, playerVehicleDefinition, missionBorders, weather, skirmish, missionOptions);
        campaign.setCurrentMission(mission);
        return mission;
    }

    public static MissionFlights createTestMission(Campaign campaign, MissionProfile missionProfile, FlightTypes flightType) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = buildTestParticipatingHumans(campaign);

        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(flightType, playerSquadron);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, missionProfile);
        mission.generate(playerFlightTypes);

        campaign.setCurrentMission(mission);
        return mission.getMissionFlights();
    }

    public static MissionHumanParticipants buildTestParticipatingHumans(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (SquadronMember player: campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        return participatingPlayers;
    }
}
