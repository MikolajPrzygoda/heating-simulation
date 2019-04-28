package visualization.frame;

import base.Main;
import processing.core.PConstants;
import simulation.Simulation;

public class SideFrame extends Frame {

    public SideFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = (height - 2 * padding) / (float) nRows;
        this.plotPixelWidth = (width - 2 * padding) / (float) nPages;
        this.plot = new int[nRows][nPages];
        this.title = "Side";
    }

    @Override
    protected void loadPlot() {
        float min = simulation.getMinValue();
        float max = simulation.getMaxValue();

        int x = currentDepth;
        for (int y = 0; y < nRows; y++) {
            for (int z = 0; z < nPages; z++) {
                plot[y][z] = plotTemperature ?
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
        main.stroke(150, 0, 0); // {R}GB <=> {X}YZ
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
        currentDepth = currentDepth == nCols ? nCols - 1 : currentDepth;
    }
}
