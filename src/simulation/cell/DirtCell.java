package simulation.cell;

import util.Color;

public class DirtCell extends Cell {

    private int typeColor = Color.fromRGB(102, 66, 26);

    public DirtCell() {
        this.value = 0.1;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
