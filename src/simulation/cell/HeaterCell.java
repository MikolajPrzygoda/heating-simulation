package simulation.cell;

import util.Color;

public class HeaterCell extends Cell {

    private int typeColor = Color.fromRGB(255, 69, 40);

    public HeaterCell(){
        this.temperature = 1;
        this.mass = CELL_VOLUME * CellParameters.HEATER_DENSITY;
        this.heatCapacity = this.mass * CellParameters.HEATER_SPECIFIC_HEAT_CAPACITY;
        this.heatConductivity = CellParameters.HEATER_THERMAL_CONDUCTIVITY;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
