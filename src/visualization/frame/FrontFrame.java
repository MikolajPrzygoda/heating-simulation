package visualization.frame;

import base.Main;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PConstants;
import simulation.Simulation;
import simulation.cell.Cell;

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
        double min = simulation.getMinValue();
        double max = simulation.getMaxValue();

        int z = currentDepth;
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                if (((Toggle) main.guiController.getController("mode")).getState())
                    plot[y][x] = simulation.getCell(z, y, x).getTypeColor();
                else
                    plot[y][x] = temp2color(simulation.getCell(z, y, x).getTemperature(), min, max);
            }
        }
    }

    @Override
    protected void drawBorder() {
        canvas.strokeWeight(padding);
        canvas.stroke(0, 0, 120); // RG{B} <=> XY{Z}
        canvas.fill(255);
        canvas.rect(padding / 2f, padding / 2f, width - padding, height - padding);
    }

    @Override
    protected void drawDepthIndicator() {
        canvas.noStroke();
        canvas.fill(255);
        canvas.rectMode(PConstants.CENTER);

        //Draw slider
        canvas.rect(width - padding / 2, height / 2, depthIndicatorWidth, plotHeight - 2 * padding);

        //Draw know
        float knobY = PApplet.map(currentDepth, 0, maxDepth - 1, height - 2 * padding, 2 * padding);
        canvas.fill(depthIndicatorKnobColor);
        canvas.rect(width - padding / 2, knobY, depthIndicatorKnobWidth, depthIndicatorKnobHeight);

        //Draw min/max values
        canvas.fill(255);
        canvas.text(0, width - padding / 2, height - 2 * padding + textHeight + 4);
        canvas.text(maxDepth, width - padding / 2, 2 * padding - 6);

        canvas.rectMode(PConstants.CORNER);
    }

    @Override
    protected void drawFramesOutlines(int xDepth, int yDepth, int zDepth) {
        canvas.noFill();
        canvas.strokeWeight(1);

        //Draw LeftSideFrame
        canvas.stroke(255, 0, 0);
        canvas.rect(padding + xDepth * plotPixelWidth, padding, plotPixelWidth, plotHeight);

        //Draw TopFrame
        canvas.stroke(0, 255, 0);
        canvas.rect(padding, padding + yDepth * plotPixelHeight, plotWidth, plotPixelHeight);
    }

    @Override
    public Cell getCellAt(int u, int v) {
        if (u > padding && u < width - padding && v > padding && v < height - padding) {
            int z = currentDepth;
            int y = (int) ((v - padding) / plotPixelHeight);
            int x = (int) ((u - padding) / plotPixelWidth);

            return simulation.getCell(z, y, x);
        }
        return null;
    }
}
