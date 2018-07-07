package pwcg.aar.outofmission.phase2.resupply;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase2.resupply.HistoricalAceTransferHandler;
import pwcg.aar.outofmission.phase2.resupply.SquadronTransferData;
import pwcg.aar.outofmission.phase2.resupply.TransferRecord;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class HistoricalAceTransferHandlerTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;
    
    @Mock
    private AARPersonnelLosses lossesInMissionData;

    private CampaignAces campaignAces;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170430"));
        Mockito.when(campaign.getSquadronId()).thenReturn(501011);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        
        campaignAces = PWCGContextManager.getInstance().getAceManager().loadFromHistoricalAces(DateUtils.getDateYYYYMMDD("19170430")); 
        Mockito.when(campaignPersonnelManager.getCampaignAces()).thenReturn(campaignAces);
        
    }

    @Test
    public void testKarlSchaferTransferToJasta28() throws PWCGException
    {
        HistoricalAceTransferHandler historicalAceTransferHandler = new HistoricalAceTransferHandler(campaign, DateUtils.getDateYYYYMMDD("19170503"));
        SquadronTransferData acesTransferred =  historicalAceTransferHandler.determineAceTransfers();
        assert (acesTransferred.getSquadronMembersTransferred().size() == 1);
        
        boolean karlSchaferFound = false;
        for (TransferRecord transferRecord : acesTransferred.getSquadronMembersTransferred())
        {
            if (transferRecord.getSquadronMember().getSerialNumber() == 101112)
            {
                karlSchaferFound = true;
            }
        }
        assert (karlSchaferFound); // Karl Schafer
    }
}