package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.MissionEntityBuilder;

@RunWith(MockitoJUnitRunner.class)
public class AcesKilledEventGeneratorTest
{
    @Mock private Campaign campaign;
    @Mock CampaignPersonnelManager personnelManager;
    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
         
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170420"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
    }

    @Test
    public void aceKilled () throws PWCGException
    {       
        Ace aceKilledInMission = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        List<SquadronMember> acesKilledInMissionAndElapsedTime = new ArrayList<>();
        acesKilledInMissionAndElapsedTime.add(aceKilledInMission);

        AcesKilledEventGenerator acesKilledEventGenerator = new AcesKilledEventGenerator(campaign);
        List<AceKilledEvent> acesKilledEvents = acesKilledEventGenerator.createAceKilledEvents(acesKilledInMissionAndElapsedTime);
        
        assert (acesKilledEvents.size() == 1);
    }

    @Test
    public void aceKilledNotEnoughVictories () throws PWCGException
    {       
        Ace aceKilledInMission = new Ace();
        aceKilledInMission.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        for (int i = 0; i < AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY-1; ++i)
        {
            Victory victory = new Victory();
            aceKilledInMission.addVictory(victory);
        }

        List<SquadronMember> acesKilledInMissionAndElapsedTime = new ArrayList<>();
        acesKilledInMissionAndElapsedTime.add(aceKilledInMission);

        AcesKilledEventGenerator acesKilledEventGenerator = new AcesKilledEventGenerator(campaign);
        List<AceKilledEvent> acesKilledEvents = acesKilledEventGenerator.createAceKilledEvents(acesKilledInMissionAndElapsedTime);
        
        assert (acesKilledEvents.size() == 0);
    }
}
