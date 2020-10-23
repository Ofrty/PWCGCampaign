package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointTriggered implements makeActivatedSubtitle
{
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;
    private VirtualWaypointPlanes vwpPlanes;

    private McuFormation formation = new McuFormation();
    private McuActivate activate = new McuActivate();

    private McuTimer activateTimer = new McuTimer();
    private McuTimer formationTimer = new McuTimer();
    private McuTimer waypointTimer = new McuTimer();
    private McuTimer escortTimer = new McuTimer();
    private McuTimer deleteUpstreamPlanesTimer = new McuTimer();
    private int vwpIdentifier = 1;
    private int index = IndexGenerator.getInstance().getNextIndex();

    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    public VirtualWaypointTriggered(IFlight flight, VirtualWayPointCoordinate vwpCoordinate, VirtualWaypointPlanes vwpPlanes, int vwpIdentifier)
    {
        this.flight = flight;
        this.vwpCoordinate = vwpCoordinate;
        this.vwpPlanes = vwpPlanes;
        this.vwpIdentifier = vwpIdentifier;
    }

    public void build() throws PWCGException
    {
        buildMcus();
        makeSubtitles();
        createTargetAssociations();
        createObjectAssociations();
    }

    private void buildMcus()
    {
        activate.setPosition(vwpCoordinate.getPosition().copy());
        activate.setOrientation(vwpCoordinate.getOrientation().copy());
        activate.setName("Activate");
        activate.setDesc("Activate");
        
        activateTimer.setTimer(1);
        activateTimer.setPosition(vwpCoordinate.getPosition().copy());
        activateTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        activateTimer.setName("Activate Timer");
        activateTimer.setDesc("Activate Timer");

        waypointTimer.setTimer(1);
        waypointTimer.setPosition(vwpCoordinate.getPosition().copy());
        waypointTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        waypointTimer.setName("Activate WP Timer");
        waypointTimer.setDesc("Activate WP Timer");

        escortTimer.setTimer(1);
        escortTimer.setPosition(vwpCoordinate.getPosition().copy());
        escortTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        escortTimer.setName("Escort Timer");
        escortTimer.setDesc("Escort Timer");

        formationTimer.setPosition(vwpCoordinate.getPosition().copy());
        formationTimer.setName("Formation Timer");
        formationTimer.setDesc("Formation Timer");
        
        deleteUpstreamPlanesTimer.setTimer(3);
        deleteUpstreamPlanesTimer.setPosition(vwpCoordinate.getPosition().copy());
        deleteUpstreamPlanesTimer.setName("Delete Upstream Planes Timer");
        deleteUpstreamPlanesTimer.setDesc("Delete Upstream Planes Timer");

        formation.setPosition(vwpCoordinate.getPosition().copy());
    }

    public void link(VirtualWaypointDeactivateNextVwp vwpDeactivateNextVwp)
    {
        escortTimer.setTarget(vwpDeactivateNextVwp.getEntryPoint());
    }

    private void makeSubtitles() throws PWCGException
    {
        Coordinate subtitlePosition = vwpCoordinate.getPosition().copy();
        McuSubtitle activatedSubtitle = McuSubtitle.makeActivatedSubtitle("VWP Activate: " + vwpIdentifier + " " + vwpPlanes.getLeadActivatePlane().getName(), subtitlePosition);
        activateTimer.setTarget(activatedSubtitle.getIndex());
        subTitleList.add(activatedSubtitle);
    }

    private void createTargetAssociations()
    {
        activateTimer.setTarget(activate.getIndex());
        formationTimer.setTarget(formation.getIndex());
        
        int wpIndex = vwpCoordinate.getWaypointIdentifier(flight);
        waypointTimer.setTarget(wpIndex);

        activateTimer.setTarget(formationTimer.getIndex());
        formationTimer.setTarget(waypointTimer.getIndex());
        waypointTimer.setTarget(escortTimer.getIndex());
        escortTimer.setTarget(deleteUpstreamPlanesTimer.getIndex());
    }

    private void createObjectAssociations()
    {
        for (PlaneMcu plane : vwpPlanes.getAllPlanes())
        {
            activate.setObject(plane.getEntity().getIndex());
        }
        
        formation.setObject(vwpPlanes.getLeadActivatePlane().getEntity().getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Virtual WP Triggered\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Virtual WP Triggered\";");
            writer.newLine();
    
            activate.write(writer);
            formation.write(writer);
    
            activateTimer.write(writer);
            formationTimer.write(writer);
            waypointTimer.write(writer);
            escortTimer.write(writer);
            deleteUpstreamPlanesTimer.write(writer);
            
            McuSubtitle.writeSubtitles(subTitleList, writer);
    
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
    
    public void linkToEscort(int escortAcvtivateTarget)
    {
        escortTimer.setTarget(escortAcvtivateTarget);
    }

    public int getEntryPoint()
    {
        return activateTimer.getIndex();
    }

    public McuFormation getFormation()
    {
        return formation;
    }

    public McuActivate getActivate()
    {
        return activate;
    }

    public McuTimer getActivateTimer()
    {
        return activateTimer;
    }

    public McuTimer getFormationTimer()
    {
        return formationTimer;
    }

    public McuTimer getDeleteUpstreamPlanesTimer()
    {
        return deleteUpstreamPlanesTimer;
    }

    public McuTimer getWaypointTimer()
    {
        return waypointTimer;
    }
}