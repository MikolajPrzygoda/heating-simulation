package simulation.cell;

import util.Color;

public class WindowCell extends Cell {

    private int typeColor = Color.fromRGB(188, 255, 230);

    public WindowCell() {
        this.temperature = 0.5;
        this.mass = CELL_VOLUME * CellParameters.WINDOW_DENSITY;
        this.heatCapacity = this.mass * CellParameters.WINDOW_SPECIFIC_HEAT_CAPACITY;
        this.heatConductivity = CellParameters.WINDOW_THERMAL_CONDUCTIVITY;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
