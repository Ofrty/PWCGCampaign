package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.StructureVictoryBuilder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class OutOfMissionStructureVictoryBuilderTest
{
    private Campaign campaign;
    private SquadronMember squadronMember;

    @Mock private AARContext aarContext;
    @Mock private ArmedService service;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        
        for (SquadronMember pilot : campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_STALINGRAD.getSquadronId()).getActiveAiSquadronMembers().getSquadronMemberList())
        {
            if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE && !pilot.isPlayer())
            {
                squadronMember = pilot;
                break;
            }
        }
    }

    @Test
    public void testVictoryAwarded () throws PWCGException
    {     
        StructureVictoryBuilder victoryGenerator = new StructureVictoryBuilder(squadronMember, PwcgStructure.BRIDGE);
        Victory victory = victoryGenerator.generateOutOfMissionVictory(campaign.getDate());
        
        assert (victory.getVictim().getAirOrGround() == Victory.VEHICLE);
        assert (victory.getVictim().getName().equals(PwcgStructure.BRIDGE.getDescription()));
    }
}
