package pwcg.testutils;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadmember.VictoryEntity;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelTestHelper
{

    public static void addVictories (SquadronMember squadronMember, Date date, int numVictories) throws PWCGException
    {
        for (int i = 0; i < numVictories; ++i)
        {
            VictoryEntity victim = new VictoryEntity();
            VictoryEntity victor = new VictoryEntity();
            
            Victory victory = new Victory();
            victory.setCrashedInSight(true);
            victory.setDate(date);
            victory.setLocation("Somewhere");
            victory.setVictim(victim);
            victory.setVictor(victor);
            
            squadronMember.addVictory(victory);
        }
    }
    
    public static SquadronMember getSquadronMemberByRank(Campaign campaign, String rank) throws PWCGException
    {
        SquadronMember selectedAiSquadMember = null;
        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        for (SquadronMember aiSquadMember : squadronMembers.getSquadronMemberList())
        {
            System.out.println("Nanme: " + aiSquadMember.getNameAndRank() + "  from " + aiSquadMember.getSquadronId());
            if (aiSquadMember.getRank().equals(rank))
            {
                if (!(aiSquadMember instanceof Ace))
                {
                    selectedAiSquadMember = aiSquadMember;
                }
            }
        }

        return selectedAiSquadMember;
    }

}
