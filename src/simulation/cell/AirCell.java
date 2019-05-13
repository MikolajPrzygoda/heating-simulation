package simulation.cell;

import util.Color;

public class AirCell extends Cell {

    private int typeColor = Color.fromRGB(66, 191, 244);

    public AirCell(){
        this.mass = CELL_VOLUME * CellParameters.AIR_DENSITY;
        this.heatCapacity = this.mass * CellParameters.AIR_SPECIFIC_HEAT_CAPACITY;
        this.heatConductivity = CellParameters.AIR_THERMAL_CONDUCTIVITY;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }


}
