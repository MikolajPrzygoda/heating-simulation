package visualization.frame;

import base.Main;
import simulation.Simulation;

public class FrontFrame extends Frame {

    public FrontFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = height / (float) nRows;
        this.plotPixelWidth = width / (float) nCols;
        this.plot = new int[nRows][nCols];
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

            int z = currentDepth;
            for (int y = 0; y < nRows; y++) {
                for (int x = 0; x < nCols; x++) {
                    plot[y][x] = temp2color( simulation.getCell(z, y, x).getValue(), min, max );
                }
            }

            for (int y = 0; y < plot.length; y++) {
                for (int x = 0; x < plot[0].length; x++) {
                    main.fill(plot[y][x]);
                    main.rect(x * plotPixelWidth, y * plotPixelHeight, plotPixelWidth, plotPixelHeight);
                }
            }
            main.stroke(255);
            main.fill(0);
            main.text("Front", this.width/2, 12);

            main.popMatrix();
            main.popStyle();
        }
    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nPages ? nPages-1 : currentDepth;
    }
}
