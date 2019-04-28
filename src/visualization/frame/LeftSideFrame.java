package visualization.frame;

import base.Main;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PConstants;
import simulation.Simulation;

public class LeftSideFrame extends Frame {

    public LeftSideFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelWidth = plotWidth / (float) nPages;
        this.plotPixelHeight = plotHeight / (float) nRows;
        this.plot = new int[nRows][nPages];
        this.title = "Left side";
        this.maxDepth = nCols;
    }

    @Override
    protected void loadPlot() {
        float min = simulation.getMinValue();
        float max = simulation.getMaxValue();

        int x = currentDepth;
        for (int y = 0; y < nRows; y++) {
            for (int z = 0; z < nPages; z++) {
                if (((Toggle) main.guiController.getController("mode")).getState())
                    plot[y][nPages - 1 - z] = simulation.getCell(z, y, x).getTypeColor();
                else
                    plot[y][nPages - 1 - z] = temp2color(simulation.getCell(z, y, x).getValue(), min, max);
            }
        }
    }

    @Override
    public void drawBorder() {
        main.strokeWeight(padding);
        main.stroke(120, 0, 0); // {R}GB <=> {X}YZ
        main.fill(255);
        main.rect(padding / 2f, padding / 2f, width - padding, height - padding);
    }

    @Override
    protected void drawDepthIndicator() {
        main.noStroke();
        main.fill(255);
        main.rectMode(PConstants.CENTER);

        main.rect(width / 2, height - padding / 2, plotWidth, depthIndicatorWidth);

        float knobX = PApplet.map(currentDepth, 0, maxDepth - 1, padding, width - padding);
        main.fill(150);
        main.rect(knobX, height - padding / 2, depthIndicatorKnobHeight, depthIndicatorKnobWidth);

        main.fill(255);
        main.text(0, padding / 2, height - padding / 2 + textHeight / 2 - 2);
        main.text(maxDepth, width - padding / 2, height - padding / 2 + textHeight / 2 - 2);

        main.rectMode(PConstants.CORNER);
    }

    @Override
    protected void drawFramesOutlines(int xDepth, int yDepth, int zDepth) {
        main.noFill();
        main.strokeWeight(1);

        //Draw FrontFrame
        main.stroke(0, 0, 255);
        main.rect(plotWidth + padding - (zDepth + 1) * plotPixelWidth, padding, plotPixelWidth, plotHeight);

        //Draw TopFrame
        main.stroke(0, 255, 0);
        main.rect(padding, padding + yDepth * plotPixelHeight, plotWidth, plotPixelHeight);
    }
}
