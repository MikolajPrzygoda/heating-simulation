package simulation.cell;

import util.Color;

public class DirtCell extends Cell {

    private int typeColor = Color.fromRGB(102, 66, 26);

    public DirtCell() {
        this.temperature = 0.1;
        this.mass = CELL_VOLUME * CellParameters.DIRT_DENSITY;
        this.heatCapacity = this.mass * CellParameters.DIRT_SPECIFIC_HEAT_CAPACITY;
        this.heatConductivity = CellParameters.DIRT_THERMAL_CONDUCTIVITY;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
