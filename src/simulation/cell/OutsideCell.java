package simulation.cell;

import util.Color;

public class OutsideCell extends Cell {

    private int typeColor = Color.fromRGB(45, 131, 168);

    public OutsideCell(){
        this.mass = CELL_VOLUME * CellParameters.AIR_DENSITY;
        this.heatCapacity = this.mass * CellParameters.AIR_SPECIFIC_HEAT_CAPACITY;
        this.heatConductivity = CellParameters.AIR_THERMAL_CONDUCTIVITY;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }

    /**
     * Overridden to not change this cell's temperature - it's and outside cell that has constant temperature.
     */
    @Override
    public void applyEnergyChange(){
        this.energyChange = 0;
    }
}
