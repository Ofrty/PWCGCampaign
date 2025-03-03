package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingFlightChooser implements ActionListener
{
    private Mission mission;
    private IFlightChanged flightChanged;
    private JPanel flightChooserPanel;

    private ButtonGroup flightChooserButtonGroup = new ButtonGroup();
    private Map<Integer, ButtonModel> flightChooserButtonModels = new HashMap<>();

    public BriefingFlightChooser(Mission mission, IFlightChanged flightChanged)
    {
        this.mission = mission;
        this.flightChanged = flightChanged;
    }
    
    public void createBriefingSquadronSelectPanel() throws PWCGException
    {        
        JPanel flightChooserButtonPanelGrid = new JPanel(new GridLayout(0,1));
        flightChooserButtonPanelGrid.setOpaque(false);

        JLabel spacerLabel1 = PWCGButtonFactory.makeDummy();        
        flightChooserButtonPanelGrid.add(spacerLabel1);

        JLabel spacerLabel2 = PWCGButtonFactory.makeDummy();        
        flightChooserButtonPanelGrid.add(spacerLabel2);

        JLabel spacerLabel3 = PWCGButtonFactory.makeDummy();        
        flightChooserButtonPanelGrid.add(spacerLabel3);

        Map<Integer, Squadron> playerSquadronsInMission = new HashMap<>();
        for (IFlight playerFlight : mission.getMissionFlights().getPlayerFlights())
        {
            Squadron squadron = playerFlight.getSquadron();
            playerSquadronsInMission.put(squadron.getSquadronId(), squadron);
        }

        for (Squadron squadron : playerSquadronsInMission.values())
        {
            JRadioButton airLowDensity = PWCGButtonFactory.makeRadioButton(
                    squadron.determineDisplayName(mission.getCampaign().getDate()), 
                    "FlightChanged:" + squadron.getSquadronId(),
                    "Select squadron to change context", 
                    false, 
                    this,
                    ColorMap.CHALK_FOREGROUND);       
            flightChooserButtonPanelGrid.add(airLowDensity);
            ButtonModel model = airLowDensity.getModel();
            flightChooserButtonGroup.add(airLowDensity);
            flightChooserButtonModels.put(squadron.getSquadronId(), model);
        }

        flightChooserPanel = new JPanel(new BorderLayout());
        flightChooserPanel.setOpaque(false);
        flightChooserPanel.add(flightChooserButtonPanelGrid, BorderLayout.SOUTH);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        shapePanel.add(flightChooserButtonPanelGrid, BorderLayout.NORTH);
        flightChooserPanel.add(shapePanel, BorderLayout.CENTER);
    }

    public void setSelectedButton(int squadronId)
    {
        ButtonModel model = flightChooserButtonModels.get(squadronId);
        flightChooserButtonGroup.setSelected(model, true);
    }

    public JPanel getFlightChooserPanel()
    {
        return flightChooserPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            int index = action.indexOf(":");
            String selectedSquadronId = action.substring(index + 1);
            int squadronId = Integer.valueOf(selectedSquadronId);
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            
            setSelectedButton(squadronId);

            flightChanged.flightChanged(squadron);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }

    }
}
