package simulation.cell;

import util.Color;

public class WallCell extends Cell {

    private int typeColor = Color.fromRGB(91, 91, 91);

    public WallCell(){
        this.value = 0.7;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
