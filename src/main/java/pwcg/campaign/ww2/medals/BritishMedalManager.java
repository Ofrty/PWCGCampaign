package pwcg.campaign.ww2.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class BritishMedalManager extends BoSMedalManager
{
    public static int PILOTS_BADGE = 1;

    public static int DFC = 2;
    public static int DFC_BAR_1 = 3;
    public static int DFC_BAR_2 = 4;
    public static int DSO = 5;
    public static int DSO_BAR = 6;
    public static int VC = 9;

    public static int WOUND_STRIPE = 20;

    BritishMedalManager(Campaign campaign)
    {
        super(campaign);

        medals.put(PILOTS_BADGE, new Medal("Pilots Badge", "gb_pb.jpg"));
        medals.put(DFC, new Medal("Distinguished Flying Cross", "gb_dfc.jpg"));
        medals.put(DFC_BAR_1, new Medal("Distinguished Flying Cross With Bar", "gb_dfc_bar.jpg"));
        medals.put(DFC_BAR_2, new Medal("Distinguished Flying Cross With 2 Bars", "gb_dfc_bar2.jpg"));
        medals.put(DSO, new Medal("Distinguished Service Order", "gb_dso.jpg"));
        medals.put(DSO_BAR, new Medal("Distinguished Service Order With Bar", "gb_dso_bar.jpg"));
        medals.put(VC, new Medal("Victoria Cross", "gb_vc.jpg"));

        medals.put(WOUND_STRIPE, new Medal("Wound Stripe", "gb_WoundChev.jpg"));
    }

    protected Medal awardWings(SquadronMember pilot)
    {
        if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }

        return null;
    }

    public Medal getWoundedAward(SquadronMember pilot, ArmedService service)
    {
        return medals.get(WOUND_STRIPE);
    }

    public Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 5 && !hasMedal(pilot, medals.get(DFC)))
        {
            return medals.get(DFC);
        }

        if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 10 && hasMedal(pilot, medals.get(DFC)) && !hasMedal(pilot, medals.get(DFC_BAR_1)))
        {
            if (victoriesThisMission >= 1)
            {
                return medals.get(DFC_BAR_1);
            }
        }

        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 15) && !hasMedal(pilot, medals.get(DSO)))
        {
            return medals.get(DSO);
        }

        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 25) && !hasMedal(pilot, medals.get(DSO_BAR)))
        {
            return medals.get(DSO_BAR);            
        }

        if (!hasMedal(pilot, medals.get(VC)))
        {
            if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 40)
            {
                if (victoriesThisMission >= 3)
                {
                    return medals.get(VC);
                }
            }
            if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 30)
            {
                if (victoriesThisMission >= 5)
                {
                    return medals.get(VC);
                }
            }
        }

        return null;
    }

    public Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        if (pilot.getGroundVictories().size() >= 15 && !hasMedal(pilot, medals.get(DFC)))
        {
            return medals.get(DFC);
        }

        if (pilot.getGroundVictories().size() >= 25 && !hasMedal(pilot, medals.get(DFC_BAR_1)))
        {
            if (victoriesThisMission >= 1)
            {
                return medals.get(DFC_BAR_1);
            }
        }

        if (pilot.getGroundVictories().size() >= 50 && !hasMedal(pilot, medals.get(DSO)))
        {
            return medals.get(DSO);
        }

        if (pilot.getGroundVictories().size() >= 75 && !hasMedal(pilot, medals.get(DSO_BAR)))
        {
            return medals.get(DSO_BAR);            
        }

        if (pilot.getGroundVictories().size() >= 100 && !hasMedal(pilot, medals.get(VC)))
        {
            return medals.get(VC);            
        }

        return awardFighter(pilot, service, victoriesThisMission);
    }
}