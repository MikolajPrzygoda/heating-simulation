package visualization.frame;

import base.Main;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PConstants;
import simulation.Simulation;

public class FrontFrame extends Frame {

    public FrontFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelWidth = plotWidth / (float) nCols;
        this.plotPixelHeight = plotHeight / (float) nRows;
        this.plot = new int[nRows][nCols];
        this.title = "Front";
        this.maxDepth = nPages;
    }

    @Override
    public void loadPlot() {
        float min = simulation.getMinValue();
        float max = simulation.getMaxValue();

        int z = currentDepth;
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                if (((Toggle) main.guiController.getController("mode")).getState())
                    plot[y][x] = simulation.getCell(z, y, x).getTypeColor();
                else
                    plot[y][x] = temp2color(simulation.getCell(z, y, x).getValue(), min, max);
            }
        }
    }

    @Override
    protected void drawBorder() {
        main.strokeWeight(padding);
        main.stroke(0, 0, 120); // RG{B} <=> XY{Z}
        main.fill(255);
        main.rect(padding / 2f, padding / 2f, width - padding, height - padding);
    }

    @Override
    protected void drawDepthIndicator() {
        main.noStroke();
        main.fill(255);
        main.rectMode(PConstants.CENTER);

        main.rect(width - padding / 2, height / 2, depthIndicatorWidth, plotHeight);

        float knobY = PApplet.map(currentDepth, 0, maxDepth - 1, height - padding, padding);
        main.fill(150);
        main.rect(width - padding / 2, knobY, depthIndicatorKnobWidth, depthIndicatorKnobHeight);

        main.fill(255);
        main.text(0, width - padding / 2, height - padding + textHeight + 4);
        main.text(maxDepth, width - padding / 2, padding - 6);

        main.rectMode(PConstants.CORNER);
    }

    @Override
    protected void drawFramesOutlines(int xDepth, int yDepth, int zDepth) {
        main.noFill();
        main.strokeWeight(1);

        //Draw LeftSideFrame
        main.stroke(255, 0, 0);
        main.rect(padding + xDepth * plotPixelWidth, padding, plotPixelWidth, plotHeight);

        //Draw TopFrame
        main.stroke(0, 255, 0);
        main.rect(padding, padding + yDepth * plotPixelHeight, plotWidth, plotPixelHeight);
    }
}
