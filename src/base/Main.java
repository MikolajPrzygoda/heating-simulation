package base;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;
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
    public ControlP5 guiController;


    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
//        fullScreen();
    }

    @Override
    public void setup() {
        createGUI();

        simulation = new Simulation(new RoomPlan.Factory().build());

        // Create three frames in three quarters of the window. each showing a room cross-section
        // from a different direction.
        frontFrame = new FrontFrame(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2, simulation, this);
        topFrame = new TopFrame(WIDTH / 2, HEIGHT / 2, WIDTH / 2, 0, simulation, this);
        leftSideFrame = new LeftSideFrame(WIDTH / 2, HEIGHT / 2, 0, HEIGHT / 2, simulation, this);
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
            case 'm':
                ((Toggle) guiController.getController("mode")).toggle();
                break;
            case 'g':
                ((Toggle) guiController.getController("grid")).toggle();
                break;
            case 'p':
                ((Toggle) guiController.getController("pause")).toggle();
                break;
            case 'o':
                ((Toggle) guiController.getController("outlines")).toggle();
                break;
        }
    }

    private void createGUI() {
        guiController = new ControlP5(this);

        PFont pfont = createFont("DejaVu Sans Mono", 12, true);
        guiController.setFont(new ControlFont(pfont));

        guiController.addLabel("Options:")
                .setPosition(16, 16);

        Toggle pauseToggle = guiController.addToggle("pause")
                .setPosition(16, 46)
                .setSize(20, 20)
                .setValue(false)
                .addListener(this::pauseDrawing);
        pauseToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("(P)ause drawing")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle modeToggle = guiController.addToggle("mode")
                .setPosition(16, 86)
                .setSize(20, 20)
                .setValue(false);
        modeToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Frames' (M)ode - Show temperature / cell type")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle outlinesToggle = guiController.addToggle("outlines")
                .setPosition(16, 126)
                .setSize(20, 20)
                .setValue(true);
        outlinesToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Show frame (o)utlines")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle gridToggle = guiController.addToggle("grid")
                .setPosition(16, 166)
                .setSize(20, 20)
                .setValue(false);
        gridToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Show (g)rid")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle translucentToggle = guiController.addToggle("translucent")
                .setPosition(36, 206)
                .setSize(20, 20)
                .setValue(true);
        translucentToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Translucent grid")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);
    }

    // =========================== ControlP5 GUI Listeners ===========================
    private void pauseDrawing(ControlEvent controlEvent) {
        if (((Toggle) controlEvent.getController()).getState())
            noLoop();
        else
            loop();
    }

}
