package base;

import processing.core.PApplet;
import simulation.RoomPlan;
import simulation.Simulation;
import visualization.frame.Frame;
import visualization.frame.FrontFrame;
import visualization.frame.LeftSideFrame;
import visualization.frame.TopFrame;


public class Main extends PApplet {

    public static void main(String[] args) {
        Main myMain = new Main();
        PApplet.runSketch(new String[]{"-- Processing --"}, myMain);
    }

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;

    private Simulation simulation;
    public Frame topFrame;
    public Frame frontFrame;
    public Frame leftSideFrame;

    private boolean isDrawingPaused = false;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
//        fullScreen();

        //Disable anti-aliasing to prevent cells from having a faint grey border around them and improve performance.
        noSmooth();

        simulation = new Simulation(new RoomPlan.Factory().build());

        // Create three frames in three quarters of the window. each showing a room cross-section
        // from a different direction.
        frontFrame = new FrontFrame(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2, simulation, this);
        topFrame = new TopFrame(WIDTH / 2, HEIGHT / 2, WIDTH / 2, 0, simulation, this);
        leftSideFrame = new LeftSideFrame(WIDTH / 2, HEIGHT / 2, 0, HEIGHT / 2, simulation, this);

    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        background(0);
        surface.setTitle(String.valueOf(frameRate));

        simulation.update();

        frontFrame.draw();
        topFrame.draw();
        leftSideFrame.draw();
    }

    @Override
    public void keyPressed() {
        switch (key) {
            case 'q':
                topFrame.moveIn();
                break;
            case 'e':
                topFrame.moveOut();
                break;
            case 'w':
                frontFrame.moveIn();
                break;
            case 's':
                frontFrame.moveOut();
                break;
            case 'a':
                leftSideFrame.moveOut();
                break;
            case 'd':
                leftSideFrame.moveIn();
                break;
            case 'r':
                simulation.reset();
                break;
            case ' ':
                frontFrame.changeMode();
                topFrame.changeMode();
                leftSideFrame.changeMode();
                break;
            case 'g':
                frontFrame.changeGrid();
                topFrame.changeGrid();
                leftSideFrame.changeGrid();
                break;
            case 'p':
                if (isDrawingPaused)
                    loop();
                else
                    noLoop();
                isDrawingPaused = !isDrawingPaused;
                break;
        }
    }
}
