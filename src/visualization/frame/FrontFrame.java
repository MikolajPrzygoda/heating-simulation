package visualization.frame;

import base.Main;
import processing.core.PConstants;
import simulation.Simulation;

public class FrontFrame extends Frame {

    public FrontFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = height / (float) nRows;
        this.plotPixelWidth = width / (float) nCols;
        this.plot = new int[nRows][nCols];
    }

    @Override
    public void loadPlot() {
        float min = simulation.getMinValue();
        float max = simulation.getMaxValue();

        int z = currentDepth;
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                plot[y][x] = plotTemperature ?
                        temp2color(simulation.getCell(z, y, x).getValue(), min, max) :
                        simulation.getCell(z, y, x).getTypeColor();
            }
        }
    }

    @Override
    public void draw() {
        drawPlot();

        main.fill(0);
        main.textAlign(PConstants.CENTER);
        main.text("Front", this.width / 2, 12);
    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nPages ? nPages - 1 : currentDepth;
    }
}
