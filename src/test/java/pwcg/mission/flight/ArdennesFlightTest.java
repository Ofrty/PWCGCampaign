package pwcg.mission.flight;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class ArdennesFlightTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGLogger.setActiveLogLevel(LogLevel.DEBUG);
    }

    @Test
    public void hasGermanAssaultdGroundAttackTest() throws PWCGException
    {
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441220"), Side.AXIS);
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441224"), Side.AXIS);
    }

    @Test
    public void hasAlliednAssaultdGroundAttackTest() throws PWCGException
    {
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441229"), Side.ALLIED);
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441230"), Side.ALLIED);
    }

    private void verifyAntiArmorOnDate(Date date, Side side) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        assert (mission.getSkirmish() != null);

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, side));
        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.LOW_ALT_CAP, side));

        boolean armorAttackFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_ARMOR, side);
        boolean infantryAttackFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_INFANTRY, side);
        assert(armorAttackFound || infantryAttackFound);
    }

    @Test
    public void hasSkirmishAndCargoDropTest() throws PWCGException
    {
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19441225"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19441228"));
    }

    private void verifyCargoDropsOnDate(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        assert (mission.getSkirmish() != null);

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.CARGO_DROP, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_INFANTRY, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.BOMB, TargetType.TARGET_INFANTRY, Side.ALLIED));
        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19441219"));
        noSkirmish(DateUtils.getDateYYYYMMDD("19441231"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        assert (mission.getSkirmish() == null);
    }
}