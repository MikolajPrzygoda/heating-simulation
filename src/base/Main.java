package base;

import processing.core.PApplet;
import simulation.RoomPlan;
import simulation.Simulation;
import visualization.frame.Frame;
import visualization.frame.FrontFrame;
import visualization.frame.SideFrame;
import visualization.frame.TopFrame;


public class Main extends PApplet {

    public static void main(String[] args) {
        Main myMain = new Main();
        PApplet.runSketch(new String[]{"-- Processing --"}, myMain);
    }

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;

    private Simulation simulation;
    private Frame topFrame;
    private Frame frontFrame;
    private Frame sideFrame;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
//        fullScreen();
        noSmooth(); //Disable anti-aliasing to prevent cells from having a faint grey border around them.

        simulation = new Simulation(new RoomPlan.Factory().build());

        // Create three frames in three quarters of the window. each showing a room cross-section
        // from a different direction.
        frontFrame = new FrontFrame(WIDTH / 2, HEIGHT / 2, 0, 0, simulation, this);
        topFrame = new TopFrame(WIDTH / 2, HEIGHT / 2, 0, HEIGHT / 2, simulation, this);
        sideFrame = new SideFrame(WIDTH / 2, HEIGHT / 2, WIDTH / 2, 0, simulation, this);
    }

    @Override
    public void draw() {
        background(0);
        surface.setTitle(String.valueOf(frameRate));

        simulation.update();

        frontFrame.draw();
        topFrame.draw();
        sideFrame.draw();
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
                sideFrame.moveIn();
                break;
            case 'd':
                sideFrame.moveOut();
                break;
            case 'r':
                simulation.reset();
                break;
            case ' ':
                frontFrame.changeMode();
                topFrame.changeMode();
                sideFrame.changeMode();
                break;
        }
    }
}
