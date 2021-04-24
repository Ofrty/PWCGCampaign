package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.GuiMissionInitiator;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class BriefingCoopPersonaChooser extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private CoopPersonaChooserPanel coopPersonaAccept;
    private JPanel coopPersonaErrorPanel;
    private List<String> errorMessages = new ArrayList<>();
    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Campaign campaign;
    private JButton missionButton;
    private MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

    public BriefingCoopPersonaChooser(Campaign campaign, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.campaignHomeGuiBriefingWrapper = campaignHomeGuiBriefingWrapper;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingCoopPersonaChooser);
            this.setImageFromName(imagePath);

        	coopPersonaAccept = new CoopPersonaChooserPanel(campaign, this);
            coopPersonaAccept.makePanels();
            this.add(BorderLayout.CENTER, coopPersonaAccept);
            this.add(BorderLayout.WEST, makeNavigatePanel());
            buildErrorPanel();
            evaluateErrors();
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void buildErrorPanel() throws PWCGException 
    {
        coopPersonaErrorPanel = new JPanel(new GridLayout(0, 2));
        coopPersonaErrorPanel.setOpaque(false);
        coopPersonaAccept.add(BorderLayout.SOUTH, coopPersonaErrorPanel);
	}
    
    public void evaluateErrors() throws PWCGException
    {
    	errorMessages.clear();
    	coopPersonaErrorPanel.removeAll();

    	formErrorMessages();
    	
		addBlankErrorLine();
    	addErrorMessages();    	
		addBlankErrorLine();
		
		if (errorMessages.isEmpty())
		{
			missionButton.setEnabled(true);
		}
		else
		{
			missionButton.setEnabled(false);
		}

    	coopPersonaErrorPanel.revalidate();
    }

	private void formErrorMessages() throws PWCGException 
	{
		List<Integer> selectedCoopPersonas = coopPersonaAccept.getAcceptedCoopPersonas();
    	if (selectedCoopPersonas.isEmpty())
    	{
            errorMessages.add("No pilots selected for mission");
    	}
    	
    	CoopUserManager coopUserManager = CoopUserManager.getIntance();
    	Map <String, Integer> coopPersonasByCoopUser = new HashMap<>();
    	for (Integer coopPersona : selectedCoopPersonas)
    	{
    	    CoopUser coopUser = coopUserManager.getCoopUserForSquadronMember(campaign.getName(), coopPersona);
            if (coopPersonasByCoopUser.containsKey(coopUser.getUsername()))
            {
                errorMessages.add("More than one pilot in mission for player " + coopUser.getUsername());
            }
    		else
    		{
    			coopPersonasByCoopUser.put(coopUser.getUsername(), coopPersona);
    		}
    	}
    	
    	SquadronMember referencePlayer = campaign.findReferencePlayer();
        boolean referencePlayerIncluded = false;
        for (int coopPersona : selectedCoopPersonas)
        {
            if (coopPersona == referencePlayer.getSerialNumber())
            {
                referencePlayerIncluded = true;
            }
        }
        
        if (!referencePlayerIncluded)
        {
            errorMessages.add("Reference player " + referencePlayer.getNameAndRank() + " must be included in the mission");
        }
	}

	private void addErrorMessages() throws PWCGException 
	{
	    Font font = PWCGMonitorFonts.getPrimaryFontLarge();
    	
        JLabel spacer = new JLabel("   ");
        spacer.setFont(font);
        coopPersonaErrorPanel.add(spacer);

		for (String errorMessage : errorMessages)
    	{
    		JLabel errorLabel = new JLabel(errorMessage);
    		errorLabel.setFont(font);
    		coopPersonaErrorPanel.add(errorLabel);
    	}

        JLabel spacer2 = new JLabel("   ");
        spacer2.setFont(font);
        coopPersonaErrorPanel.add(spacer2);
	}

	private void addBlankErrorLine() throws PWCGException 
	{
	    Font font = PWCGMonitorFonts.getPrimaryFontLarge();
    	
	    for (int i = 0; i < 3; ++i)
	    {
	        JLabel spacer = new JLabel("   ");
	        spacer.setFont(font);
	        coopPersonaErrorPanel.add(spacer);
	    }
	}

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        missionButton = makeMenuButton("Coop Mission", "CampCoopMission", "Create a coop mission");
        buttonPanel.add(missionButton);

        JButton scrubButton = makeMenuButton("Scrub Mission", "ScrubMission", "Scrub this mission");
        buttonPanel.add(scrubButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    private JButton makeMenuButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CampCoopMission"))
            {
            	List<SquadronMember> selectedCoopPersonas = coopPersonaAccept.getAcceptedSquadronMembers();
            	participatingPlayers.addSquadronMembers(selectedCoopPersonas);
            	
             	GuiMissionInitiator missionInitiator = new GuiMissionInitiator(campaign, participatingPlayers);
                Mission mission = missionInitiator.makeMission(false);
                showBriefingMap(mission);
            }
            else if (action.equalsIgnoreCase("ScrubMission"))
            {
                scrubMission();
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void showBriefingMap(Mission mission) throws PWCGException 
    {
    	MusicManager.playMissionBriefingTheme();
    	SoundManager.getInstance().playSound("Typewriter.WAV");

        BriefingDescriptionScreen briefingMap = new BriefingDescriptionScreen(campaignHomeGuiBriefingWrapper, mission);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    private void scrubMission() throws PWCGException
    {
        Campaign campaign  = PWCGContext.getInstance().getCampaign();
        campaign.setCurrentMission(null);        
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }

}


