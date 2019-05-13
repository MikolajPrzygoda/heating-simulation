package simulation.cell;

import util.Color;

public class HeaterCell extends Cell {

    private int typeColor = Color.fromRGB(255, 69, 40);
    private double powerOutput = 0;

    public HeaterCell(){
        this.mass = CELL_VOLUME * CellParameters.HEATER_DENSITY;
        this.heatCapacity = this.mass * CellParameters.HEATER_SPECIFIC_HEAT_CAPACITY;
        this.heatConductivity = CellParameters.HEATER_THERMAL_CONDUCTIVITY;
    }

    public void setPowerOutput(double powerOutput){
        this.powerOutput = powerOutput;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }

    @Override
    public void applyEnergyChange(double timeStep){
        //As a heater, apart from the energyChange from thermal conduction
        // add additional energy based on cell power output:
        energyChange += powerOutput * timeStep;

        this.temperature += energyChange / heatCapacity;
        this.energyChange = 0;
    }
}
