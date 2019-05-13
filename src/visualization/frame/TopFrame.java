package visualization.frame;

import base.Main;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PConstants;
import simulation.Simulation;

public class TopFrame extends Frame {

    public TopFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelWidth = plotWidth / (float) nCols;
        this.plotPixelHeight = plotHeight / (float) nPages;
        this.plot = new int[nPages][nCols];
        this.title = "Top";
        this.maxDepth = nRows;
    }

    @Override
    protected void loadPlot() {
        double min = simulation.getMinValue();
        double max = simulation.getMaxValue();

        int y = currentDepth;
        for (int z = 0; z < nPages; z++) {
            for (int x = 0; x < nCols; x++) {
                if (((Toggle) main.guiController.getController("mode")).getState())
                    plot[nPages - 1 - z][x] = simulation.getCell(z, y, x).getTypeColor();
                else
                    plot[nPages - 1 - z][x] = temp2color(simulation.getCell(z, y, x).getTemperature(), min, max);
            }
        }
    }

    @Override
    public void drawBorder() {
        canvas.strokeWeight(padding);
        canvas.stroke(0, 120, 0); // R{G}B <=> X{Y}Z
        canvas.fill(255);
        canvas.rect(padding / 2f, padding / 2f, width - padding, height - padding);
    }

    @Override
    protected void drawDepthIndicator() {
        canvas.noStroke();
        canvas.fill(255);
        canvas.rectMode(PConstants.CENTER);

        canvas.rect(width - padding / 2, height / 2, depthIndicatorWidth, plotHeight);

        float knobY = PApplet.map(currentDepth, 0, maxDepth - 1, padding, height - padding);
        canvas.fill(depthIndicatorKnobColor);
        canvas.rect(width - padding / 2, knobY, depthIndicatorKnobWidth, depthIndicatorKnobHeight);

        canvas.fill(255);
        canvas.text(0, width - padding / 2, padding - 6);
        canvas.text(maxDepth, width - padding / 2, height - padding + textHeight + 4);

        canvas.rectMode(PConstants.CORNER);
    }

    @Override
    protected void drawFramesOutlines(int xDepth, int yDepth, int zDepth) {
        canvas.noFill();
        canvas.strokeWeight(1);

        //Draw LeftSideFrame
        canvas.stroke(255, 0, 0);
        canvas.rect(padding + xDepth * plotPixelWidth, padding, plotPixelWidth, plotHeight);

        //Draw FrontFrame
        canvas.stroke(0, 0, 255);
        canvas.rect(padding, plotHeight + padding - (zDepth + 1) * plotPixelHeight, plotWidth, plotPixelHeight);
    }
}
