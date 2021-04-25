package pwcg.campaign.plane;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public interface IPlaneMarkingManager {
    public void allocatePlaneIdCode(Campaign campaign, int squadronId, Equipment equipment, EquippedPlane equippedPlane) throws PWCGException;

    public String determineDisplayMarkings(Campaign campaign, EquippedPlane equippedPlane) throws PWCGException;

    public void writeTacticalCodes(BufferedWriter writer, Campaign campaign, PlaneMcu equippedPlane) throws PWCGException;
}
