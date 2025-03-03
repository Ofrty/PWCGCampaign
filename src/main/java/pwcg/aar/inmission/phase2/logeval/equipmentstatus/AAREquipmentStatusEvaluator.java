package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;

public class AAREquipmentStatusEvaluator
{
    private Campaign campaign;
    private AARLogEventData logEventData;
    private AARVehicleBuilder aarVehicleBuilder;

    public AAREquipmentStatusEvaluator(Campaign campaign, AARLogEventData logEventData, AARVehicleBuilder aarVehicleBuilder)
    {
        this.campaign = campaign;
        this.logEventData = logEventData;
        this.aarVehicleBuilder = aarVehicleBuilder;
    }

    public void determineFateOfPlanesInMission () throws PWCGException 
    {        
        for (LogPlane logPlane : aarVehicleBuilder.getLogPlanes().values())
        {
            IAType3 destroyedEventForPlane = logEventData.getDestroyedEvent(logPlane.getId());
            if (destroyedEventForPlane != null)
            {
                Airfield playerSquadronField = PWCGContext.getInstance().getSquadronManager().getSquadron(logPlane.getSquadronId()).determineCurrentAirfieldAnyMap(campaign.getDate());
                if (playerSquadronField != null)
                {
                    EquipmentSurvivalCalculator equipmentSurvivalCalculator = new EquipmentSurvivalCalculator(destroyedEventForPlane.getLocation(), playerSquadronField);
                    if (equipmentSurvivalCalculator.isPlaneDestroyed())
                    {
                        logPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
                    }
                }
            }
        }
    }
}
