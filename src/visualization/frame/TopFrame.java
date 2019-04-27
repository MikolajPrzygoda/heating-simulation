package visualization.frame;

import base.Main;
import simulation.Simulation;

public class TopFrame extends Frame {

    public TopFrame(int width, int height, int posX, int posY, Simulation simulation, Main main) {
        super(width, height, posX, posY, simulation, main);

        this.plotPixelHeight = height / (float) nPages;
        this.plotPixelWidth = width / (float) nCols;
        this.plot = new int[nPages][nCols];
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

            int y = currentDepth;
            for (int z = 0; z < nPages; z++) {
                for (int x = 0; x < nCols; x++) {
                    plot[z][x] = temp2color( simulation.getCell(z, y, x).getValue(), min, max );
                }
            }

            for (y = 0; y < plot.length; y++) {
                for (int x = 0; x < plot[0].length; x++) {
                    main.fill(plot[y][x]);
                    main.rect(x * plotPixelWidth, y * plotPixelHeight, plotPixelWidth, plotPixelHeight);
                }
            }
            main.stroke(255);
            main.fill(0);
            main.text("Top", this.width/2, 12);

            main.popMatrix();
            main.popStyle();
        }
    }

    @Override
    public void moveIn() {
        this.currentDepth++;
        currentDepth = currentDepth == nRows ? nRows-1 : currentDepth;
    }
}
