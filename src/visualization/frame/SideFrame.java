package visualization.frame;

import base.Main;
import simulation.Simulation;

public class SideFrame extends Frame {

    public SideFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = height / (float) nRows;
        this.plotPixelWidth = width / (float) nPages;
        this.plot = new int[nRows][nPages];
    }

    @Override
    protected void loadPlot() {
        float min = simulation.getMinValue();
        float max = simulation.getMaxValue();

        int x = currentDepth;
        for (int y = 0; y < nRows; y++) {
            for (int z = 0; z < nPages; z++) {
                plot[y][z] = plotTemperature ?
                        temp2color(simulation.getCell(z, y, x).getValue(), min, max) :
                        simulation.getCell(z, y, x).getTypeColor();
            }
        }
    }

    @Override
    public void draw() {
        drawPlot();

        main.stroke(255);
        main.fill(0);
        main.text("Side", this.width / 2, 12);
    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nCols ? nCols-1 : currentDepth;
    }
}
