package pwcg.aar.inmission.phase2.logeval;

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
import pwcg.aar.inmission.phase2.logeval.victory.AARAreaOfCombat;
import pwcg.aar.inmission.phase2.logeval.victory.AARRandomAssignmentCalculator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class AARRandomAssignmentCalculatorTest
{

    private List<IATypeBase> chronologicalAType = new ArrayList<>();
    
    @Mock
    private AARAreaOfCombat areaOfCombat;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        chronologicalAType.clear();
    }

    @Test
    public void testSetForRandomAssignmentBecauseCloseEnough() throws PWCGException
    {
        IAType17 waypoint1 = new AType17("T:14605 AType:17 ID:11111 POS(282341.281,941.709,62512.715)");
        IAType17 waypoint2 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown1 = new AType3("T:78230 AType:3 AID:22222 TID:99999 POS(232341.281,941.709,62512.715)");
        IAType17 waypoint3 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown2 = new AType3("T:78230 AType:3 AID:11111 TID:88888 POS(262341.281,941.709,62512.715)");
        
        chronologicalAType.add(waypoint1);
        chronologicalAType.add(waypoint2);
        chronologicalAType.add(somebodyWhoGotShotDown1);
        chronologicalAType.add(waypoint3);
        chronologicalAType.add(somebodyWhoGotShotDown2);

        AARRandomAssignmentCalculator randomAssignmentCalculatorTest = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markForRandomAssignment = randomAssignmentCalculatorTest.shouldBeMarkedForRandomAssignment(chronologicalAType, "88888@1");

        assert(markForRandomAssignment == true);
    }

    @Test
    public void testSetForRandomAssignmentBecauseInAreaOfCombat() throws PWCGException
    {
        IAType17 waypoint1 = new AType17("T:14605 AType:17 ID:11111 POS(282341.281,941.709,62512.715)");
        IAType17 waypoint2 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown1 = new AType3("T:78230 AType:3 AID:22222 TID:99999 POS(232341.281,941.709,62512.715)");
        IAType17 waypoint3 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown2 = new AType3("T:78230 AType:3 AID:11111 TID:88888 POS(232341.281,941.709,62512.715)");
        
        chronologicalAType.add(waypoint1);
        chronologicalAType.add(waypoint2);
        chronologicalAType.add(somebodyWhoGotShotDown1);
        chronologicalAType.add(waypoint3);
        chronologicalAType.add(somebodyWhoGotShotDown2);

        Mockito.when(areaOfCombat.isNearAreaOfCombat(ArgumentMatchers.<Coordinate>any())).thenReturn(true);
        AARRandomAssignmentCalculator randomAssignmentCalculatorTest = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markForRandomAssignment = randomAssignmentCalculatorTest.shouldBeMarkedForRandomAssignment(chronologicalAType, "88888@1");

        assert(markForRandomAssignment == true);
    }

    @Test
    public void testNotSetForRandomAssignmentBecauseTooFarAndNotInAreaOfCombat() throws PWCGException
    {
        IAType17 waypoint1 = new AType17("T:14605 AType:17 ID:11111 POS(282341.281,941.709,62512.715)");
        IAType17 waypoint2 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown1 = new AType3("T:78230 AType:3 AID:22222 TID:99999 POS(232341.281,941.709,62512.715)");
        IAType17 waypoint3 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown2 = new AType3("T:78230 AType:3 AID:11111 TID:88888 POS(232341.281,941.709,62512.715)");
        
        chronologicalAType.add(waypoint1);
        chronologicalAType.add(waypoint2);
        chronologicalAType.add(somebodyWhoGotShotDown1);
        chronologicalAType.add(waypoint3);
        chronologicalAType.add(somebodyWhoGotShotDown2);

        AARRandomAssignmentCalculator randomAssignmentCalculatorTest = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markForRandomAssignment = randomAssignmentCalculatorTest.shouldBeMarkedForRandomAssignment(chronologicalAType, "88888");

        assert(markForRandomAssignment == false);
    }
    

    @Test
    public void testNotSetForRandomAssignmentBecauseTooWrongVictim() throws PWCGException
    {
        IAType17 waypoint1 = new AType17("T:14605 AType:17 ID:11111 POS(282341.281,941.709,62512.715)");
        IAType17 waypoint2 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown1 = new AType3("T:78230 AType:3 AID:22222 TID:99999 POS(232341.281,941.709,62512.715)");
        IAType17 waypoint3 = new AType17("T:14605 AType:17 ID:11111 POS(262341.281,941.709,62512.715)");
        IAType3 somebodyWhoGotShotDown2 = new AType3("T:78230 AType:3 AID:11111 TID:88888 POS(232341.281,941.709,62512.715)");
        
        chronologicalAType.add(waypoint1);
        chronologicalAType.add(waypoint2);
        chronologicalAType.add(somebodyWhoGotShotDown1);
        chronologicalAType.add(waypoint3);
        chronologicalAType.add(somebodyWhoGotShotDown2);

        AARRandomAssignmentCalculator randomAssignmentCalculatorTest = new AARRandomAssignmentCalculator(areaOfCombat);
        boolean markForRandomAssignment = randomAssignmentCalculatorTest.shouldBeMarkedForRandomAssignment(chronologicalAType, "77777");

        assert(markForRandomAssignment == false);
    }

}
