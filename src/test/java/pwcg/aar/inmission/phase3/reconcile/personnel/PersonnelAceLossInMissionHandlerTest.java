package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class PersonnelAceLossInMissionHandlerTest
{
    private Campaign campaign;
    private List<LogPilot> aceStatusList;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
        LogPilot wernerVoss = new LogPilot();
        wernerVoss.setSerialNumber(101175);
        wernerVoss.setStatus(SquadronMemberStatus.STATUS_KIA);
        
        LogPilot georgesGuynemer = new LogPilot();
        georgesGuynemer.setSerialNumber(101064);
        georgesGuynemer.setStatus(SquadronMemberStatus.STATUS_KIA);

        aceStatusList = new ArrayList<>();
        aceStatusList.add(wernerVoss);
        aceStatusList.add(georgesGuynemer);
    }

    @Test
    public void testHistoricalAcesKille() throws PWCGException
    {
        PersonnelAceLossInMissionHandler aceLossInMissionHandler = new PersonnelAceLossInMissionHandler(campaign);
        List<SquadronMember> acesKilled = aceLossInMissionHandler.acesShotDownInMission(aceStatusList);
        
        assert(acesKilled.size() == 2);
    }

}
