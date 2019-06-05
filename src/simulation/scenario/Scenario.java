package simulation.scenario;

import simulation.Simulation;

public abstract class Scenario{
    public final int TIME_STEP = 15;

    public abstract boolean isGoalReached(Simulation simulation);

    public abstract int getHeaterPower(Simulation simulation);
}
