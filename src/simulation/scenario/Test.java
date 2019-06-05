package simulation.scenario;

import simulation.Simulation;

public class Test extends Scenario{
    @Override
    public boolean isGoalReached(Simulation simulation){
        return true;
    }

    @Override
    public int getHeaterPower(Simulation simulation){
        return 0;
    }
}
