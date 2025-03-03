
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.utils.LCIndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.objective.MissionObjectiveFactory;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

public class McuIcon extends BaseFlightMcu
{

    private int enabled = 1;
    private int lCName = 0;
    private int lCDesc = 0;
    private int rColor = 255;
    private int gColor = 255;
    private int bColor = 255;
    private McuIconIdType iconId = McuIconIdType.ICON_ID_NORMAL;
    private McuIconLineType lineType = McuIconLineType.ICON_LINE_TYPE_NONE;
    private List<Coalition> coalitions = new ArrayList<Coalition>();

    public McuIcon(String iconName, String iconText, Side side)
    {
        super();

        setName(iconName);
        setDesc(iconName);

        MissionStringHandler.getInstance().registerMissionText(lCName, iconText);
        coalitions.add(CoalitionFactory.getCoalitionBySide(side));
    }

    public McuIcon(String iconName, String iconText)
    {
        super();

        setName(iconName);
        setDesc(iconName);

        MissionStringHandler.getInstance().registerMissionText(lCName, iconText);
        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.ALLIED));
        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.AXIS));
    }

    public McuIcon(McuWaypoint waypoint, Side side)
    {
        super();
        position = waypoint.getPosition().copy();

        iconId = McuIconIdType.ICON_ID_WAYPOINT;

        setName(waypoint.getName());

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePosition1())
        {
            rColor = 0;
            gColor = 0;
            bColor = 0;

            lineType = McuIconLineType.ICON_LINE_TYPE_POSITION1;

            setDesc(waypoint.getName() + "<routespeed>" + waypoint.getSpeed() + "</routespeed>");
        }
        else
        {
            setDesc(waypoint.getName());
        }

        coalitions.add(CoalitionFactory.getCoalitionBySide(side));
    }

    public McuIcon(WaypointAction action, MissionPoint missionPoint, Side side)
    {
        super();
        if (action == WaypointAction.WP_ACTION_TAKEOFF)
        {
            createIconTakeoff(missionPoint, side);
        }
        else if (action == WaypointAction.WP_ACTION_ATTACK)
        {
            createIconTarget(missionPoint, side);
        }
        else
        {
            createIconLanding(missionPoint, side);
        }
    }

    private void createIconTarget(MissionPoint target, Side side) {
        position = target.getPosition();

        iconId = McuIconIdType.ICON_ID_ACTION_POINT;

        setName("Target");
        setDesc("Target");

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePosition1()) {
            rColor = 0;
            gColor = 0;
            bColor = 0;

            lineType = McuIconLineType.ICON_LINE_TYPE_POSITION1;
        }

        MissionStringHandler.getInstance().registerMissionText(lCName, name);
        coalitions.add(CoalitionFactory.getCoalitionBySide(side));
    }

    private void createIconLanding(MissionPoint landing, Side side)
    {
        position = landing.getPosition();
        iconId = McuIconIdType.ICON_ID_LAND;
        setName("Land");
        setDesc("Land");

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePosition1())
        {
            rColor = 0;
            gColor = 0;
            bColor = 0;

            lineType = McuIconLineType.ICON_LINE_TYPE_POSITION1;
        }

        coalitions.add(CoalitionFactory.getCoalitionBySide(side));
    }

    public McuIcon(GroundUnitCollection balloon, Side side) throws PWCGException
    {
        super();

        this.iconId = McuIconIdType.ICON_ID_ENEMY_BALLOON;
        setName(FCPlaneAttributeMapping.BALLOON.getPlaneType());
        setDesc(FCPlaneAttributeMapping.BALLOON.getPlaneType());
        position = balloon.getPosition().copy();

        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.ALLIED));
        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.AXIS));
    }

    public McuIcon(Coordinate arrowPosition, double angle, Side side)
    {
        super();

        this.iconId = McuIconIdType.ICON_ID_NORMAL;
        this.lineType = McuIconLineType.ICON_LINE_TYPE_ARROW;
        setName("");
        setDesc("Assault");
        position = arrowPosition.copy();
        orientation = new Orientation(angle);
        if (side == Side.ALLIED)
        {
            rColor = 177;
            gColor = 107;
            bColor = 96;
        }
        else
        {
            rColor = 98;
            gColor = 119;
            bColor = 152;
        }

        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.ALLIED));
        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.AXIS));
    }


    public McuIcon(Airfield airfield, Side side)
    {
        super();

        this.iconId = McuIconIdType.ICON_ID_AIRFIELD;
        setName(airfield.getName());
        setDesc(airfield.getName());
        position = airfield.getPosition().copy();

        coalitions.add(CoalitionFactory.getCoalitionBySide(side));
    }

    public McuIcon(FrontLinePoint frontLinePoint)
    {
        setName("");
        setDesc("");

        position = frontLinePoint.getPosition().copy();
        this.lineType = McuIconLineType.ICON_LINE_TYPE_SECTOR1;

        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.ALLIED));
        coalitions.add(CoalitionFactory.getCoalitionBySide(Side.AXIS));
    }

    public McuIcon(IFlight flight, Date date) throws PWCGException {
        super();
        position = flight.getFlightHomePosition().copy();
        position.setXPos(position.getXPos() + 5000);
        iconId = McuIconIdType.ICON_ID_FREE_FLIGHT;
        setName(flight.getSquadron().determineDisplayName(date));
        setDesc(MissionObjectiveFactory.formMissionObjective(flight, date));

        coalitions.add(CoalitionFactory.getCoalitionBySide(flight.getFlightInformation().getCountry().getSide()));
    }
    
    private void createIconTakeoff(MissionPoint takeoff, Side side)
    {
        position = takeoff.getPosition();
        iconId = McuIconIdType.ICON_ID_TAKEOFF;
        setName("Take Off");
        setDesc("Take Off");

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePosition1())
        {
            rColor = 0;
            gColor = 0;
            bColor = 0;

            lineType = McuIconLineType.ICON_LINE_TYPE_POSITION1;
        }

        coalitions.add(CoalitionFactory.getCoalitionBySide(side));
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("MCU_Icon");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            super.write(writer);

            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();
            writer.write("  LCName = " + lCName + ";");
            writer.newLine();
            writer.write("  LCDesc = " + lCDesc + ";");
            writer.newLine();
            writer.write("  RColor = " + rColor + ";");
            writer.newLine();
            writer.write("  GColor = " + gColor + ";");
            writer.newLine();
            writer.write("  BColor = " + bColor + ";");
            writer.newLine();
            writer.write("  IconId = " + iconId.iconLineIdValue + ";");
            writer.newLine();
            writer.write("  LineType = " + lineType.iconLineTypeValue + ";");
            writer.newLine();

            CoalitionWriter.writeGenericCoalition(writer, coalitions);

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public void setColorBlue()
    {
        rColor = 0;
        gColor = 0;
        bColor = 255;
    }

    public void setColorRed()
    {
        rColor = 255;
        gColor = 0;
        bColor = 0;
    }

    public int getEnabled()
    {
        return enabled;
    }

    public void setEnabled(int enabled)
    {
        this.enabled = enabled;
    }

    public void setLineType(McuIconLineType lineType)
    {
        this.lineType = lineType;
    }

    public int getlCName()
    {
        return lCName;
    }

    public void setlCName(int lCName)
    {
        this.lCName = lCName;
    }

    public int getlCDesc()
    {
        return lCDesc;
    }

    public void setlCDesc(int lCDesc)
    {
        this.lCDesc = lCDesc;
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        if (lCName == 0)
            lCName = LCIndexGenerator.getInstance().getNextIndex();

        MissionStringHandler.getInstance().registerMissionText(lCName, name);
    }

    @Override
    public void setDesc(String desc) {
        super.setDesc(desc);

        if (lCDesc == 0)
            lCDesc = LCIndexGenerator.getInstance().getNextIndex();

        MissionStringHandler.getInstance().registerMissionText(lCDesc, desc);
    }

}
