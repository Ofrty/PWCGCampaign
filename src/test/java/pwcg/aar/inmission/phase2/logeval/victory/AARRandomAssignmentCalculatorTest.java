package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.event.AType17;
import pwcg.aar.inmission.phase1.parse.event.AType3;
import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.aar.inmission.phase1.parse.event.IATypeBase;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class AARRandomAssignmentCalculatorTest
{
    private IAType17 waypoint;
    private IAType3 crash;
    private List<IATypeBase> chronologicalAType;
    
    @Mock
    private AARAreaOfCombat areaOfCombat;
    
    @Before
    public void setup()
    {
        chronologicalAType = new ArrayList<>();        
    }

    @Test
    public void testMarkTrueBecauseCloseToWaypoint () throws PWCGException
    {
        waypoint = new AType17("T:14605 AType:17 ID:129023 POS(97132,1781,133919)");
        crash = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(93132,0,133919)");

        chronologicalAType.add(waypoint);
        chronologicalAType.add(crash);
        
        AARRandomAssignmentCalculator randomAssignmentCalculator = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markedForAssignment = randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(chronologicalAType, "35839@1");
        
        assert(markedForAssignment == true);
    }

    @Test
    public void testMarkTrueBecauseCloseToCombat () throws PWCGException
    {
        waypoint = new AType17("T:14605 AType:17 ID:129023 POS(97132,1781,133919)");
        crash = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(90132,0,133919)");
        
        chronologicalAType.add(waypoint);
        chronologicalAType.add(crash);
        
        Mockito.when(areaOfCombat.isNearAreaOfCombat(ArgumentMatchers.<Coordinate>any())).thenReturn(true);

        AARRandomAssignmentCalculator randomAssignmentCalculator = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markedForAssignment = randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(chronologicalAType, "35839@1");
        
        assert(markedForAssignment == true);
    }

    @Test
    public void testMarkFalseBecauseTooFarAwayFromWaypoint () throws PWCGException
    {
        waypoint = new AType17("T:14605 AType:17 ID:129023 POS(97132,1781,133919)");
        crash = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(90132,0,133919)");
        
        chronologicalAType.add(waypoint);
        chronologicalAType.add(crash);
        
        AARRandomAssignmentCalculator randomAssignmentCalculator = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markedForAssignment = randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(chronologicalAType, "35839");
        
        assert(markedForAssignment == false);
    }

    @Test
    public void testMarkFalseBecauseVictimMismatch () throws PWCGException
    {
        waypoint = new AType17("T:14605 AType:17 ID:129023 POS(97132,1781,133919)");
        crash = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(93132,0,133919)");
        
        chronologicalAType.add(waypoint);
        chronologicalAType.add(crash);
        
        AARRandomAssignmentCalculator randomAssignmentCalculator = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markedForAssignment = randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(chronologicalAType, "99999");
        
        assert(markedForAssignment == false);
    }

}
