package simulation.scenario;

import simulation.Simulation;

public class HeatingUpScenarioFast extends Scenario{
    @Override
    public boolean isGoalReached(Simulation simulation){
        return simulation.getAverageTemperature() >= 24; // Run until the room temp reaches 24 degrees Celsius.
    }

    @Override
    public int getHeaterPower(Simulation simulation){
        if(simulation.getAverageTemperature() < 20)
            return 2000;
        if(simulation.getAverageTemperature() < 21)
            return 1750;
        if(simulation.getAverageTemperature() < 22)
            return 1500;
        if(simulation.getAverageTemperature() < 23)
            return 1250;
        if(simulation.getAverageTemperature() < 24)
            return 1000;
        else
            return 0;
    }
}
