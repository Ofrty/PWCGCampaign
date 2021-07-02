package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARContextEventSequence;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OutOfMissionAAALossCalculator
{
    private Campaign campaign;
    private AARContext aarContext;
    private Map<Integer, SquadronMember> pilotsLostDueToAAA = new HashMap<>();
    private Map<Integer, LogPlane> planesLostDueToAAA = new HashMap<>();
    private OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator;

    public OutOfMissionAAALossCalculator (Campaign campaign, AARContext aarContext, OutOfMissionAAAOddsCalculator oddsShotDownByAAACalculator)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        this.oddsShotDownByAAACalculator = oddsShotDownByAAACalculator;
    }
    
    public void lostToAAA() throws PWCGException
    {
        SquadronMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(
                campaign, aarContext.getPreliminaryData().getCampaignMembersInMission());
        for (SquadronMember squadronMember : campaignMembersNotInMission.getSquadronMemberList())
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
            if (SquadronViability.isSquadronViable(squadron, campaign))
            {
                calculatePilotShotDownByAAA(squadronMember);
            }
        }
    }

    private void calculatePilotShotDownByAAA(SquadronMember squadronMember) throws PWCGException
    {
        if (OutOfMissionPilotSelector.shouldPilotBeEvaluated(campaign, squadronMember)) 
        {
            int oddsShotDown = oddsShotDownByAAACalculator.oddsShotDownByAAA(squadronMember);
            
            int shotDownDiceRoll = RandomNumberGenerator.getRandom(1000);
            if (shotDownDiceRoll <= oddsShotDown)
            {
                EquippedPlane planeShotDownByAAA = campaign.getEquipmentManager().destroyPlaneFromSquadron(squadronMember.getSquadronId(), campaign.getDate());

                LogPlane logPlane = new LogPlane(AARContextEventSequence.getNextOutOfMissionEventSequenceNumber());
                logPlane.initializeFromOutOfMission(campaign, planeShotDownByAAA, squadronMember);
                
                pilotsLostDueToAAA.put(squadronMember.getSerialNumber(), squadronMember);
                planesLostDueToAAA.put(planeShotDownByAAA.getSerialNumber(), logPlane);
            }
        }
    }

    public Map<Integer, SquadronMember> getPilotsLostDueToAAA()
    {
        return pilotsLostDueToAAA;
    }

    public Map<Integer, LogPlane> getPlanesLostDueToAAA()
    {
        return planesLostDueToAAA;
    }
}
