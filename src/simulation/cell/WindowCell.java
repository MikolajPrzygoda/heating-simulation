package simulation.cell;

import util.Color;

public class WindowCell extends Cell {

    private int typeColor = Color.fromRGB(188, 255, 230);

    public WindowCell() {
        this.value = 0.5;
    }

    @Override
    public int getTypeColor() {
        return typeColor;
    }
}
