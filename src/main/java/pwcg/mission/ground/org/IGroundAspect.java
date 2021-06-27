package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.BaseFlightMcu;

public interface IGroundAspect
{
    void createGroundUnitAspect() throws PWCGException;

    void linkToNextElement(int targetIndex);

    int getEntryPoint();

    BaseFlightMcu getEntryPointMcu();

    void write(BufferedWriter writer) throws PWCGException;
    
    void validate() throws PWCGException;
}
