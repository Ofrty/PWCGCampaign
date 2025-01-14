package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;

@RunWith(MockitoJUnitRunner.class)
public class LocationIOJsonTest
{
    @Test
    public void readJsonArrasTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        String directory = System.getProperty("user.dir") + "\\FCData\\Input\\Arras\\19170801\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Moscow\\19411001\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Stalingrad\\19421011\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Kuban\\19420601\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonBodenplatteTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Bodenplatte\\19440901\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }
}
