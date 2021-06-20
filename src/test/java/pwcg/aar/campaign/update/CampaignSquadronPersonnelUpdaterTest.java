package pwcg.aar.campaign.update;


import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase1.elapsedtime.OutOfMissionCommandChangeHandler;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronMemberPicker;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignSquadronPersonnelUpdaterTest
{
    private Map<Integer, SquadronMember> squadMembersKilled = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersCaptured = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersWounded = new HashMap<>();
    private Map<Integer, SquadronMember> acesKilled = new HashMap<>();
    
    private Campaign campaign;
    private AARContext aarContext;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        aarContext = new AARContext(campaign);
        aarContext.setNewDate(DateUtils.advanceTimeDays(campaign.getDate(), 3));

        squadMembersKilled.clear();
        squadMembersMaimed.clear();
        squadMembersCaptured.clear();
        squadMembersWounded.clear();
        acesKilled.clear();
    }
    
    @After
    public void reset() throws PWCGException
    {
        SquadronPersonnel personnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        for (SquadronMember squadronMember : personnel.getSquadronMembersWithAces().getSquadronMemberList())
        {
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
        }
    }

    @Test
    public void testSquadronMemberKilled() throws PWCGException
    {
        SquadronMember deadSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId()); 
        deadSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        squadMembersKilled.put(deadSquadronMember.getSerialNumber(), deadSquadronMember);
        
        for (SquadronMember squadronMember : squadMembersKilled.values())
        {
            aarContext.getCampaignUpdateData().getPersonnelLosses().addPersonnelKilled(squadronMember);
        }
        
        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(deadSquadronMember.getSerialNumber()); 
        assertTrue (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }

    @Test
    public void testSquadronMemberCaptured() throws PWCGException
    {
        SquadronMember capturedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        capturedSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        squadMembersCaptured.put(capturedSquadronMember.getSerialNumber(), capturedSquadronMember);
        
        for (SquadronMember squadronMember : squadMembersCaptured.values())
        {
            aarContext.getCampaignUpdateData().getPersonnelLosses().addPersonnelCaptured(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(capturedSquadronMember.getSerialNumber()); 
        assertTrue (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_CAPTURED);
    }

    @Test
    public void testSquadronMemberMaimed() throws PWCGException
    {
        SquadronMember maimedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId()); 
        Date recoveryDate = DateUtils.advanceTimeDays(campaign.getDate(), 21);
        maimedSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), recoveryDate);
        squadMembersMaimed.put(maimedSquadronMember.getSerialNumber(), maimedSquadronMember);
                
        for (SquadronMember squadronMember : squadMembersMaimed.values())
        {
            aarContext.getCampaignUpdateData().getPersonnelLosses().addPersonnelMaimed(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(maimedSquadronMember.getSerialNumber()); 
        assert (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }

    @Test
    public void testPlayerMemberMaimed() throws PWCGException
    {
        SquadronMember maimedPlayer = SquadronMemberPicker.pickPlayerSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId()); 
        squadMembersMaimed.put(maimedPlayer.getSerialNumber(), maimedPlayer);
        
        for (SquadronMember squadronMember : squadMembersMaimed.values())
        {
            aarContext.getCampaignUpdateData().getPersonnelLosses().addPersonnelMaimed(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(maimedPlayer.getSerialNumber()); 
        assert (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert(maimedPlayer.getRecoveryDate().after(campaign.getDate()));
    }

    @Test
    public void testPlayerBecomesCommander() throws PWCGException
    {
        SquadronMember commander = null;
        commander = getAiCommander();
        assert (commander != null);

        SquadronMember player = SquadronMemberPicker.pickPlayerSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId());

        IRankHelper iRank = RankFactory.createRankHelper();        
        String commandRank = iRank.getRankByService(0, player.determineService(campaign.getDate()));
        player.setRank(commandRank);
        
        OutOfMissionCommandChangeHandler commandChangeHandler = new OutOfMissionCommandChangeHandler(campaign);
        AARPersonnelLosses personnelLossesTransferHome = commandChangeHandler.replaceCommanderWithPlayer();
        
        for (SquadronMember squadronMember : personnelLossesTransferHome.getPersonnelTransferredHome().values())
        {
            aarContext.getReconciledOutOfMissionData().getPersonnelLossesOutOfMission().addPersonnelTransferredHome(squadronMember);;
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember commanderAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(commander.getSerialNumber()); 
        assert (commanderAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_TRANSFERRED);
        assert(commanderAfterUpdate.getInactiveDate().equals(campaign.getDate()));

        SquadronMember activeCommander = getAiCommander();
        assert (activeCommander == null);
    }

    private SquadronMember getAiCommander() throws PWCGException
    {
        SquadronMember commander = null;
        SquadronPersonnel squadronMembers = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronMembers.getSquadronMembers().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : activeSquadronMembers.getSquadronMemberList())
        {
            if (squadronMember.determineIsSquadronMemberCommander() && !squadronMember.isPlayer())
            {
                commander = squadronMember;
            }
        }
        return commander;
    }

    @Test
    public void testWoundedPilotHealed() throws PWCGException
    {
        SquadronMember woundedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign, SquadronTestProfile.ESC_103_PROFILE.getSquadronId()); 
        squadMembersWounded.put(woundedSquadronMember.getSerialNumber(), woundedSquadronMember);
        
        for (SquadronMember squadronMember : squadMembersWounded.values())
        {
            aarContext.getCampaignUpdateData().getPersonnelLosses().addPersonnelWounded(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(woundedSquadronMember.getSerialNumber()); 
        assert (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_WOUNDED);
        assert(woundedSquadronMember.getRecoveryDate().after(campaign.getDate()));
    }

    @Test
    public void testSquadronMemberTransferred() throws PWCGException
    {
        ArmedService armedService = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.ESC_103_PROFILE.getSquadronId()).determineServiceForSquadron(campaign.getDate());
        PersonnelReplacementsService serviceReplacements =  campaign.getPersonnelManager().getPersonnelReplacementsService(armedService.getServiceId());
        SquadronMember transferredSquadronMember = serviceReplacements.findReplacement(); 
        
        TransferRecord transferRecord = new TransferRecord(transferredSquadronMember, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        
        aarContext.getCampaignUpdateData().getResupplyData().getSquadronTransferData().addTransferRecord(transferRecord);

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(transferredSquadronMember.getSerialNumber()); 
        assertTrue (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE);
        
        assertTrue (squadronMemberAfterUpdate.getSquadronId() != SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
    }

    @Test
    public void testSquadronAceKilled() throws PWCGException
    {
        Ace guynemerInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101064); 
        acesKilled.put(101064, guynemerInCampaign);

        for (SquadronMember squadronMember : acesKilled.values())
        {
            aarContext.getCampaignUpdateData().getPersonnelLosses().addAcesKilled(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        Ace aceAfterUpdate = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101064); 
        assertTrue (aceAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }
    
    @Test
    public void testNonSquadronAceKilled() throws PWCGException
    {
        Ace vossInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101175); 
        acesKilled.put(101175, vossInCampaign);
        

        for (SquadronMember squadronMember : acesKilled.values())
        {
            aarContext.getCampaignUpdateData().getPersonnelLosses().addAcesKilled(squadronMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        Ace aceAfterUpdate = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101175); 
        assertTrue (aceAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }

}
