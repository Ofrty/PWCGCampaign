package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.CampaignModeChooser;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;

public class MissionBorderBuilderFactory 
{
    public static IMissionCenterBuilder buildMissionCenterBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers, Skirmish skirmish, MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        if (TestDriver.getInstance().getTestMissionCenter() != null)
        {
            return new MissionCenterBuilderTest();
        }
        else if (playerFlightTypes.isStrategicInterceptPlayerFlight() && campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            return new MissionCenterBuilderStrategicIntercept(campaign, participatingPlayers);
        }
        else if (skirmish != null)
        {
            return new MissionCenterBuilderSkirmish(campaign, skirmish);
        }
        else if (CampaignModeChooser.isCampaignModeCompetitive(campaign))
        {
            return new MissionCenterBuilderMulti(campaign, participatingPlayers);
        }
        else
        {
            return new MissionCenterBuilderFrontLines(campaign, participatingPlayers);
        }
    }
}
