package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.ShippingLanes;
import pwcg.core.exception.PWCGException;

public class ShippingLaneIOJson 
{
	public static ShippingLanes readJson(String mapName) throws PWCGException, PWCGException
	{
		JsonObjectReader<ShippingLanes> jsonReader = new JsonObjectReader<>(ShippingLanes.class);
		ShippingLanes shippingLanes = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "SeaLanes.json");
		return shippingLanes;
	}
}
