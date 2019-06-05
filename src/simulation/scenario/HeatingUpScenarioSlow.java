package simulation.scenario;

import simulation.Simulation;

public class HeatingUpScenarioSlow extends Scenario{
    @Override
    public boolean isGoalReached(Simulation simulation){
        return simulation.getAverageTemperature() >= 24; // Run until the room temp reaches 24 degrees Celsius.
    }

    @Override
    public int getHeaterPower(Simulation simulation){
        if(simulation.getAverageTemperature() < 15)
            return 1000;
        if(simulation.getAverageTemperature() < 17)
            return 800;
        if(simulation.getAverageTemperature() < 20)
            return 600;
        if(simulation.getAverageTemperature() < 23)
            return 400;
        if(simulation.getAverageTemperature() < 24)
            return 200;
        else
            return 0;
    }
}
