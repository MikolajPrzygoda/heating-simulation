package visualization.frame;

import base.Main;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PConstants;
import simulation.Simulation;
import simulation.cell.Cell;

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
        double min = simulation.getMinValue();
        double max = simulation.getMaxValue();

        int x = currentDepth;
        for (int y = 0; y < nRows; y++) {
            for (int z = 0; z < nPages; z++) {
                if (((Toggle) main.guiController.getController("mode")).getState())
                    plot[y][nPages - 1 - z] = simulation.getCell(z, y, x).getTypeColor();
                else
                    plot[y][nPages - 1 - z] = temp2color(simulation.getCell(z, y, x).getTemperature(), min, max);
            }
        }
    }

    @Override
    public void drawBorder() {
        canvas.strokeWeight(padding);
        canvas.stroke(120, 0, 0); // {R}GB <=> {X}YZ
        canvas.fill(255);
        canvas.rect(padding / 2f, padding / 2f, width - padding, height - padding);
    }

    @Override
    protected void drawDepthIndicator() {
        canvas.noStroke();
        canvas.fill(255);
        canvas.rectMode(PConstants.CENTER);

        canvas.rect(width / 2, height - padding / 2, plotWidth, depthIndicatorWidth);

        float knobX = PApplet.map(currentDepth, 0, maxDepth - 1, padding, width - padding);
        canvas.fill(depthIndicatorKnobColor);
        canvas.rect(knobX, height - padding / 2, depthIndicatorKnobHeight, depthIndicatorKnobWidth);

        canvas.fill(255);
        canvas.text(0, padding / 2, height - padding / 2 + textHeight / 2 - 2);
        canvas.text(maxDepth, width - padding / 2, height - padding / 2 + textHeight / 2 - 2);

        canvas.rectMode(PConstants.CORNER);
    }

    @Override
    protected void drawFramesOutlines(int xDepth, int yDepth, int zDepth) {
        canvas.noFill();
        canvas.strokeWeight(1);

        //Draw FrontFrame
        canvas.stroke(0, 0, 255);
        canvas.rect(plotWidth + padding - (zDepth + 1) * plotPixelWidth, padding, plotPixelWidth, plotHeight);

        //Draw TopFrame
        canvas.stroke(0, 255, 0);
        canvas.rect(padding, padding + yDepth * plotPixelHeight, plotWidth, plotPixelHeight);
    }

    @Override
    public Cell getCellAt(int u, int v){
        return null;
    }
}
