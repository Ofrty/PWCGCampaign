package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IATypeBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.UnknownVictoryAssignments;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARRandomAssignmentTest
{
    @Mock
    private AARLogEventData logEventData;
    
    @Mock
    private AARRandomAssignmentCalculator randomAssignmentCalculator;
    
    private LogVictory victoryResult;
    
    @Before
    public void setup()
    {
        victoryResult = new LogVictory(10);
    }

    @Test
    public void testMarkTrue () throws PWCGException
    {
        Mockito.when(randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(ArgumentMatchers.<List<IATypeBase>>any(), ArgumentMatchers.<String>any())).thenReturn(true);

        AARRandomAssignment randomAssignment = new AARRandomAssignment(logEventData, randomAssignmentCalculator);
        LogUnknown markedForAssignment = randomAssignment.markForRandomAssignment(victoryResult);
        
        assert(markedForAssignment.getUnknownVictoryAssignment() == UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
    }

    @Test
    public void testMarkFalse () throws PWCGException
    {
        Mockito.when(randomAssignmentCalculator.shouldBeMarkedForRandomAssignment(ArgumentMatchers.<List<IATypeBase>>any(), ArgumentMatchers.<String>any())).thenReturn(false);

        AARRandomAssignment randomAssignment = new AARRandomAssignment(logEventData, randomAssignmentCalculator);
        LogUnknown markedForAssignment = randomAssignment.markForRandomAssignment(victoryResult);
        
        assert(markedForAssignment == null);
    }

}
