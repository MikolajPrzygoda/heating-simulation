package simulation.scenario;

import simulation.Simulation;

public class MaintainTemperatureScenario extends Scenario{
    @Override
    public boolean isGoalReached(Simulation simulation){
        return simulation.getElapsedTime() >= 3600 * 24; //Run for 24h
    }

    @Override
    public int getHeaterPower(Simulation simulation){
        if(simulation.getAverageTemperature() < 18)
            return 2000;
        if(simulation.getAverageTemperature() < 21)
            return 1500;
        if(simulation.getAverageTemperature() < 24)
            return 1000;
        else
            return 0;
    }
}
