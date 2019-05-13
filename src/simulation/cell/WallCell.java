package simulation.cell;

import util.Color;

public class WallCell extends Cell {

    private int typeColor = Color.fromRGB(91, 91, 91);

    public WallCell(){
        this.mass = CELL_VOLUME * CellParameters.WALL_DENSITY;
        this.heatCapacity = this.mass * CellParameters.WALL_SPECIFIC_HEAT_CAPACITY;
        this.heatConductivity = CellParameters.WALL_THERMAL_CONDUCTIVITY;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
