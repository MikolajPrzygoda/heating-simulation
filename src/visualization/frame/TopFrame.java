package visualization.frame;

import base.Main;
import processing.core.PConstants;
import simulation.Simulation;

public class TopFrame extends Frame {

    public TopFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = (height - 2 * padding) / (float) nPages;
        this.plotPixelWidth = (width - 2 * padding) / (float) nCols;
        this.plot = new int[nPages][nCols];
        this.title = "Top";
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
        main.pushMatrix();
        main.translate(posX, posY);

        //Border
        main.strokeWeight(padding);
        main.stroke(0, 150, 0); // R{G}B <=> X{Y}Z
        main.fill(255);
        main.rect(padding / 2f, padding / 2f, width - padding, height - padding);

        //Plot
        drawPlot();

        main.fill(0);
        main.textAlign(PConstants.CENTER);
        main.text(title, width / 2f, 12);

        main.popMatrix();
    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nRows ? nRows - 1 : currentDepth;
    }
}
