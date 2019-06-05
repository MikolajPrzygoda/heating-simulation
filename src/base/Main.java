package base;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;
import simulation.RoomPlan;
import simulation.Simulation;
import simulation.cell.Cell;
import simulation.scenario.HeatingUpScenarioFast;
import simulation.scenario.HeatingUpScenarioSlow;
import simulation.scenario.MaintainTemperatureScenario;
import simulation.scenario.Test;
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

    private static final int MANUAL_MODE = 0;
    private static final int HEATING_UP_SLOW = 1;
    private static final int HEATING_UP_FAST = 2;
    private static final int MAINTAINING = 3;
    private static final int TEST_SCENARIO = 4;

    public Simulation simulation;
    public Frame topFrame;
    public Frame frontFrame;
    public Frame leftSideFrame;
    public ControlP5 guiController;


    @Override
    public void settings(){
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup(){
        createGUI();

        simulation = new Simulation(new RoomPlan.Factory().build());

        // Create three frames in three quarters of the window. each showing a room's cross-section
        // from a different perspective.
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

        // FPS counter
        surface.setTitle("FPS: " + String.valueOf(frameRate).substring(0, 4));

        // Update elapsedTime and energyUsed labels
        int elapsedTime = simulation.getElapsedTime();
        ((Textlabel) guiController.getController("elapsedTime")).setText(
                "Elapsed time: " + elapsedTime / 3600 + "h" + elapsedTime / 60 % 60 + "m"
        );
        String usedEnergyText = simulation.getUsedEnergy() / 1000f / 3600f + "";
        usedEnergyText = usedEnergyText.length() > 5 ? usedEnergyText.substring(0, 5) : usedEnergyText;
        ((Textlabel) guiController.getController("energyUsed")).setText(
                "Energy used: " + usedEnergyText + "kWh"
        );

        // Update cursor temp label
        Cell cell;
        if(mouseX < WIDTH / 2){
            cell = leftSideFrame.getCellAt(mouseX, mouseY);
        }
        else if(mouseY < HEIGHT / 2){
            cell = topFrame.getCellAt(mouseX - WIDTH / 2, mouseY);
        }
        else{
            cell = frontFrame.getCellAt(mouseX - WIDTH / 2, mouseY - HEIGHT / 2);
        }
        double temp = cell == null ? Double.NEGATIVE_INFINITY : cell.getTemperature();

        ((Textlabel) guiController.getController("cursorTemp")).setText(
                "At cursor: " + (temp + "").substring(0, 4) + "°C"
        );

        // Update average air temp label
        ((Textlabel) guiController.getController("averageAirTemp")).setText(
                "Average temperature\nin the room: " + (simulation.getAverageTemperature() + "").substring(0, 4) + "°C"
        );

        // If in scenario mode
        if(simulation.scenario != null){
            // 1) Update simulation parameters
            guiController.getController("timeStep").setValue(simulation.scenario.TIME_STEP);
            guiController.getController("heaterOutputPower").setValue(simulation.scenario.getHeaterPower(simulation));
            // 2) Check if the scenario goal has been reached
            if(simulation.scenario.isGoalReached(simulation)){
                ((Toggle) guiController.getController("pauseSimulation")).setValue(true);
                simulation.scenario = null;
                showEndWindow(simulation);
            }
        }
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
            case 'n':
                simulation.update();
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
        guiController.addLabel("energyUsed")
                .setPosition(416, 76);
        guiController.addLabel("averageAirTemp")
                .setPosition(416, 106);
        guiController.addLabel("cursorTemp")
                .setPosition(416, 146);

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
                .setText("[t]ranslucent grid (Hard on FPS)")
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
                .addListener(e -> simulation.changeHeaterPower((int) e.getValue()));
        heaterOutputPowerSlider
                .getCaptionLabel()
                .toUpperCase(false)
                .setText("Heater output power (in watts)")
                .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
                .setPaddingX(6);

        DropdownList scenariosDropdown = guiController.addDropdownList("scenarios")
                .setPosition(416, 176)
                .setSize(160, 200);
        scenariosDropdown.setItemHeight(22);
        scenariosDropdown.setBarHeight(22);
        scenariosDropdown.getCaptionLabel().toUpperCase(false);
        scenariosDropdown.getCaptionLabel().getStyle().marginTop = 3;
        scenariosDropdown.setCaptionLabel("Choose scenario...");

        scenariosDropdown.getValueLabel().toUpperCase(false);
        scenariosDropdown.getValueLabel().getStyle().marginTop = 3;

        scenariosDropdown.addItem("Manual", MANUAL_MODE);
        scenariosDropdown.addItem("Heating up (slow)", HEATING_UP_SLOW);
        scenariosDropdown.addItem("Heating up (fast)", HEATING_UP_FAST);
        scenariosDropdown.addItem("Maintain heat", MAINTAINING);
//        scenariosDropdown.addItem("DEBUG SCENARIO", TEST_SCENARIO);

        scenariosDropdown.addListener(this::onScenarioChange);
    }

    private void showEndWindow(Simulation simulation){
        SummaryWindow popup = new SummaryWindow(simulation.getElapsedTime(), simulation.getUsedEnergy());
        PApplet.runSketch(new String[]{"-- Processing --"}, popup);
    }

    // =========================== ControlP5 GUI Listeners ===========================
    void onScenarioChange(ControlEvent event){
        // Pause simulation
        ((Toggle) guiController.getController("pauseSimulation")).setValue(true);

        // Set scenario
        switch((int) event.getValue()){
            case MANUAL_MODE:
                simulation.scenario = null;
                break;
            case HEATING_UP_SLOW:
                simulation.scenario = new HeatingUpScenarioSlow();
                break;
            case HEATING_UP_FAST:
                simulation.scenario = new HeatingUpScenarioFast();
                break;
            case MAINTAINING:
                simulation.scenario = new MaintainTemperatureScenario();
                break;
            case TEST_SCENARIO:
                simulation.scenario = new Test();
                break;
        }

        // Setup sliders
        switch((int) event.getValue()){
            case MANUAL_MODE:
                guiController.getController("timeStep").unlock();
                guiController.getController("timeStep").setColorForeground(color(0, 116, 217));
                guiController.getController("heaterOutputPower").unlock();
                guiController.getController("heaterOutputPower").setColorForeground(color(0, 116, 217));
                break;
            case HEATING_UP_SLOW:
            case HEATING_UP_FAST:
            case MAINTAINING:
            case TEST_SCENARIO:
                guiController.getController("timeStep").lock();
                guiController.getController("timeStep").setColorForeground(color(120));
                guiController.getController("heaterOutputPower").lock();
                guiController.getController("heaterOutputPower").setColorForeground(color(120));
                break;
        }

        // Setup simulation state
        simulation.reset();
        simulation.setTimeStep(0);
        simulation.setElapsedTime(0);
        simulation.setUsedEnergy(0);

        if(simulation.scenario == null){ // Case when scenario =/= null is considered in update method
            guiController.getController("timeStep").setValue(5);
            guiController.getController("heaterOutputPower").setValue(500);
        }
    }
}
