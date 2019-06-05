package simulation;

import simulation.cell.AirCell;
import simulation.cell.Cell;
import simulation.cell.HeaterCell;
import simulation.scenario.Scenario;
import util.int3d;

import java.util.ArrayList;
import java.util.List;

public class Simulation{

    private Cell[][][] room;
    private int depth;
    private int height;
    private int width;
    private int timeStep = 5;
    private int elapsedTime = 0;
    private int usedEnergy = 0;
    private int currentPower = 0;
    private List<HeaterCell> heaterCells = new ArrayList<>();

    private int3d roomAirStart;
    private int3d roomAirEnd;

    public Scenario scenario = null;

    public Simulation(RoomPlan roomPlan){

        this.depth = roomPlan.getRoomDepth();
        this.height = roomPlan.getRoomHeight();
        this.width = roomPlan.getRoomWidth();


        // Save information about the position of all the air inside the room
        roomAirStart = new int3d(
                1 + roomPlan.getWallThickness(),
                1 + roomPlan.getWallThickness(),
                1 + roomPlan.getWallThickness()
        );
        roomAirEnd = new int3d(
                roomPlan.getRoomDepth() - (1 + roomPlan.getWallThickness()),
                roomPlan.getRoomHeight() - (1 + roomPlan.getWallThickness()),
                roomPlan.getRoomWidth() - (1 + roomPlan.getWallThickness())
        );

        // Create room arrays
        room = new Cell[depth][height][width];
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    room[z][y][x] = roomPlan.getCellAt(new int3d(z, y, x));

                    if(room[z][y][x] instanceof HeaterCell){
                        heaterCells.add((HeaterCell) room[z][y][x]);
                    }
                }
            }
        }
    }

    public Cell getCell(int z, int y, int x){
        return room[z][y][x];
    }

    public int getDepth(){
        return depth;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getTimeStep(){
        return timeStep;
    }

    public void setTimeStep(int timeStep){
        this.timeStep = timeStep;
    }

    public int getElapsedTime(){
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime){
        this.elapsedTime = elapsedTime;
    }

    public int getUsedEnergy(){
        return usedEnergy;
    }

    public void setUsedEnergy(int energy){
        this.usedEnergy = energy;
    }

    public void reset(){
        for(int z = 0; z < depth - 1; z++){
            for(int y = 0; y < height - 1; y++){
                for(int x = 0; x < width - 1; x++){
                    room[z][y][x].setTemperature(Cell.INITIAL_TEMPERATURE);
                }
            }
        }
    }

    /**
     * <h3>Calculate heat flow between cells.</h3>
     * There are two phases:<br />
     * <ol>
     * <li>
     * Heat conduction between two neighbouring cells: <br />
     * The method for that is called only once between two cells, and 'away' from simulation's origin. <br />
     * The last page/column/row isn't being considered as it is not needed, the outside air cell has constant<br />
     * (or at least not depending on the heat coming out of the room cells) temperature so the amount of heat<br />
     * flow is irrelevant.<br />
     * Second parameter is the distance between two cells, so for cells:
     * <ul>
     * <li>sharing a side -> sqrt(1)</li>
     * <li>sharing an edge -> sqrt(2)</li>
     * <li>sharing a point -> sqrt(3)</li>
     * </ul>
     * </li>
     * <li>
     * Heat convection between two AirCells mainly in the upwards direction. <br />
     * This transfer happens only between AirCells, and works separate from conventional heat transfer. <br />
     * Firstly, calculates the energy flow in the upward direction, if there little to none, then transfer <br />
     * heat sideways. <br />
     * The ratio of how much heat difference is supposed to go to other cells is calculated to be roughly 80%, <br />
     * so if the heat goes upwards, each cell should get 0.8/9 = 8% of the temperature difference. After that <br />
     * the flow is further decreased by the distance between cells, similar to the heat transfer <br />
     * via conduction.
     * </li>
     * </ol>
     */
    public void update(){

        // First phase: hear conduction
        for(int z = 0; z < depth - 1; z++){
            for(int y = 0; y < height - 1; y++){
                for(int x = 0; x < width - 1; x++){
                    Cell currentCell = room[z][y][x];

                    currentCell.updateEnergyFlow(room[z + 1][y][x], 1, timeStep);
                    currentCell.updateEnergyFlow(room[z][y + 1][x], 1, timeStep);
                    currentCell.updateEnergyFlow(room[z][y][x + 1], 1, timeStep);

                    currentCell.updateEnergyFlow(room[z + 1][y + 1][x], 1.414, timeStep);
                    currentCell.updateEnergyFlow(room[z + 1][y][x + 1], 1.414, timeStep);
                    currentCell.updateEnergyFlow(room[z][y + 1][x + 1], 1.414, timeStep);

                    currentCell.updateEnergyFlow(room[z + 1][y + 1][x + 1], 1.732, timeStep);
                }
            }
        }

        //Apply conduction changes
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    room[z][y][x].applyEnergyChange(timeStep);
                }
            }
        }

        // Second phase of heat transfer: air convection.
        for(int z = roomAirStart.z; z < roomAirEnd.z; z++){
            for(int y = roomAirStart.y; y < roomAirEnd.y; y++){
                for(int x = roomAirStart.x; x < roomAirEnd.x; x++){
                    Cell currentCell = room[z][y][x];

                    // Calculate the change to the cells above (point (0,0,0) is on the top of the room, so y-1 == up).
                    currentCell.updateConvectiveEnergyFlow(room[z][y - 1][x], 0.08, 1, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z][y - 1][x + 1], 0.08, 1.41, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z][y - 1][x - 1], 0.08, 1.41, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z + 1][y - 1][x], 0.08, 1.41, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z - 1][y - 1][x], 0.08, 1.41, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z + 1][y - 1][x + 1], 0.08, 1.73, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z + 1][y - 1][x - 1], 0.08, 1.73, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z - 1][y - 1][x + 1], 0.08, 1.73, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z - 1][y - 1][x - 1], 0.08, 1.73, timeStep);

                    // Now consider cells to the sides. If the temperature change of current cell is less that 10%
                    // of it's current temperature (the case when air above this cell is on the similar temperature
                    // level as currentCell, so the air will flow to the sides instead of upwards) send more to the
                    // sides than usually.
                    double sidewaysRatio =
                            currentCell.getTemperatureChange() < currentCell.getTemperature() * 0.1 ? 0.1 : 0.04;
                    currentCell.updateConvectiveEnergyFlow(room[z][y][x + 1], sidewaysRatio, 1, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z][y][x - 1], sidewaysRatio, 1, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z + 1][y][x], sidewaysRatio, 1, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z - 1][y][x], sidewaysRatio, 1, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z + 1][y][x + 1], sidewaysRatio, 1.41, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z + 1][y][x - 1], sidewaysRatio, 1.41, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z - 1][y][x + 1], sidewaysRatio, 1.41, timeStep);
                    currentCell.updateConvectiveEnergyFlow(room[z - 1][y][x - 1], sidewaysRatio, 1.41, timeStep);
                }
            }
        }

        //Apply convection changes
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    room[z][y][x].applyTemperatureChange();
                }
            }
        }

        //Increase time elapsed and energy used
        this.elapsedTime += timeStep;
        this.usedEnergy += this.currentPower * timeStep;
    }

    public double getMinValue(){
        double result = Double.POSITIVE_INFINITY;

        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(room[z][y][x].getTemperature() < result)
                        result = room[z][y][x].getTemperature();
                }
            }
        }

        return result - 1;
    }

    public double getMaxValue(){
        double result = Double.NEGATIVE_INFINITY;

        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){

                for(int x = 0; x < width; x++){
                    if(room[z][y][x].getTemperature() > result)
                        result = room[z][y][x].getTemperature();
                }
            }
        }

        return result + 1;
    }

    public void changeHeaterPower(int heaterPower){
        this.currentPower = heaterPower;
        double cellsPower = heaterPower / heaterCells.size();
        heaterCells.forEach(cell -> cell.setPowerOutput(cellsPower));
    }

    public double getAverageTemperature(){
        double sum = 0;
        int count = 0;

        for(int z = roomAirStart.z; z < roomAirEnd.z; z++){
            for(int y = roomAirStart.y; y < roomAirEnd.y; y++){
                for(int x = roomAirStart.x; x < roomAirEnd.x; x++){
                    if(room[z][y][x] instanceof AirCell){
                        count++;
                        sum += room[z][y][x].getTemperature();
                    }
                }
            }
        }

        return sum / count;
    }
}
