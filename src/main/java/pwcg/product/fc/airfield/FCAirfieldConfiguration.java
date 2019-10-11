package pwcg.product.fc.airfield;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.FCAirfieldDescriptorIOJson;
import pwcg.core.exception.PWCGException;

public class FCAirfieldConfiguration implements IAirfieldConfiguration
{
    public Map<String, IAirfield> configure (String mapName) throws PWCGException
    {
        Map<String, IAirfield> airfields = new TreeMap<String, IAirfield>();
        
        airfields.clear();

        String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
        AirfieldDescriptorSet airfieldDescriptors = FCAirfieldDescriptorIOJson.readJson(pwcgInputDir, AIRFIELD_LOCATION_FILE_NAME);
        for (FCAirfield.AirfieldDescriptor desc : airfieldDescriptors.locations)
        {
            FCAirfield field = new FCAirfield();
            field.initializeAirfieldFromDescriptor(desc);
            airfields.put(desc.getName(), field);
        }

        return airfields;
    }

    static public class AirfieldDescriptorSet
    {
        public String locationSetName = "";
        public List <FCAirfield.AirfieldDescriptor> locations = new ArrayList<>();
    }
}