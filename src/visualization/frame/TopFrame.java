package visualization.frame;

import base.Main;
import simulation.Simulation;

public class TopFrame extends Frame {

    public TopFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = height / (float) nPages;
        this.plotPixelWidth = width / (float) nCols;
        this.plot = new int[nPages][nCols];
    }

    @Override
    protected void loadPlot() {
        float min = simulation.getMinValue();
        float max = simulation.getMaxValue();

        int y = currentDepth;
        for (int z = 0; z < nPages; z++) {
            for (int x = 0; x < nCols; x++) {
                plot[z][x] = plotTemperature ?
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
        main.text("Top", this.width / 2, 12);

    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nRows ? nRows-1 : currentDepth;
    }
}
