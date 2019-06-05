package base;

import processing.core.PApplet;

public class SummaryWindow extends PApplet{

    private int elapsedTime;
    private int usedEnergy;

    public SummaryWindow(int elapsedTime, int usedEnergy){
        this.elapsedTime = elapsedTime;
        this.usedEnergy = usedEnergy;
    }

    public void settings(){
        size(250, DEFAULT_HEIGHT);
    }

    public void draw(){
        surface.setTitle("End of simulation");

        background(0);
        text("Scenario goal of has been reached.", 16, 32);
        text("Elapsed time: " + elapsedTime / 3600 + "h " + elapsedTime / 60 % 60 + "m,", 16, 54);
        text("Energy used: " + (usedEnergy / 1000f / 3600f + "").substring(0, 5) + "kWh.", 16, 76);
    }

    @Override
    public void keyPressed(){
        if(key == ' ')
            exit();
    }
}
