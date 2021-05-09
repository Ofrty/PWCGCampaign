package pwcg.mission.flight.attack;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.scramble.AirfieldAttackScrambleFlightBuilder;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilder;

public class GroundAttackPackage implements IFlightPackage
{
    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.GROUND_ATTACK);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        GroundAttackFlight groundAttackFlight = new GroundAttackFlight (flightInformation, targetDefinition);
		groundAttackFlight.createFlight();
		
        AirfieldAttackScrambleFlightBuilder.addAirfieldScrambleToFlight(groundAttackFlight);
		
        return groundAttackFlight;
    }

    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
