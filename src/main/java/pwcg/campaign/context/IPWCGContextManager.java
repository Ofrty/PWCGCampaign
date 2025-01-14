package pwcg.campaign.context;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.staticobject.StaticObjectDefinitionManager;
import pwcg.campaign.newspapers.NewspaperManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.skin.SkinManager;
import pwcg.campaign.squadron.SkirmishProfileManager;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public interface IPWCGContextManager
{
    void changeContext(FrontMapIdentifier frontMapIdentifier) throws PWCGException;

    void setCampaign(Campaign campaign) throws PWCGException;

    Date getEarliestPwcgDate() throws PWCGException;

    Campaign getCampaign();

    PWCGMap getCurrentMap();

    PWCGMap getMapByMapName(String mapName);

    PWCGMap getMapByMapId(FrontMapIdentifier mapId);

    AceManager getAceManager();

    boolean isTestMode();

    void setTestMode(boolean testMode);

    List<String> getCampaignStartDates();

    List<PWCGMap> getAllMaps();

    Airfield getAirfieldAllMaps(String airfieldName);

    IPayloadFactory getPayloadFactory() throws PWCGException;

    void initializeMap() throws PWCGException;
    
    SquadronManager getSquadronManager();

    SkirmishProfileManager getSkirmishProfileManager();
    
    SkinManager getSkinManager();

    PlaneTypeFactory getPlaneTypeFactory();

    PWCGDirectoryProductManager getDirectoryManager();

    List<PWCGMap> getMaps();

    VehicleDefinitionManager getVehicleDefinitionManager();

    StaticObjectDefinitionManager getStaticObjectDefinitionManager();

    void setMapForCampaign(Campaign campaign) throws PWCGException;

    void configurePwcgMaps() throws PWCGException;

    void setMissionLogDirectory(String missionLogPath);

    String getMissionLogDirectory();

    void setCurrentMap(FrontMapIdentifier bodenplatteMap) throws PWCGException;

    NewspaperManager getNewspaperManager();
}
