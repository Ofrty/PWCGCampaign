 package pwcg.mission.flight.balloondefense;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;
import pwcg.mission.target.TargetDefinitionBuilderOpposing;

public class BalloonDefensePackage implements IFlightPackage
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private List<IFlight> packageFlights = new ArrayList<>();

    public BalloonDefensePackage()
    {
    }

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BALLOON_DEFENSE);
        this.targetDefinition = buildTargetDefintion();

        IFlight balloonDefenseFlight = buildBalloonDefenseFllght();
        packageFlights.add(balloonDefenseFlight);
		return packageFlights;
	}

    private IFlight buildBalloonDefenseFllght() throws PWCGException
    {
        IFlight balloonDefenseFlight = new BalloonDefenseFlight(flightInformation, targetDefinition);
        balloonDefenseFlight.createFlight();
        return balloonDefenseFlight;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder;
        if (this.flightInformation.isPlayerFlight())
        {
            targetDefinitionBuilder = new TargetDefinitionBuilderOpposing(flightInformation);
        }
        else
        {
            targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        }

        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
