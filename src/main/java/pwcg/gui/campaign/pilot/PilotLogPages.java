package pwcg.gui.campaign.pilot;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadmember.VictoryDescription;
import pwcg.core.exception.PWCGException;

public class PilotLogPages
{
    private SquadronMember squadronMember;
    private PageSizeCalculator pageSizeCalculator = new PageSizeCalculator();
    private Map<Integer, StringBuffer> pages = new TreeMap<Integer, StringBuffer>();
    private Campaign campaign;

    public PilotLogPages(Campaign campaign, SquadronMember squadronMember)
    {
        this.campaign = campaign;
        this.squadronMember = squadronMember;
    }

    public void makePages() throws PWCGException
    {
        makePageOne();
        
        int pageCount = 2;
        if (squadronMember.getSquadronMemberVictories().getAirToAirVictoryCount() > 0)
        {
            pageCount = addVictoriesToPage(pageCount, squadronMember.getSquadronMemberVictories().getAirToAirVictories());
        }

        if (squadronMember.getSquadronMemberVictories().getGroundVictoryCount() > 0)
        {
            pageCount = addVictoriesToPage(pageCount, squadronMember.getSquadronMemberVictories().getTankVictories());
        }

        if (squadronMember.getSquadronMemberVictories().getGroundVictoryCount() > 0)
        {
            pageCount = addVictoriesToPage(pageCount, squadronMember.getSquadronMemberVictories().getGroundVictories());
        }
        
        return;
    }

    private void makePageOne() throws PWCGException
    {
        StringBuffer pageOneBuffer = new StringBuffer("Pilot Dossier:\n");
        
        String pilotName = squadronMember.getNameAndRank() + "\n";
        pageOneBuffer.append("\n" + pilotName);

        String pilotStatus = "Pilot Status: " + SquadronMemberStatus.pilotStatusToStatusDescription(squadronMember.getPilotActiveStatus()) + "\n";
        pageOneBuffer.append(pilotStatus);

        pageOneBuffer.append(squadronMember.skillAsString() + "\n");

        String pilotAirVictories = "Air Victories: " + squadronMember.getSquadronMemberVictories().getAirToAirVictoryCount() + "\n";
        pageOneBuffer.append(pilotAirVictories + "\n");

        String pilotTankVictories = "Tank Victories: " + squadronMember.getSquadronMemberVictories().getTankVictoryCount() + "\n";
        pageOneBuffer.append(pilotTankVictories + "\n");

        String pilotGroundVictories = "Ground Victories: " + squadronMember.getSquadronMemberVictories().getGroundVictoryCount() + "\n";
        pageOneBuffer.append(pilotGroundVictories + "\n");

        pages.put(1, pageOneBuffer);
    }
    
    private int addVictoriesToPage(int pageCount, List<Victory> victories) throws PWCGException
    {
        StringBuffer page = new StringBuffer("");

        for (Victory victory : victories)
        {
            VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            String logEntry = victoryDescriptionText  + "\n\n";

            if ( (countLines(page.toString()) + countLines(logEntry)) >= pageSizeCalculator.getLinesPerPage())
            {
                pages.put(pageCount, page);
                ++pageCount;
                page = new StringBuffer("");
            }

            page.append(logEntry);
        }
        
        pages.put(pageCount, page);
        
        ++pageCount;
        return pageCount;
    }

    private int countLines(String str)
    {
        String[] lines = str.split("\r\n|\r|\n");
        int calculatedLines = lines.length;
        
        for (String line : lines)
        {
            calculatedLines += calculateLinesNeededForThisString(line);
        }
        
        
        return  calculatedLines;
    }

    private int calculateLinesNeededForThisString(String line)
    {
        int extraLines = 0;
        if (line.length() > pageSizeCalculator.getCharsPerLine())
        {
            ++extraLines;
        }
        return extraLines;
    }

    public int getPageCount()
    {
        return pages.size();
    }

    public StringBuffer getPage(int pageNum)
    {
        return pages.get(pageNum);
    }

    
}
