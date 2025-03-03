package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class SquadronRoleSet
{
    private List<SquadronRolePeriod> squadronRolePeriods = new ArrayList<>();

    public Role selectRoleForMission(Date date) throws PWCGException 
    {
        Role selectedRole = Role.ROLE_NONE;
        SquadronRolePeriod squadronRoleForDate = selectRoleSetByDate(date);
        if (squadronRoleForDate != null)
        {
            selectedRole = selectRoleFromSet(squadronRoleForDate);
        }
        else
        {
            throw new PWCGException("No role found for squadron on date " + DateUtils.getDateStringDashDelimitedYYYYMMDD(date));
        }
        
        return selectedRole;
    }
    
    public Role selectSquadronPrimaryRole(Date date) throws PWCGException 
    {
        SquadronRolePeriod squadronRoleForDate = selectRoleSetByDate(date);
        Role selectedRole = selectRoleByWeight(squadronRoleForDate);

        return selectedRole;
    }
    
    private Role selectRoleFromSet(SquadronRolePeriod squadronRole) throws PWCGException 
    {
        Role selectedRole = Role.ROLE_NONE;
        
        int totalWeight = 0;
        for (SquadronRoleWeight squadronRoleWeight : squadronRole.getWeightedRoles())
        {
            totalWeight += squadronRoleWeight.getWeight();
        }
        
        int evaluatedWeight = 0;
        int roll = RandomNumberGenerator.getRandom(totalWeight);
        for (SquadronRoleWeight squadronRoleWeight : squadronRole.getWeightedRoles())
        {
            evaluatedWeight += squadronRoleWeight.getWeight();
            if (roll <= evaluatedWeight)
            {
                selectedRole = squadronRoleWeight.getRole();
                break;
            }
        }
        
        return selectedRole;
    }
    


    private Role selectRoleByWeight(SquadronRolePeriod squadronRole) throws PWCGException 
    {
        Role selectedRole = Role.ROLE_NONE;
        
        int heaviestWeight = 0;
        for (SquadronRoleWeight squadronRoleWeight : squadronRole.getWeightedRoles())
        {
            if (squadronRoleWeight.getWeight() > heaviestWeight)
            {
                heaviestWeight = squadronRoleWeight.getWeight();
                selectedRole = squadronRoleWeight.getRole();
            }
        }
         
        return selectedRole;
    }
    
    private SquadronRolePeriod selectRoleSetByDate(Date date) throws PWCGException 
    {
        SquadronRolePeriod rolesForPeriod = null;
        for (SquadronRolePeriod squadronRole : squadronRolePeriods)
        {
            Date startDate = squadronRole.getStartDate();
            Date endDate = squadronRole.getEndDate();

            if (date.before(startDate))
            {
            }
            else if (date.after(endDate))
            {
            }
            else
            {
                rolesForPeriod = squadronRole;
            }
        }
        
        if (rolesForPeriod == null)
        {
            throw new PWCGException("No roles for date range " +  date);
        }

        return rolesForPeriod;
    }
    
    public boolean isSquadronThisPrimaryRole (Date date, Role requestedRole) throws PWCGException 
    {
        SquadronRolePeriod squadronRoleForDate = selectRoleSetByDate(date);
        Role primaryRole = selectRoleByWeight(squadronRoleForDate);
        return (requestedRole == primaryRole);
    }

    public boolean isSquadronThisRole (Date date, Role requestedRole) throws PWCGException 
    {
        SquadronRolePeriod squadronRoleForDate = selectRoleSetByDate(date);
        for (SquadronRoleWeight squadronRoleWeight : squadronRoleForDate.getWeightedRoles())
        {
            if (squadronRoleWeight.getRole() == requestedRole)
            {
                return true;
            }
        }
        
        return false;
    }

    public List<SquadronRolePeriod> getSquadronRolePeriods()
    {
        return squadronRolePeriods;
    }
}
