package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishBuilder;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;

public class MissionGenerator
{
    private Campaign campaign = null;

    public MissionGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Mission makeMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionProfile missionProfile = generateProfile(participatingPlayers);

        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        MissionSquadronFlightTypes playerFlightTypeOverrides = new MissionSquadronFlightTypes();
        Skirmish skirmish = getSkirmishForMission(participatingPlayers);
        Mission mission = buildMission(participatingPlayers, playerFlightTypeOverrides, missionProfile, weather, skirmish, missionOptions);
        return mission;
    }

    public Mission makeLoneWolfMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionProfile missionProfile = MissionProfile.DAY_TACTICAL_MISSION;
        
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        MissionSquadronFlightTypes playerFlightTypeOverrides = new MissionSquadronFlightTypes();
        playerFlightTypeOverrides.add(participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron(), FlightTypes.LONE_WOLF);

        Skirmish skirmish = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypeOverrides, MissionProfile.DAY_TACTICAL_MISSION, weather, skirmish, missionOptions);
        return mission;
    }

    public Mission makeTestSingleMissionFromFlightType(MissionHumanParticipants participatingPlayers, FlightTypes playerFlightType,
            MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        MissionSquadronFlightTypes playerFlightTypeOverrides = new MissionSquadronFlightTypes();
        playerFlightTypeOverrides.add(participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron(), playerFlightType);
        
        Mission mission = buildMission(participatingPlayers, playerFlightTypeOverrides, missionProfile, weather, null, missionOptions);
        return mission;
    }

    public Mission makeTestCoopMissionFromFlightType(MissionHumanParticipants participatingPlayers, MissionSquadronFlightTypes playerFlightTypeOverrides,
            MissionProfile missionProfile) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Skirmish skirmish = null;
        Mission mission = buildMission(participatingPlayers, playerFlightTypeOverrides, missionProfile, weather, skirmish, missionOptions);
        return mission;
    }

    public Mission makeTestMissionFromFlightTypeWithSkirmish(
            MissionHumanParticipants participatingPlayers, 
            MissionSquadronFlightTypes playerFlightTypeOverrides,
            MissionProfile missionProfile,
            Skirmish skirmish) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate(), missionProfile);
        missionOptions.createFlightSpecificMissionOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Mission mission = buildMission(participatingPlayers, playerFlightTypeOverrides, missionProfile, weather, skirmish, missionOptions);
        return mission;
    }

    private MissionProfile generateProfile(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionProfileGenerator missionProfileGenerator = new MissionProfileGenerator(campaign, participatingPlayers);
        MissionProfile missionProfile = missionProfileGenerator.generateMissionProfile();
        return missionProfile;
    }

    private Mission buildMission(
            MissionHumanParticipants participatingPlayers, 
            MissionSquadronFlightTypes playerFlightTypeOverrides, 
            MissionProfile missionProfile,
            MissionWeather weather, 
            Skirmish skirmish,
            MissionOptions missionOptions) throws PWCGException
    {
        campaign.setCurrentMission(null);
        
        MissionSquadronFlightTypes playerFlightTypes = makePlayerFlightTypes(playerFlightTypeOverrides, participatingPlayers, missionProfile, weather, skirmish);
        CoordinateBox missionBorders = buildMissionBorders(missionProfile, participatingPlayers, skirmish, playerFlightTypes);
        CoordinateBox structureBorders = buildStructureBorders(missionProfile, participatingPlayers, missionBorders);
        Mission mission = new Mission(campaign, missionProfile, participatingPlayers, missionBorders, structureBorders, weather, skirmish, missionOptions);
        campaign.setCurrentMission(mission);
        mission.generate(playerFlightTypes);

        return mission;
    }
    
    private MissionSquadronFlightTypes makePlayerFlightTypes(
            MissionSquadronFlightTypes playerFlightTypeOverrides, 
            MissionHumanParticipants participatingPlayers,
            MissionProfile missionProfile,
            MissionWeather weather,
            Skirmish skirmish) throws PWCGException
    {
        if (!playerFlightTypeOverrides.hasPlayerFlightTypes())
        {
            return PlayerFlightTypeBuilder.finalizePlayerFlightTypes(campaign, participatingPlayers, missionProfile, weather, skirmish);
        }
        else
        {
            return playerFlightTypeOverrides;
        }

    }
    
    private Skirmish getSkirmishForMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        SkirmishBuilder skirmishBuilder = new SkirmishBuilder(campaign, participatingPlayers);
        return skirmishBuilder.chooseBestSkirmish();
    }

    private CoordinateBox buildMissionBorders(MissionProfile missionProfile, MissionHumanParticipants participatingPlayers, Skirmish skirmish, MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, skirmish, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
        return missionBorders;
    }

    private CoordinateBox buildStructureBorders(MissionProfile missionProfile, MissionHumanParticipants participatingPlayers, CoordinateBox missionBorders) throws PWCGException
    {
        StructureBorderBuilder structureBorderBuilder = new StructureBorderBuilder(campaign, participatingPlayers, missionBorders);
        CoordinateBox structureBorder = structureBorderBuilder.getBordersForStructures();
        return structureBorder;
    }
}
