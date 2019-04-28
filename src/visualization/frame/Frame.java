package visualization.frame;

import base.Main;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PConstants;
import simulation.Simulation;

public abstract class Frame {

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


    public Frame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        this.width = width;
        this.height = height;
        this.plotWidth = width - 2 * padding;
        this.plotHeight = height - 2 * padding;
        this.posX = posX;
        this.posY = posY;
        this.simulation = simulation;
        this.main = main;

        this.nPages = simulation.getDepth();
        this.nRows = simulation.getHeight();
        this.nCols = simulation.getWidth();
    }

    protected abstract void loadPlot();

    protected void drawPlot() {
        loadPlot();

        main.translate(padding, padding);

        //Set cell stroke according to gui controls.
        if (((Toggle) main.guiController.getController("grid")).getState()) {
            if (((Toggle) main.guiController.getController("translucent")).getState())
                main.stroke(140, 80);
            else
                main.stroke(140);
            main.strokeWeight(1);
        }
        else
            main.noStroke();

        for (int y = 0; y < plot.length; y++) {
            for (int x = 0; x < plot[0].length; x++) {
                main.fill(plot[y][x]);
                main.rect(x * plotPixelWidth, y * plotPixelHeight, plotPixelWidth, plotPixelHeight);
            }
        }

        main.translate(-padding, -padding);
    }

    protected abstract void drawDepthIndicator();

    public void draw() {
        main.translate(posX, posY);

        //Border
        drawBorder();

        //Plot
        drawPlot();

        //Title
        main.fill(255);
        main.textAlign(PConstants.CENTER);
        main.text(title, width / 2f, padding / 2 + textHeight / 3);

        //Depth Indicator
        drawDepthIndicator();

        if (((Toggle) main.guiController.getController("outlines")).getState())
            drawFramesOutlines(
                    main.leftSideFrame.getCurrentDepth(),
                    main.topFrame.getCurrentDepth(),
                    main.frontFrame.getCurrentDepth()
            );

        main.translate(-posX, -posY);
    }

    protected abstract void drawBorder();

    protected abstract void drawFramesOutlines(int xDepth, int yDepth, int zDepth);

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == maxDepth ? maxDepth - 1 : currentDepth;
    }

    public void moveOut() {
        currentDepth = currentDepth - 1 < 0 ? 0 : currentDepth - 1;
    }

    protected int temp2color(double value, double min, double max) {
        int r = (int) PApplet.map((float) value, (float) min, (float) max, 0, 255);
        int b = 255 - r;
        return main.color(r, 0, b);
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}
