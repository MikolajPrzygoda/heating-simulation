package simulation.cell;

import util.Color;

public class OutsideCell extends Cell {

    private int typeColor = Color.fromRGB(45, 131, 168);

    public OutsideCell(){
        this.value = 0;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
