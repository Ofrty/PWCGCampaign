package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.resupply.depot.EquipmentReplacementWeightByNeed;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentReplacementWeightByNeedTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    private void removePlanesFromCampaign(Campaign campaign) throws PWCGException
    {
        for (Equipment equipment : campaign.getEquipmentManager().getEquipmentAllSquadrons().values())
        {
            int numDestroyedOverOneWeekAgo = 0;
            int numDestroyedlessThanOneWeekAgo = 0;
            for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
            {
                if (numDestroyedOverOneWeekAgo < 2)
                {
                    Date threeWeeksAgo = DateUtils.removeTimeDays(campaign.getDate(), 21);
                    ++numDestroyedOverOneWeekAgo;
                    equippedPlane.setDateRemovedFromService(threeWeeksAgo);
                    equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
                }
                else if (numDestroyedlessThanOneWeekAgo < 1)
                {
                    Date threeDaysAgo = DateUtils.removeTimeDays(campaign.getDate(), 3);
                    ++numDestroyedlessThanOneWeekAgo;
                    equippedPlane.setDateRemovedFromService(threeDaysAgo);
                    equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
                }
                else
                {
                    break;
                }
            }
        }
    }
    
    @Test
    public void testGermanReplacementArchTypes() throws PWCGException
    {
        Campaign earlyCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        removePlanesFromCampaign(earlyCampaign);
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("bf110"));
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(aircraftUsageByArchType.containsKey("ju87"));
        assert(aircraftUsageByArchType.containsKey("ju88"));
        assert(aircraftUsageByArchType.containsKey("ju52"));

        assert(!aircraftUsageByArchType.containsKey("hs129"));
        assert(!aircraftUsageByArchType.containsKey("fw190"));
        assert(!aircraftUsageByArchType.containsKey("yak"));
        assert(!aircraftUsageByArchType.containsKey("il2"));
        assert(!aircraftUsageByArchType.containsKey("fw190d"));
        assert(!aircraftUsageByArchType.containsKey("me262"));
        
        int me109Weight = aircraftUsageByArchType.get("bf109");
        int me110Weight = aircraftUsageByArchType.get("bf110");
        int ju52Weight = aircraftUsageByArchType.get("ju52");
        int ju87Weight = aircraftUsageByArchType.get("ju87");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        int he111Weight = aircraftUsageByArchType.get("he111");
        assert(me109Weight > ju88Weight);
        assert(ju87Weight > he111Weight);
        assert(he111Weight > ju52Weight);
        assert(me110Weight == ju52Weight);
    }

    @Test
    public void testRussianReplacementArchTypes() throws PWCGException
    {
        Campaign earlyCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        removePlanesFromCampaign(earlyCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10101);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("il2"));
        assert(aircraftUsageByArchType.containsKey("i16"));
        assert(aircraftUsageByArchType.containsKey("lagg"));
        assert(aircraftUsageByArchType.containsKey("pe2"));
        assert(aircraftUsageByArchType.containsKey("mig3"));
        assert(aircraftUsageByArchType.containsKey("p40"));

        assert(!aircraftUsageByArchType.containsKey("yak"));
        assert(!aircraftUsageByArchType.containsKey("bf109"));
        assert(!aircraftUsageByArchType.containsKey("he111"));
        
        int il2Weight = aircraftUsageByArchType.get("il2");
        int i16Weight = aircraftUsageByArchType.get("i16");
        int laggWeight = aircraftUsageByArchType.get("lagg");
        int pe2Weight = aircraftUsageByArchType.get("pe2");
        int mig3Weight = aircraftUsageByArchType.get("mig3");
        int p40Weight = aircraftUsageByArchType.get("p40");
        assert(il2Weight > i16Weight);
        assert(mig3Weight > laggWeight);
        assert(pe2Weight > p40Weight);
    }
    
    @Test
    public void testItalianReplacementArchTypes() throws PWCGException
    {
        Campaign earlyCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        removePlanesFromCampaign(earlyCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("mc200"));
        assert(aircraftUsageByArchType.size() == 1);
    }
    
    @Test
    public void testGermanLateReplacementArchTypes() throws PWCGException
    {
        Campaign lateCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);        
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("ju88"));
        assert(aircraftUsageByArchType.containsKey("fw190"));
        assert(aircraftUsageByArchType.containsKey("fw190d"));
        assert(aircraftUsageByArchType.containsKey("me262"));
        assert(aircraftUsageByArchType.containsKey("ju52"));
        assert(aircraftUsageByArchType.containsKey("bf110"));        
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(aircraftUsageByArchType.containsKey("ju87"));
        assert(aircraftUsageByArchType.containsKey("hs129"));        
        
        int me109Weight = aircraftUsageByArchType.get("bf109");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        
        assert(me109Weight > ju88Weight);
    }

    @Test
    public void testAmericanReplacementArchTypes() throws PWCGException
    {
        Campaign lateCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);        
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(BoSServiceManager.USAAF);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("p47"));
    }

    @Test
    public void testBritishReplacementArchTypes() throws PWCGException
    {
        Campaign lateCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);        
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(BoSServiceManager.RAF);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("spitfire"));
    }
}
