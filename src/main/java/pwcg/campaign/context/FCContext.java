package pwcg.campaign.context;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.product.fc.plane.payload.FCPayloadFactory;

public class FCContext extends PWCGContextBase implements IPWCGContextManager
{
    protected FCContext()
    {
        campaignStartDates.add("01/08/1917");
        campaignStartDates.add("01/09/1917");
        campaignStartDates.add("01/10/1917");
        campaignStartDates.add("01/11/1917");
        campaignStartDates.add("01/12/1917");
        campaignStartDates.add("01/01/1918");
        campaignStartDates.add("01/02/1918");
        campaignStartDates.add("01/03/1918");
        campaignStartDates.add("01/04/1918");
        campaignStartDates.add("01/05/1918");
        campaignStartDates.add("01/06/1918");
        campaignStartDates.add("01/07/1918");
        campaignStartDates.add("01/08/1918");
        campaignStartDates.add("01/09/1918");
        campaignStartDates.add("01/10/1918");
    }

    @Override
    protected void initialize() throws PWCGException  
    {
        PWCGMap arrasMap = PWCGMapFactory.getMap(FrontMapIdentifier.ARRAS_MAP);
        
        pwcgMaps.put(arrasMap.getMapIdentifier(), arrasMap);

        super.initialize();
    }

    @Override
    public void initializeMap() throws PWCGException  
    {
        changeContext(FrontMapIdentifier.ARRAS_MAP);
    }

    @Override
    public IPayloadFactory getPayloadFactory() throws PWCGException  
    {
        return new FCPayloadFactory();
    }

    @Override
    public PWCGDirectoryProductManager getDirectoryManager()
    {
        return new PWCGDirectoryProductManager(PWCGProduct.FC);
    }

    @Override
    public IPlaneMarkingManager getPlaneMarkingManager()
    {
         return new IPlaneMarkingManager() {
            @Override
            public void allocatePlaneIdCode(Campaign campaign, int squadronId, Equipment equipment, EquippedPlane equippedPlane) throws PWCGException {
            }

            @Override
            public String determineDisplayMarkings(Campaign campaign, EquippedPlane equippedPlane) throws PWCGException
            {
                return Integer.toString(equippedPlane.getSerialNumber());
            }

            @Override
            public void writeTacticalCodes(BufferedWriter writer, Campaign campaign, PlaneMcu equippedPlane) throws PWCGException
            {
            }
        };
    }
}
