package visualization.frame;

import base.Main;
import simulation.Simulation;

public class SideFrame extends Frame {

    public SideFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = height / (float) nRows;
        this.plotPixelWidth = width / (float) nPages;
        this.plot = new int[nRows][nPages];
    }

    @Override
    public void draw() {
        {
            main.pushMatrix();
            main.pushStyle();
            main.translate(posX, posY);
            main.noStroke();

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


            for (int y = 0; y < plot.length; y++) {
                for (x = 0; x < plot[0].length; x++) {
                    main.fill(plot[y][x]);
                    main.rect(x * plotPixelWidth, y * plotPixelHeight, plotPixelWidth, plotPixelHeight);
                }
            }
            main.stroke(255);
            main.fill(0);
            main.text("Side", this.width/2, 12);

            main.popMatrix();
            main.popStyle();
        }
    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nCols ? nCols-1 : currentDepth;
    }
}
