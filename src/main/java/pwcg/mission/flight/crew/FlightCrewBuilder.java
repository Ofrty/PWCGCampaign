package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberSorter;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;

public class FlightCrewBuilder
{
    private FlightInformation flightInformation;

    private Map <Integer, SquadronMember> assignedCrewMap = new HashMap <>();
    private Map <Integer, SquadronMember> unassignedCrewMap = new HashMap <>();
    
	public FlightCrewBuilder(FlightInformation flightInformation)
	{
        this.flightInformation = flightInformation;
	}

    public List<SquadronMember> createCrewAssignmentsForFlight(int numCrewNeeded) throws PWCGException 
    {
        CrewFactory crewFactory = new CrewFactory(flightInformation);
        unassignedCrewMap = crewFactory.createCrews();
        
        assignPlayersToCrew();
        assignAiPilotsToPlanes(numCrewNeeded);
        
        List<SquadronMember> sortedByRank = sortCrewsByRank();
        List<SquadronMember> finalCrewSequence = playerIsLead(sortedByRank);
        return finalCrewSequence;
    }

    private void assignPlayersToCrew() throws PWCGException
    {
        if (Squadron.isPlayerSquadron(flightInformation.getCampaign(), flightInformation.getSquadron().getSquadronId()))
        {
            List<SquadronMember> participatingPlayerCrews = new ArrayList<>();
            for (SquadronMember pilot : flightInformation.getFlightParticipatingPlayers())
            {
            	participatingPlayerCrews.add(pilot);
            }

            for (SquadronMember  participatingPlayerCrew : participatingPlayerCrews)
            {
                assignedCrewMap.put(participatingPlayerCrew.getSerialNumber(), participatingPlayerCrew);
                unassignedCrewMap.remove(participatingPlayerCrew.getSerialNumber());
            }
        }
    }

	private void assignAiPilotsToPlanes(int numCrewNeeded) throws PWCGException
    {
        while (assignedCrewMap.size() < numCrewNeeded)
        {
            List<Integer> unassignedAiCrewSerialNumbers = buildUnassignedAiCrewMembers();
            
            int crewIndex = RandomNumberGenerator.getRandom(unassignedAiCrewSerialNumbers.size());
            int selectedSerialNumber = unassignedAiCrewSerialNumbers.get(crewIndex);
            SquadronMember crewToAssign = unassignedCrewMap.get(selectedSerialNumber);
            assignedCrewMap.put(crewToAssign.getSerialNumber(), crewToAssign);
            unassignedCrewMap.remove(crewToAssign.getSerialNumber());
        }
    }
    
	private List<Integer> buildUnassignedAiCrewMembers()
	{
        List<Integer> unassignedAiCrewSerialNumbers = new ArrayList<>();
        List<Integer> unassignedCrewSerialNumbers = new ArrayList<>(unassignedCrewMap.keySet());
        for (int unassignedCrewSerialNumber : unassignedCrewSerialNumbers)
        {
        	if (shouldAssignAIPilot(unassignedCrewSerialNumber))
        	{
        		unassignedAiCrewSerialNumbers.add(unassignedCrewSerialNumber);
        	}
        }
        return unassignedAiCrewSerialNumbers;
	}
	
	private boolean shouldAssignAIPilot(int unassignedCrewSerialNumber)
	{
        if (SerialNumber.getSerialNumberClassification(unassignedCrewSerialNumber) == SerialNumberClassification.AI)
        {
            return true;
        }
        else if (SerialNumber.getSerialNumberClassification(unassignedCrewSerialNumber) == SerialNumberClassification.ACE)
        {
            return true;
        }
        else
        {
            return false;
        }
	}
	
    private List<SquadronMember> sortCrewsByRank() throws PWCGException
    {
        return SquadronMemberSorter.sortSquadronMembers(flightInformation.getCampaign(), assignedCrewMap);
    }

    private List<SquadronMember> playerIsLead(List<SquadronMember> sortedByRank)
    {
        List<SquadronMember> withPlayerAsLead = new ArrayList<>();
        if (FlightTypes.isPlayerLead(flightInformation.getFlightType()) && flightInformation.isPlayerFlight())
        {
            for (SquadronMember squadronMember : sortedByRank)
            {
                if (squadronMember.isPlayer())
                {
                    withPlayerAsLead.add(squadronMember);
                }
            }
            
            for (SquadronMember squadronMember : sortedByRank)
            {
                if (!squadronMember.isPlayer())
                {
                    withPlayerAsLead.add(squadronMember);
                }
            }
            
            return withPlayerAsLead;
        }
        return sortedByRank;
    }
}
