package base;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import simulation.RoomPlan;
import simulation.Simulation;
import simulation.cell.Cell;
import visualization.frame.Frame;
import visualization.frame.FrontFrame;
import visualization.frame.LeftSideFrame;
import visualization.frame.TopFrame;


public class Main extends PApplet{

    public static void main(String[] args){
        Main myMain = new Main();
        PApplet.runSketch(new String[]{"-- Processing --"}, myMain);
    }

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;

    public Simulation simulation;
    public Frame topFrame;
    public Frame frontFrame;
    public Frame leftSideFrame;
    public ControlP5 guiController;


    @Override
    public void settings(){
        size(WIDTH, HEIGHT);
//        fullScreen();
    }

    @Override
    public void setup(){
        createGUI();

        simulation = new Simulation(new RoomPlan.Factory().build());

        // Create three frames in three quarters of the window. each showing a room cross-section
        // from a different direction.
        frontFrame = new FrontFrame(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2, simulation, this);
        topFrame = new TopFrame(WIDTH / 2, HEIGHT / 2, WIDTH / 2, 0, simulation, this);
        leftSideFrame = new LeftSideFrame(WIDTH / 2, HEIGHT / 2, 0, HEIGHT / 2, simulation, this);
    }

    @Override
    public void draw(){
        background(0);

        if(!((Toggle) guiController.getController("pauseSimulation")).getState()){
            simulation.update();
        }

        if(!((Toggle) guiController.getController("pauseDrawingFrames")).getState()){
            frontFrame.draw();
            topFrame.draw();
            leftSideFrame.draw();
        }

        //FPS counter
        surface.setTitle(String.valueOf(frameRate));

        //Update elapsedTime label
        ((Textlabel) guiController.getController("elapsedTime")).setText(
                "Elapsed time: " + simulation.getElapsedTime() / 60 + "m"
        );
    }

    @Override
    public void keyPressed(){
        switch(key){
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
            case 'm':
                ((Toggle) guiController.getController("mode")).toggle();
                break;
            case 'g':
                ((Toggle) guiController.getController("grid")).toggle();
                break;
            case 't':
                ((Toggle) guiController.getController("translucent")).toggle();
                break;
            case 'P':
                ((Toggle) guiController.getController("pauseDrawingFrames")).toggle();
                break;
            case 'p':
                ((Toggle) guiController.getController("pauseSimulation")).toggle();
                break;
            case 'o':
                ((Toggle) guiController.getController("outlines")).toggle();
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent event){
        Cell cell;

        if(event.getX() < WIDTH / 2){
            cell = leftSideFrame.getCellAt(event.getX(), event.getY() - HEIGHT / 2);
        }
        else if(event.getY() < HEIGHT / 2){
            cell = topFrame.getCellAt(event.getX() - WIDTH / 2, event.getY());
        }
        else{
            cell = frontFrame.getCellAt(event.getX() - WIDTH / 2, event.getY() - HEIGHT / 2);
        }

        if(cell != null){
            System.out.println("Temp: " + cell.getTemperature());
        }
    }

    private void createGUI(){
        guiController = new ControlP5(this);

        PFont pfont = createFont("DejaVu Sans Mono", 12, true);
        guiController.setFont(new ControlFont(pfont));

        guiController.addLabel("Options:")
                .setPosition(16, 16);

        guiController.addLabel("Information:")
                .setPosition(416, 16);
        guiController.addLabel("elapsedTime")
                .setPosition(416, 46);

        Toggle pauseDrawingFramesToggle = guiController.addToggle("pauseDrawingFrames")
                .setPosition(16, 46)
                .setSize(20, 20)
                .setValue(false);
        pauseDrawingFramesToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("[P]ause drawing Frames")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle pauseSimulationToggle = guiController.addToggle("pauseSimulation")
                .setPosition(216, 46)
                .setSize(20, 20)
                .setValue(true);
        pauseSimulationToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("[p]ause simulation")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle modeToggle = guiController.addToggle("mode")
                .setPosition(16, 86)
                .setSize(20, 20)
                .setValue(false);
        modeToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Frames' [m]ode - Show temperature / cell type")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle outlinesToggle = guiController.addToggle("outlines")
                .setPosition(16, 126)
                .setSize(20, 20)
                .setValue(true);
        outlinesToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Show frame [o]utlines")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle gridToggle = guiController.addToggle("grid")
                .setPosition(16, 166)
                .setSize(20, 20)
                .setValue(false);
        gridToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Show [g]rid")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Toggle translucentToggle = guiController.addToggle("translucent")
                .setPosition(36, 206)
                .setSize(20, 20)
                .setValue(true);
        translucentToggle
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("[t]ranslucent grid (Very costly)")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Slider timeStepSlider = guiController.addSlider("timeStep")
                .setRange(0, 20)
                .setValue(5)
                .setPosition(16, 246)
                .setSize(160, 20)
                .setNumberOfTickMarks(21) //Every second: (20-0) + 1
                .showTickMarks(false)
                .snapToTickMarks(true)
                .addListener(e -> simulation.setTimeStep((int) e.getValue()));
        timeStepSlider
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Time Step (in seconds)")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        Slider heaterOutputPowerSlider = guiController.addSlider("heaterOutputPower")
                .setRange(0, 2000)
                .setValue(0)
                .setPosition(16, 286)
                .setSize(160, 20)
                .setNumberOfTickMarks(41) //Every 50W: (2000-0)/50 + 1
                .showTickMarks(false)
                .snapToTickMarks(true)
                .addListener(e -> simulation.changeHeaterPower(e.getValue()));
        heaterOutputPowerSlider
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Heater output power (in watts)")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);
    }

    // =========================== ControlP5 GUI Listeners ===========================

}
