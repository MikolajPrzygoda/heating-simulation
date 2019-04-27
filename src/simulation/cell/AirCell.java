package simulation.cell;

import util.Color;

public class AirCell extends Cell {

    private int typeColor = Color.fromRGB(66, 191, 244);

    public AirCell(){
        this.value = 0.3;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
