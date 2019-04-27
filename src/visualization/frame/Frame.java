package visualization.frame;

import base.Main;
import processing.core.PApplet;
import simulation.Simulation;

public abstract class Frame {

    protected int width;
    protected int height;
    protected int posX, posY;
    protected Simulation simulation;
    protected int currentDepth = 0;

    protected int nPages;
    protected int nRows;
    protected int nCols;

    protected float plotPixelWidth;
    protected float plotPixelHeight;
    protected int[][] plot;
    protected boolean plotTemperature = true;

    protected Main main;

    public Frame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.simulation = simulation;
        this.main = main;

        this.nPages = simulation.getDepth();
        this.nRows = simulation.getHeight();
        this.nCols = simulation.getWidth();
    }

    public abstract void moveIn();

    public abstract void draw();

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    public void moveOut(){
        currentDepth = currentDepth - 1 < 0 ? 0 : currentDepth - 1;
    }

    protected int temp2color(double value, double min, double max) {
        int r = (int) PApplet.map((float) value, (float) min, (float) max, 0, 255);
        int b = 255 - r;
        return main.color(r, 0, b);
    }

    public void changeMode() {
        this.plotTemperature = !this.plotTemperature;
    }
}
