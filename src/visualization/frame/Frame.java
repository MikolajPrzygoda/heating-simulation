package visualization.frame;

import base.Main;
import controlP5.Toggle;
import processing.core.PConstants;
import processing.core.PGraphics;
import simulation.Simulation;
import simulation.cell.Cell;
import visualization.pallete.IronbowPalette;

public abstract class Frame {

    protected PGraphics canvas;
    protected Main main;
    protected Simulation simulation;

    protected int width, height;
    protected int posX, posY;
    protected int currentDepth, maxDepth;

    protected int nPages, nRows, nCols;

    protected String title = "";
    protected int textHeight = 12;
    protected int padding = 24;

    protected int plotWidth, plotHeight;
    protected float plotPixelWidth, plotPixelHeight;
    protected int[][] plot;

    protected int depthIndicatorWidth = padding / 4;
    protected int depthIndicatorKnobWidth = depthIndicatorWidth * 2;
    protected int depthIndicatorKnobHeight = 6;
    protected int depthIndicatorKnobColor = 100;

    protected int colorScaleWidth = padding / 2;


    public Frame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.simulation = simulation;
        this.main = main;

        this.canvas = main.createGraphics(width, height);
        canvas.noSmooth();

        this.nPages = simulation.getDepth();
        this.nRows = simulation.getHeight();
        this.nCols = simulation.getWidth();

        this.plotWidth = width - 2 * padding;
        this.plotHeight = height - 2 * padding;
    }

    protected abstract void loadPlot();

    protected void drawPlot() {
        loadPlot();

        canvas.translate(padding, padding);

        //Set cell stroke according to gui controls.
        if (((Toggle) main.guiController.getController("grid")).getState()) {
            if (((Toggle) main.guiController.getController("translucent")).getState())
                canvas.stroke(140, 80);
            else
                canvas.stroke(140);
            canvas.strokeWeight(1);
        }
        else
            canvas.noStroke();

        for (int y = 0; y < plot.length; y++) {
            for (int x = 0; x < plot[0].length; x++) {
                canvas.fill(plot[y][x]);
                canvas.rect(x * plotPixelWidth, y * plotPixelHeight, plotPixelWidth, plotPixelHeight);
            }
        }

        canvas.translate(-padding, -padding);
    }

    public void draw() {
        canvas.beginDraw();

        //Border
        drawBorder();

        //Plot
        drawPlot();

        //Title
        canvas.fill(255);
        canvas.textAlign(PConstants.CENTER);
        canvas.text(title, width / 2f, padding / 2 + textHeight / 3);

        //Depth Indicator
        drawDepthIndicator();

        //Scale
        drawColorScale();

        if (((Toggle) main.guiController.getController("outlines")).getState())
            drawFramesOutlines(
                    main.leftSideFrame.getCurrentDepth(),
                    main.topFrame.getCurrentDepth(),
                    main.frontFrame.getCurrentDepth()
            );

        canvas.endDraw();
        main.image(canvas, posX, posY);
    }

    protected abstract void drawBorder();

    protected abstract void drawFramesOutlines(int xDepth, int yDepth, int zDepth);

    protected abstract void drawDepthIndicator();

    //By default draw the scale to the left of the plot
    protected void drawColorScale() {
        //Min-Max values
        canvas.textSize(8);
        String minString = (simulation.getMinValue() + "").substring(0, 4);
        String maxString = (simulation.getMaxValue() + "").substring(0, 4);

        canvas.text(minString, padding * 0.5f - 1, plotHeight + padding * 0.5f + 4);
        canvas.text(maxString, padding * 0.5f - 1, padding * 1.5f);

        canvas.textSize(12);

        //Scale
        //Start from 2*padding and for each pixel to plotHeight draw a 1-pixel high bar in appropriate color
        for (int i = 2 * padding; i < plotHeight; i++) {
            canvas.noStroke();
            canvas.fill(IronbowPalette.getColor(i, 2 * padding, plotHeight));
            canvas.rect(padding / 2 - colorScaleWidth / 2, height - i, colorScaleWidth, 1);
        }
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    public void moveIn() {
        currentDepth = currentDepth == maxDepth - 1 ? currentDepth : currentDepth + 1;
    }

    public void moveOut() {
        currentDepth = currentDepth == 0 ? currentDepth : currentDepth - 1;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public abstract Cell getCellAt(int u, int v);
}
