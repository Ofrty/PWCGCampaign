package pwcg.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CampaignIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class ConfigManagerCampaignTest
{
    private Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
    }
    
    @Test
    public void testGlobalConfigurationInitialize() throws PWCGException 
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        CampaignIOJson.writeJson(campaign);
        
        String campaignConfigDir = campaign.getCampaignPathAutoCreateDirectory() + "config\\";
        ConfigManagerCampaign campaignConfigManager = new ConfigManagerCampaign(campaignConfigDir);
        campaignConfigManager.initializeDefault();
        
        Map<String, ConfigSet> defaultConfigSets = campaignConfigManager.getDefaultCampaignConfigSets();
        Map<String, ConfigSet> initialConfigSetsFromFiles = campaignConfigManager.readInitialConfigSets();

        assertEquals(defaultConfigSets.size(), 14);
        assertEquals(initialConfigSetsFromFiles.size(), 17);
                
        boolean allConfigsOk = verifyConfigSets(defaultConfigSets, initialConfigSetsFromFiles);
        
        assertTrue(allConfigsOk);
    }
    
    private boolean verifyConfigSets(Map<String, ConfigSet> defaultConfigSets, Map<String, ConfigSet> initialConfigSetsFromFiles)
    {
        boolean allConfigsOk = true;
        
        for (String configSetId : defaultConfigSets.keySet())
        {
            ConfigSet defaultConfigSet = defaultConfigSets.get(configSetId);
            ConfigSet initialConfigSet = initialConfigSetsFromFiles.get(configSetId);
            boolean configSetOk =  verifyConfigSet(defaultConfigSet, initialConfigSet);
            if (!configSetOk)
            {
                allConfigsOk = false;
            }
        }
        
        return allConfigsOk;
    }

    private boolean verifyConfigSet(ConfigSet defaultConfigSet, ConfigSet initialConfigSet)
    {
        boolean configSetOk =  true;
        for (String defaultConfigName : defaultConfigSet.getConfigItemNames())
        {
            if (!initialConfigSet.hasConfigItem(defaultConfigName))
            {
                System.out.println("Missing from initial config set  " + defaultConfigSet.getConfigSetName() + " " + defaultConfigName);
                configSetOk = false;
            }
        }

        for (String initialConfigName : initialConfigSet.getConfigItemNames())
        {
            if (!defaultConfigSet.hasConfigItem(initialConfigName))
            {
                System.out.println("Missing from default config set " + defaultConfigSet.getConfigSetName() + " " + initialConfigName);
                configSetOk = false;
            }
        }
    
        return configSetOk;
    }
}
