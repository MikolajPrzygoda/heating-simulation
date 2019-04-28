package visualization.frame;

import base.Main;
import processing.core.PApplet;
import processing.core.PConstants;
import simulation.Simulation;

public class TopFrame extends Frame {

    public TopFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = (height - 2 * padding) / (float) nPages;
        this.plotPixelWidth = (width - 2 * padding) / (float) nCols;
        this.plot = new int[nPages][nCols];
        this.title = "Top";
        this.maxDepth = nRows;
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
    public void drawBorder() {
        main.strokeWeight(padding);
        main.stroke(0, 120, 0); // R{G}B <=> X{Y}Z
        main.fill(255);
        main.rect(padding / 2f, padding / 2f, width - padding, height - padding);
    }

    @Override
    protected void drawDepthIndicator() {
        main.noStroke();
        main.fill(255);
        main.rectMode(PConstants.CENTER);

        main.rect(width - padding / 2, height / 2, depthIndicatorWidth, height - 2 * padding);

        float knobY = PApplet.map(currentDepth, 0, maxDepth - 1, padding, height - padding);
        main.fill(150);
        main.rect(width - padding / 2, knobY, depthIndicatorKnobWidth, depthIndicatorKnobHeight);

        main.fill(255);
        main.text(0, width - padding / 2, padding - 6);
        main.text(maxDepth, width - padding / 2, height - padding + textHeight + 4);

        main.rectMode(PConstants.CORNER);
    }
}
