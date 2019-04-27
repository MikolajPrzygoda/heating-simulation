package simulation.cell;

import util.Color;

public class HeaterCell extends Cell {

    private int typeColor = Color.fromRGB(255, 69, 40);

    public HeaterCell(){
        this.value = 1;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
