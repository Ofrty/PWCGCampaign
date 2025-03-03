package pwcg.aar.campaign.update;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class CampaignServiceChangeHandlerTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }

    @Test
    public void testRafTransition() throws PWCGException 
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_2_PROFILE);
        ArmedService service = campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(campaign.getDate());
        ICountry country = service.getCountry();
        SquadronPersonnel personnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.RFC_2_PROFILE.getSquadronId());

        assertTrue (country.getCountry() == Country.BRITAIN);
        assertTrue (service.getName().equals(FCServiceManager.RFC_NAME));

        SquadronMembers rfcSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : rfcSquadronMembers.getSquadronMemberList())
        {
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }

        ServiceChangeHandler serviceChangeHandler = new ServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(DateUtils.getRAFDate());
                
        service = campaign.determinePlayerSquadrons().get(0).determineServiceForSquadron(DateUtils.getRAFDate());
        assertTrue (service.getCountry().getCountry() == Country.BRITAIN);
        assertTrue (service.getName().equals(FCServiceManager.RAF_NAME));

        SquadronMembers rafSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(personnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : rafSquadronMembers.getSquadronMemberList())
        {
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(squadronMember.getRank(), service);
            assert(rankPos >= 0);
        }
    }
}
