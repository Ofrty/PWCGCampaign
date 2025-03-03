package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.equipment.EquipmentReplacementHandler;
import pwcg.campaign.resupply.equipment.EquipmentResupplyData;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentReplacementHandlerTest
{
    private Campaign campaign;
    
    @Mock private ArmedService armedService;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        Mockito.when(armedService.getServiceId()).thenReturn(20101);
     }

    @Test
    public void testTransfersInForLostCampaignMembers() throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler squadronTransferHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        
        deactivateCampaignEquipment();
      
        EquipmentResupplyData equipmentTransferData = squadronTransferHandler.resupplyForLosses(armedService);
        assert (equipmentTransferData.getTransferCount() == 3);
    }

    private void deactivateCampaignEquipment() throws PWCGException
    {
        Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);
        Squadron playerSquadron = campaign.determinePlayerSquadrons().get(0);
        int numInactivated = 0;
        for (Equipment equipment: campaign.getEquipmentManager().getEquipmentAllSquadrons().values())
        {
            for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
            {
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(equippedPlane.getSquadronId());
                if (squadron.getService() == armedService.getServiceId())
                {
                    if (playerSquadron.getSquadronId() != equippedPlane.getSquadronId())
                    {
                        equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
                        equippedPlane.setDateRemovedFromService(inactiveDate);
                        ++numInactivated;
                    }
                }
                
                break;
            }

            if (numInactivated == 3)
            {
                break;
            }
        }
    }

    @Test
    public void testTransfersInForLostSquadronMembers() throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler squadronTransferHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        
        deactivateSquadronEquipment();
      
        EquipmentResupplyData equipmentTransferData = squadronTransferHandler.resupplyForLosses(armedService);
        assert (equipmentTransferData.getTransferCount() == 3);
    }

    private void deactivateSquadronEquipment() throws PWCGException
    {
        Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);

        int numInactivated = 0;
        Squadron playerSquadron = campaign.determinePlayerSquadrons().get(0);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(playerSquadron.getSquadronId());
        for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadron.getSquadronId());
            if (squadron.getSquadronId() == equippedPlane.getSquadronId())
            {
                System.out.println("Deactivate: " + equippedPlane.getSerialNumber() + " for " + equippedPlane.getSquadronId());
                
                equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
                equippedPlane.setDateRemovedFromService(inactiveDate);
                ++numInactivated;
            }
                            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
