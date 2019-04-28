package visualization.frame;

import base.Main;
import processing.core.PConstants;
import simulation.Simulation;

public class FrontFrame extends Frame {

    public FrontFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = (height - 2 * padding) / (float) nRows;
        this.plotPixelWidth = (width - 2 * padding) / (float) nCols;
        this.plot = new int[nRows][nCols];
        this.title = "Front";
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
        main.pushMatrix();
        main.translate(posX, posY);

        //Border
        main.strokeWeight(padding);
        main.stroke(0, 0, 150); // RG{B} <=> XY{Z}
        main.fill(255);
        main.rect(padding / 2f, padding / 2f, width - padding, height - padding);

        //Plot
        drawPlot();

        //Title
        main.fill(0);
        main.textAlign(PConstants.CENTER);
        main.text(title, width / 2, 12);

        main.popMatrix();
    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nPages ? nPages - 1 : currentDepth;
    }
}
